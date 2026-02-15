package com.campus.service;

import com.campus.entity.Category;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CategoryService {
    List<Category> list();

    List<Category> listFacility();
}
