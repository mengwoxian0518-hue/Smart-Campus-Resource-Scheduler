package com.campus.controller.admin;

import com.campus.Result.PageResult;
import com.campus.Result.Result;
import com.campus.constant.JwtClaimsConstant;
import com.campus.dto.AdminLoginDto;
import com.campus.dto.StaffPageListDto;
import com.campus.entity.Admin;
import com.campus.entity.Staff;
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
    @GetMapping("staff/page")
    public Result listStaff(StaffPageListDto staffPageListDto)
    {
        PageResult<Staff> pageStaffList = adminservice.listStaff(staffPageListDto);
        return Result.success(pageStaffList);
    }
    @PostMapping("staff")
    public Result<Staff> addStaff(@RequestBody Staff staff)
    {adminservice.addStaff(staff);
        return Result.success() ;
    }
    @DeleteMapping("staff/{id}")
    public Result deleteStaff(@PathVariable Long id)
    {
        adminservice.deleteStaff(id);
        return Result.success();
    }
    @GetMapping("staff/{id}")
    public Result<Staff> getStaffById(@PathVariable Long id)
    {
        Staff staff = adminservice.getStaffById(id);
        return Result.success(staff);
    }
    @PostMapping("staff/update")
    public Result update(@RequestBody Staff staff)
    {
        adminservice.update(staff);
        return Result.success();
    }
}
