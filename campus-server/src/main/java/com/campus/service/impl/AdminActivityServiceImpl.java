package com.campus.service.impl;

import com.campus.Result.PageResult;
import com.campus.dto.ActivityDTO;
import com.campus.dto.ActivityPageQueryDTO;
import com.campus.entity.Activity;
import com.campus.mapper.ActivityMapper;
import com.campus.service.AdminActivityService;
import com.campus.vo.ActivityVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class AdminActivityServiceImpl implements AdminActivityService {

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    // 详情缓存 Key 前缀
    private static final String REDIS_KEY_ACTIVITY_DETAIL = "activity:detail:";
    // 库存缓存 Key 前缀
    private static final String REDIS_KEY_ACTIVITY_STOCK = "activity:stock:";

    @Override
    public PageResult pageQuery(ActivityPageQueryDTO dto) {
        PageHelper.startPage(dto.getPage(), dto.getPageSize());
        List<ActivityVO> list = activityMapper.pageQuery(dto);
        Page<ActivityVO> page = (Page<ActivityVO>) list;
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(ActivityDTO dto) {
        Activity activity = new Activity();
        BeanUtils.copyProperties(dto, activity);
        activity.setCurrentParticipants(0); // 新增活动初始人数为0
        activity.setCreateTime(LocalDateTime.now());
        activity.setUpdateTime(LocalDateTime.now());
        
        activityMapper.insert(activity);
        
        // 新增活动，不需要清理详情缓存，因为还没有详情缓存
        // 需要初始化库存缓存吗？一般是上线或审核通过后才初始化，这里简单起见，如果状态是报名中，可以初始化
        if (Integer.valueOf(1).equals(activity.getStatus())) {
            refreshRedisStock(activity);
        }
    }

    @Override
    public ActivityVO getById(Long id) {
        // 管理端直接查库，不走缓存，保证数据最新
        return activityMapper.getById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ActivityDTO dto) {
        Activity activity = new Activity();
        BeanUtils.copyProperties(dto, activity);
        activity.setUpdateTime(LocalDateTime.now());
        
        activityMapper.update(activity);
        
        // 修改活动后，删除详情缓存，保证用户端下次查到最新的
        redisTemplate.delete(REDIS_KEY_ACTIVITY_DETAIL + dto.getId());
        
        // 如果修改了最大人数或状态，可能需要更新库存缓存
        // 简单策略：直接删除库存缓存，下次用户访问时懒加载（需配合 Lua 脚本中的懒加载逻辑，或者这里强制刷新）
        // 由于 Lua 脚本通常不负责加载库存，这里我们主动刷新库存
        ActivityVO latest = activityMapper.getById(dto.getId());
        Activity latestEntity = new Activity();
        BeanUtils.copyProperties(latest, latestEntity);
        refreshRedisStock(latestEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        activityMapper.deleteById(id);
        // 删除缓存
        redisTemplate.delete(REDIS_KEY_ACTIVITY_DETAIL + id);
        redisTemplate.delete(REDIS_KEY_ACTIVITY_STOCK + id);
    }

    /**
     * 刷新 Redis 中的活动库存
     * Key: activity:stock:{id}
     * Value: 剩余名额 (max - current)
     */
    private void refreshRedisStock(Activity activity) {
        // 只有报名中(1)才需要库存缓存
        if (activity.getStatus() == 1) {
            int stock = activity.getMaxParticipants() - activity.getCurrentParticipants();
            if (stock < 0) stock = 0;
            redisTemplate.opsForValue().set(REDIS_KEY_ACTIVITY_STOCK + activity.getId(), String.valueOf(stock));
        } else {
            // 如果活动关闭或结束，删除库存缓存，Lua 脚本查不到库存会报错或走兜底
            redisTemplate.delete(REDIS_KEY_ACTIVITY_STOCK + activity.getId());
        }
    }
}
