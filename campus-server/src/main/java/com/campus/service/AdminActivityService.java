package com.campus.service;

import com.campus.dto.ActivityDTO;
import com.campus.dto.ActivityPageQueryDTO;
import com.campus.vo.ActivityVO;
import com.campus.Result.PageResult;

public interface AdminActivityService {
    /**
     * 分页查询活动
     */
    PageResult pageQuery(ActivityPageQueryDTO dto);

    /**
     * 新增活动
     */
    void add(ActivityDTO dto);

    /**
     * 根据ID查询活动详情
     */
    ActivityVO getById(Long id);

    /**
     * 修改活动
     */
    void update(ActivityDTO dto);

    /**
     * 删除活动
     */
    void delete(Long id);
}
