package com.campus.service.impl;

import com.campus.mapper.UserQueryMapper;
import com.campus.service.UserQueryService;
import com.campus.vo.CategoryVO;
import com.campus.vo.UserQueryVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserQueryServiceImpl implements UserQueryService {
    @Autowired
    UserQueryMapper userQueryMapper;
    @Override
    public List<UserQueryVO> getCategories(String type) {
        if(type.equals("RESOURCE"))
        {
            return userQueryMapper.getResource();
        }else if(type.equals("VENUE"))
        {
            return userQueryMapper.getFacility();
        }
        else return null;
    }

    @Override
    public List<CategoryVO> getList(Long id,String type) {
        if(type.equals("RESOURCE"))
        {
            return userQueryMapper.getResourceList(id);
        }else if(type.equals("VENUE"))
        {
            return userQueryMapper.getFacilityList(id);
        }
        else return null;
    }

    @Override
    public CategoryVO getDetail(Long id, String type) {
        if(type.equals("RESOURCE"))
        {
            return userQueryMapper.getDetailByResource(id);
        }
        else if(type.equals("VENUE"))
        {
            return userQueryMapper.getDetailByVenue(id);
        }
        else return null;
    }
}
