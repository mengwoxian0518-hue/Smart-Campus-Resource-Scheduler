package com.campus.mapper;

import com.campus.entity.ActivitySignup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ActivitySignupMapper {

    /**
     * 新增报名记录
     * @param signup
     */
    void insert(ActivitySignup signup);

    /**
     * 查询用户是否已报名某活动
     * @param userId
     * @param activityId
     * @return
     */
    Integer countByUserIdAndActivityId(@Param("userId") Long userId, @Param("activityId") Long activityId);
}
