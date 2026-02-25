package com.campus.controller.user;

import com.campus.Result.Result;
import com.campus.entity.User;
import com.campus.service.UserProfileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/user/profile")
@Api(tags = "用户资料管理")
public class UserProfileController {
    @Autowired
    UserProfileService userProfileService;
    @ApiOperation("获取用户资料")
    @GetMapping
    public Result<User> getProfile() {
        return Result.success(userProfileService.getProfile());
    }
    @ApiOperation("更新用户资料")
    @PutMapping
    public Result<User> updateProfile(@RequestBody User user) {
        User userZ = userProfileService.updateProfile(user);
        return Result.success(userZ);
    }
}
