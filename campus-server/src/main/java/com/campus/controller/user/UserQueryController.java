package com.campus.controller.user;

import com.campus.Result.Result;
import com.campus.service.UserQueryService;
import com.campus.vo.CategoryVO;
import com.campus.vo.UserQueryVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/user/query")
@Api(tags = "用户查询接口")
public class UserQueryController {
    @Autowired
    UserQueryService userQueryService;
    @ApiOperation("获取分类列表")
    @GetMapping("categories")
    public Result<List<UserQueryVO>> getCategories(String type)
    {
        List<UserQueryVO> userQueryVO = userQueryService.getCategories(type);
        return Result.success(userQueryVO);
    }
    @ApiOperation("获取查询列表")
    @GetMapping("list")
    public Result<List<CategoryVO>> getList(Long categoryId,String type)
    {
        List<CategoryVO> categoryvo = userQueryService.getList(categoryId,type);
        return Result.success(categoryvo);
    }
    @ApiOperation("获取查询详情")
    @GetMapping("detail")
    public Result<CategoryVO> getDetail(Long id,String type)
    {
        CategoryVO CategoryVO = userQueryService.getDetail(id,type);
        return Result.success(CategoryVO);
    }

}
