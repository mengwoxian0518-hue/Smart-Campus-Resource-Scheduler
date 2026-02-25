package com.campus.controller.admin;

import com.campus.Result.PageResult;
import com.campus.Result.Result;
import com.campus.annotation.Log;
import com.campus.dto.CreditChangeDTO;
import com.campus.dto.UserPageQueryDTO;
import com.campus.entity.User;
import com.campus.service.AdminUserService;
import com.campus.vo.UserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/admin/user")
@Api(tags = "用户管理")
public class AdminUserController {
    @Autowired
    AdminUserService adminUserService;
    @ApiOperation("分页查询用户信息")
    @GetMapping("/page")
    public Result<PageResult<UserVO>> page(UserPageQueryDTO userPageQueryDTO)
    {
        PageResult<UserVO> page = adminUserService.page(userPageQueryDTO);
        return Result.success(page);
    }
    @ApiOperation("修改信用分")
    @PutMapping("/credit")
    @Log(module = "用户管理", action = "修改信用分")
    public Result updateCredit(@RequestBody CreditChangeDTO creditChangeDTO)
    {
        adminUserService.updateCredit(creditChangeDTO);
        return Result.success();
    }
}
