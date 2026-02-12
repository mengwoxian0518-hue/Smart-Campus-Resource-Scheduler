package com.campus.service;

import com.campus.Result.PageResult;
import com.campus.dto.AdminLoginDto;
import com.campus.dto.StaffPageListDto;
import com.campus.entity.Admin;
import com.campus.entity.Staff;

public interface AdminService {
    Admin login(AdminLoginDto adminLoginDto);

    PageResult<Staff> listStaff(StaffPageListDto staffPageListDto);
}
