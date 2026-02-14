package com.campus.service;

import com.campus.Result.PageResult;
import com.campus.dto.ResourcePageQueryDTO;
import com.campus.entity.Resource;
import com.campus.vo.ResourceVO;

import java.util.List;

public interface ResourceService {
    PageResult<ResourceVO> pageList(ResourcePageQueryDTO resourcePageQueryDTO);

    void add(Resource resource);

    void update(Resource resource);

    void delete(List<Long> id);

    Resource getById(Long id);

    void startOrStop(Integer status, Long id);
}
