package com.campus.service;

import com.campus.Result.PageResult;
import com.campus.dto.FacilityPageQueryDto;
import com.campus.entity.Facility;
import com.campus.vo.FacilityVO;

import java.util.List;

public interface FacilityService {
    List<Facility> list();

    PageResult<FacilityVO> page(FacilityPageQueryDto page);

    Facility getById(Long id);

    void add(Facility facility);

    void update(Facility facility);

    void delete(List<Long> ids);

    void startOrStop(Integer status, Long id);
}
