package com.campus.controller.user;

import com.campus.Result.Result;
import com.campus.service.UserQueryService;
import com.campus.vo.CategoryVO;
import com.campus.vo.UserQueryVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/user/query")
public class UserQueryController {
    @Autowired
    UserQueryService userQueryService;
    @GetMapping("categories")
    public Result<List<UserQueryVO>> getCategories(String type)
    {
        List<UserQueryVO> userQueryVO = userQueryService.getCategories(type);
        return Result.success(userQueryVO);
    }
    @GetMapping("list")
    public Result<List<CategoryVO>> getList(Long categoryId,String type)
    {
        List<CategoryVO> categoryvo = userQueryService.getList(categoryId,type);
        return Result.success(categoryvo);
    }
    @GetMapping("detail")
    public Result<CategoryVO> getDetail(Long id,String type)
    {
        CategoryVO CategoryVO = userQueryService.getDetail(id,type);
        return Result.success(CategoryVO);
    }
}
