package com.campus.controller.admin;

import com.campus.Result.Result;
import com.campus.entity.Category;
import com.campus.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/admin/category")
@Api(tags = "类别管理")
public class CategoryController {
    @Autowired
    CategoryService categoryService;
    @ApiOperation("查询类别")
    @GetMapping("/list")
    public Result<List<Category>> list(Integer type) {
        List<Category> list=new ArrayList<>();
        if(type==1)
        {
            list = categoryService.list();
        }
        else if (type==2)
        {
            list = categoryService.listFacility();
        }
        return Result.success(list);
    }
}
