package com.campus.controller.user;

import com.campus.Result.Result;
import com.campus.dto.CodeDto;
import com.campus.service.UserLoginService;
import com.campus.vo.UserLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/user/login")
public class UserLoginController {
    @Autowired
    UserLoginService userLoginService;
    @PostMapping
    public Result<UserLoginVO> login(@RequestBody CodeDto UserCode)
    {
        UserLoginVO login = userLoginService.Login(UserCode.getCode());
        return Result.success(login);
    }
}
