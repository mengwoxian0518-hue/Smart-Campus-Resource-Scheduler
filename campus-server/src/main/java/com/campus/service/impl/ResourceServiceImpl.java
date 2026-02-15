package com.campus.service.impl;

import com.campus.Result.PageResult;
import com.campus.dto.ResourcePageQueryDTO;
import com.campus.entity.Resource;
import com.campus.mapper.ResourceMapper;
import com.campus.service.ResourceService;
import com.campus.vo.ResourceVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ResourceServiceImpl implements ResourceService {
    @Autowired
    ResourceMapper resourceMapper;
    @Override
    public PageResult<ResourceVO> pageList(ResourcePageQueryDTO resourcePageQueryDTO) {
        PageHelper.startPage(resourcePageQueryDTO.getPage(), resourcePageQueryDTO.getPageSize());
        Page<ResourceVO> resourceVOS = (Page<ResourceVO>)resourceMapper.pageList(resourcePageQueryDTO);
        return new PageResult<ResourceVO>(resourceVOS.getTotal(), resourceVOS.getResult());
    }

    @Override
    public void add(Resource resource) {
        resource.setStatus(1);
        resourceMapper.add(resource);
    }

    @Override
    public void update(Resource resource) {
        resource.setStatus(1);
        resourceMapper.update(resource);
    }

    @Override
    public void delete(List<Long> id) {
        resourceMapper.delete(id);
    }

    @Override
    public Resource getById(Long id) {
        Resource byId = resourceMapper.getById(id);
        return byId;
    }

    @Override
    public void startOrStop(Integer status, Long id) {
        resourceMapper.startOrStop(status,id);
    }

    @Override
    public List<Resource> list(Long id) {
        List<Resource> list = resourceMapper.list(id);
        return list;
    }
}
