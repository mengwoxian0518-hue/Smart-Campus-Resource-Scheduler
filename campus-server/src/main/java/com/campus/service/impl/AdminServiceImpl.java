package com.campus.service.impl;

import com.campus.Result.PageResult;
import com.campus.dto.AdminLoginDto;
import com.campus.dto.StaffPageListDto;
import com.campus.entity.Admin;
import com.campus.entity.Staff;
import com.campus.mapper.AdminMapper;
import com.campus.service.AdminService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;

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

    @Override
    public PageResult<Staff> listStaff(StaffPageListDto staffPageListDto) {
        Page<Staff> page = PageHelper.startPage(staffPageListDto.getPage(), staffPageListDto.getPageSize());
        List<Staff> staff = adminMapper.listStaff(staffPageListDto);
        PageResult<Staff> pageStaffList = new PageResult<Staff>(page.getTotal(), page.getResult());
        return pageStaffList;
    }
}
