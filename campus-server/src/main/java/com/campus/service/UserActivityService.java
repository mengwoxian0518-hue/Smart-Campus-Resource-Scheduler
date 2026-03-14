package com.campus.service;

import com.campus.Result.PageResult;
import com.campus.dto.ActivityPageQueryDTO;
import com.campus.vo.ActivityVO;

import java.util.List;

public interface UserActivityService {
    /**
     * 分页查询活动
     */
    PageResult pageQuery(ActivityPageQueryDTO dto);

    /**
     * 根据ID查询活动详情
     */
    ActivityVO getById(Long id);

    /**
     * 用户报名活动
     * @param id 活动ID
     */
    void signup(Long id);

    List<ActivityVO> myActivities();
}
