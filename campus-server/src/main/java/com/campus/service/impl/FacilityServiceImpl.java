package com.campus.service.impl;

import com.campus.Result.PageResult;
import com.campus.dto.FacilityPageQueryDto;
import com.campus.entity.Facility;
import com.campus.entity.Resource;
import com.campus.mapper.FacilityMapper;
import com.campus.service.FacilityService;
import com.campus.vo.FacilityVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class FacilityServiceImpl implements FacilityService {
    @Autowired
    FacilityMapper facilityMapper;
    @Override
    public List<Facility> list() {
        List<Facility> list = facilityMapper.list();
        return list;
    }

    @Override
    public PageResult<FacilityVO> page(FacilityPageQueryDto page) {
        PageHelper.startPage(page.getPage(), page.getPageSize());
        Page<FacilityVO>p=(Page<FacilityVO>) facilityMapper.pageList(page);
        return new PageResult<>(p.getTotal(),p.getResult());
    }

    @Override
    public Facility getById(Long id) {
        Facility byId = facilityMapper.getById(id);
        return byId;
    }

    @Override
    public void add(Facility facility) {
        facilityMapper.add(facility);
    }

    @Override
    public void update(Facility facility) {
        facilityMapper.update(facility);
    }

    @Override
    public void delete(List<Long> ids) {
        for (Long id : ids) {
            List<Resource> resources = facilityMapper.searchResource(id);
            if(resources!=null&&!resources.isEmpty()) throw new RuntimeException("该场所下还有资源，请清理");
        }
        facilityMapper.delete(ids);
    }

    @Override
    public void startOrStop(Integer status, Long id) {
        facilityMapper.startOrStop(status,id);
    }
}
