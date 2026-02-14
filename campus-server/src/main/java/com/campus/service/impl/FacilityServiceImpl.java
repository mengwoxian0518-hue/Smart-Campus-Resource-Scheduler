package com.campus.service.impl;

import com.campus.entity.Facility;
import com.campus.mapper.FacilityMapper;
import com.campus.service.FacilityService;
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
}
