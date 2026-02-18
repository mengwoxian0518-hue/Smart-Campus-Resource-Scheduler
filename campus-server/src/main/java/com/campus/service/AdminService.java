package com.campus.service;

import com.campus.Result.PageResult;
import com.campus.dto.AdminLoginDto;
import com.campus.dto.StaffPageListDto;
import com.campus.entity.Admin;
import com.campus.entity.Staff;
//Zmjjkk
public interface AdminService {
    Admin login(AdminLoginDto adminLoginDto);

    PageResult<Staff> listStaff(StaffPageListDto staffPageListDto);

    void addStaff(Staff staff);

    void deleteStaff(Long id);

    Staff getStaffById(Long id);

    void update(Staff staff);

    void startOrStop(Integer status, Long id);
}
