package com.campus.mapper;

import com.campus.dto.ActivityPageQueryDTO;
import com.campus.entity.Activity;
import com.campus.vo.ActivityVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ActivityMapper {

    /**
     * 新增活动
     * @param activity
     */
    void insert(Activity activity);

    /**
     * 分页查询活动
     * @param dto
     * @return
     */
    List<ActivityVO> pageQuery(ActivityPageQueryDTO dto);

    /**
     * 根据ID查询活动详情
     * @param id
     * @return
     */
    ActivityVO getById(Long id);

    /**
     * 修改活动信息
     * @param activity
     */
    void update(Activity activity);

    /**
     * 删除活动
     * @param id
     */
    void deleteById(Long id);

    /**
     * 扣减活动库存（数据库层面的原子操作）
     * @param id
     * @return 影响行数
     */
    int decreaseStock(Long id);

    /**
     * 增加当前报名人数
     * @param id
     */
    void increaseParticipants(Long id);

    List<ActivityVO> listMyActivities(Long userId);
}
