package com.campus.controller.admin;

import com.campus.Result.Result;
import com.campus.constant.JwtClaimsConstant;
import com.campus.dto.AdminLoginDto;
import com.campus.entity.Admin;
import com.campus.properties.JwtProperties;
import com.campus.service.AdminService;
import com.campus.utils.JwtUtil;
import com.campus.vo.AdminLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@Slf4j
public class AdminController {
    @Autowired
    AdminService adminservice;
    @Autowired
    JwtProperties jwtProperties;
    @PostMapping("/login")
    public Result login(@RequestBody AdminLoginDto adminLoginDto) {
        Admin admin = adminservice.login(adminLoginDto);
        if(admin==null) return Result.error("用户名或密码错误");
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, admin.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        AdminLoginVO employeeLoginVO = AdminLoginVO.builder()
                .id(admin.getId())
                .userName(admin.getUsername())
                .name(admin.getName())
                .token(token)
                .build();
      return Result.success(employeeLoginVO);
    }
}
