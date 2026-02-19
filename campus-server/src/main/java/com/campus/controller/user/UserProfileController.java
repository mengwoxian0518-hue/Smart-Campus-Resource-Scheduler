package com.campus.controller.user;

import com.campus.Result.Result;
import com.campus.entity.User;
import com.campus.service.UserProfileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/user/profile")
public class UserProfileController {
    @Autowired
    UserProfileService userProfileService;
    @GetMapping
    public Result<User> getProfile() {
        return Result.success(userProfileService.getProfile());
    }
    @PutMapping
    public Result<User> updateProfile(@RequestBody User user) {
        User userZ = userProfileService.updateProfile(user);
        return Result.success(userZ);
    }
}
