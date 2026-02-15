package com.campus.service.impl;

import com.campus.entity.Category;
import com.campus.mapper.CategoryMapper;
import com.campus.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryMapper categoryMapper;
    @Override
    public List<Category> list() {
        List<Category> list = categoryMapper.list();
        return list;
    }

    @Override
    public List<Category> listFacility() {
        List<Category> categories = categoryMapper.listFacility();
        return categories;
    }
}
