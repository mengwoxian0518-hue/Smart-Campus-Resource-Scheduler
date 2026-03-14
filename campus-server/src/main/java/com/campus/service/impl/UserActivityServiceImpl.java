package com.campus.service.impl;

import com.campus.Result.PageResult;
import com.campus.context.BaseContext;
import com.campus.dto.ActivityPageQueryDTO;
import com.campus.entity.ActivitySignup;
import com.campus.mapper.ActivityMapper;
import com.campus.mapper.ActivitySignupMapper;
import com.campus.service.UserActivityService;
import com.campus.vo.ActivityVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
@Slf4j
public class UserActivityServiceImpl implements UserActivityService {

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private ActivitySignupMapper activitySignupMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String REDIS_KEY_ACTIVITY_STOCK = "activity:stock:";
    private static final String REDIS_KEY_ACTIVITY_SIGNUP_USERS = "activity:signup:users:";
    private static final String REDIS_KEY_ACTIVITY_DETAIL = "activity:detail:";
    private static final String REDIS_KEY_ACTIVITY_SIGNUP_QUEUE = "queue.activity.signup";
    
    // Lua 脚本
    private DefaultRedisScript<Long> signupScript;

    @PostConstruct
    public void init() {
        signupScript = new DefaultRedisScript<>();
        signupScript.setResultType(Long.class);
        signupScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/activity_signup.lua")));
    }

    @Override
    public PageResult pageQuery(ActivityPageQueryDTO dto) {
        // 用户端只展示报名中(1)、已满额(2)、已结束(3)的活动，不展示已关闭(0)
        // 这里简单起见，如果 status 为 null，前端也没传，则不筛选，或默认查询非关闭
        // 建议前端传参控制，或者后端默认只查 1,2,3
        PageHelper.startPage(dto.getPage(), dto.getPageSize());
        List<ActivityVO> list = activityMapper.pageQuery(dto);
        Page<ActivityVO> page = (Page<ActivityVO>) list;
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public ActivityVO getById(Long id) {
        // 1. 先查缓存
        // TODO: 实现活动详情缓存逻辑 (VO序列化存入Redis)
        // 这里为了简化演示，暂不实现 VO 缓存，只做数据库查询 + 用户报名状态检查
        
        ActivityVO vo = activityMapper.getById(id);
        if (vo == null) {
            throw new RuntimeException("活动不存在");
        }
        
        // 2. 检查当前用户是否已报名
        Long userId = BaseContext.getCurrentId();
        if (userId != null) {
            // 先查 Redis Set
            String userSetKey = REDIS_KEY_ACTIVITY_SIGNUP_USERS + id;
            Boolean isMember = redisTemplate.opsForSet().isMember(userSetKey, userId.toString());
            if (Boolean.TRUE.equals(isMember)) {
                vo.setIsSigned(true);
            } else {
                // Redis 没有，再查 DB 兜底 (防止 Redis 数据丢失或未同步)
                Integer count = activitySignupMapper.countByUserIdAndActivityId(userId, id);
                vo.setIsSigned(count > 0);
                // 如果 DB 有但 Redis 没有，是否要回写 Redis？
                // 视情况而定，这里暂不回写，因为 Redis 主要用于高并发阻拦，DB 是最终事实
            }
        }
        
        return vo;
    }

    @Override
    public void signup(Long id) {
        Long userId = BaseContext.getCurrentId();
        
        // 1. 基础校验 (查 DB 或 缓存)
        ActivityVO activity = activityMapper.getById(id);
        if (activity == null) {
            throw new RuntimeException("活动不存在");
        }
        if (activity.getStatus() != 1) { // 只有报名中状态可报名
            throw new RuntimeException("活动未开始或已结束");
        }
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(activity.getSignupStartTime())) {
            throw new RuntimeException("报名尚未开始");
        }
        if (now.isAfter(activity.getSignupEndTime())) {
            throw new RuntimeException("报名已截止");
        }

        // 2. 准备 Lua 脚本参数
        String stockKey = REDIS_KEY_ACTIVITY_STOCK + id;
        String userSetKey = REDIS_KEY_ACTIVITY_SIGNUP_USERS + id;
        // 设置 Set 过期时间为活动结束后 30 天
        long expireAt = activity.getEndTime().plusDays(30).toEpochSecond(ZoneOffset.of("+8"));
        
        // 3. 执行 Lua 脚本 (原子扣减库存 + 记录用户)
        // Keys: {stockKey, userSetKey}
        // Args: {userId, expireTime}
        Long result = redisTemplate.execute(signupScript, 
                java.util.Arrays.asList(stockKey, userSetKey), 
                userId.toString(), String.valueOf(expireAt));

        if (result == null) {
            throw new RuntimeException("系统繁忙，请重试");
        }
        
        if (result == -1) {
            throw new RuntimeException("您已报名该活动，无需重复报名");
        }
        if (result == -2) {
            // 库存 Key 不存在，说明可能未预热或活动已下架
            // 尝试从 DB 同步库存到 Redis (懒加载预热)
            refreshStock(activity);
            // 再次尝试执行 Lua
            result = redisTemplate.execute(signupScript, 
                    java.util.Arrays.asList(stockKey, userSetKey), 
                    userId.toString(), String.valueOf(expireAt));
            if (result == -2) {
                throw new RuntimeException("活动报名通道未开启");
            }
        }
        if (result == 0) {
            throw new RuntimeException("名额已满，下次手慢无！");
        }

        try {
            String payload = userId + ":" + id + ":" + System.currentTimeMillis();
            redisTemplate.opsForList().rightPush(REDIS_KEY_ACTIVITY_SIGNUP_QUEUE, payload);
        } catch (Exception e) {
            log.error("Send stream message error: userId={}, activityId={}", userId, id, e);
            throw new RuntimeException("报名排队异常，请重试");
        }
    }

    @Override
    public List<ActivityVO> myActivities() {
        Long userId = BaseContext.getCurrentId();
        return activityMapper.listMyActivities(userId);
    }

    private void refreshStock(ActivityVO activity) {
        int stock = activity.getMaxParticipants() - activity.getCurrentParticipants();
        if (stock < 0) stock = 0;
        redisTemplate.opsForValue().set(REDIS_KEY_ACTIVITY_STOCK + activity.getId(), String.valueOf(stock));
    }
}
