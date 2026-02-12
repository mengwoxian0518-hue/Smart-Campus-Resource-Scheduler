package com.campus.service.impl;

import com.campus.dto.AdminLoginDto;
import com.campus.entity.Admin;
import com.campus.mapper.AdminMapper;
import com.campus.service.AdminService;
import com.google.j2objc.annotations.AutoreleasePool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
@Slf4j
public class AdminServiceImpl implements AdminService {
    @Autowired
    AdminMapper adminMapper;
    public Admin login(AdminLoginDto adminLoginDto) {
        String password = DigestUtils.md5DigestAsHex(adminLoginDto.getPassword().getBytes());
        adminLoginDto.setPassword(password);
        Admin login = adminMapper.login(adminLoginDto);
        return login;
    }
}
