package com.campus.service;

import com.campus.dto.AdminLoginDto;
import com.campus.entity.Admin;

public interface AdminService {
    Admin login(AdminLoginDto adminLoginDto);
}
