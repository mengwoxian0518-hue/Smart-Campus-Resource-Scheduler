package com.campus.controller.admin;

import com.alibaba.excel.EasyExcel;
import com.campus.Result.PageResult;
import com.campus.Result.Result;
import com.campus.annotation.Log;
import com.campus.constant.JwtClaimsConstant;
import com.campus.dto.AdminLoginDto;
import com.campus.dto.StaffPageListDto;
import com.campus.entity.Admin;
import com.campus.entity.Staff;
import com.campus.entity.SysLog;
import com.campus.mapper.SysLogMapper;
import com.campus.properties.JwtProperties;
import com.campus.service.AdminService;
import com.campus.utils.JwtUtil;
import com.campus.vo.AdminLoginVO;
import com.campus.vo.SysLogExportVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@Slf4j
@Api(tags = "管理员接口")
public class AdminController {
    @Autowired
    AdminService adminservice;
    @Autowired
    JwtProperties jwtProperties;
    @Autowired
    SysLogMapper sysLogMapper;
    @ApiOperation("管理员登录")
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
    @ApiOperation("员工列表")
    @GetMapping("staff/page")
    public Result listStaff(StaffPageListDto staffPageListDto)
    {
        PageResult<Staff> pageStaffList = adminservice.listStaff(staffPageListDto);
        return Result.success(pageStaffList);
    }
    @ApiOperation("员工添加")
    @PostMapping("staff")
    @Log(module = "员工管理", action = "增加员工")
    public Result<Staff> addStaff(@RequestBody Staff staff)
    {adminservice.addStaff(staff);
        return Result.success() ;
    }
    @ApiOperation("员工删除")
    @DeleteMapping("staff/{id}")
    @Log(module = "员工管理", action = "删除员工")
    public Result deleteStaff(@PathVariable Long id)
    {
        adminservice.deleteStaff(id);
        return Result.success();
    }
    @ApiOperation("员工查询")
    @GetMapping("staff/{id}")
    public Result<Staff> getStaffById(@PathVariable Long id)
    {
        Staff staff = adminservice.getStaffById(id);
        return Result.success(staff);
    }
    @ApiOperation("员工修改")
    @PostMapping("staff/update")
    @Log(module = "员工管理", action = "修改员工")
    public Result update(@RequestBody Staff staff)
    {
        adminservice.update(staff);
        return Result.success();
    }
    @ApiOperation("员工状态修改")
    @PostMapping("staff/status/{status}")
    @Log(module = "员工管理", action = "更改员工状态")
    public Result startOrStop(@PathVariable Integer status,Long id)
    {
        adminservice.startOrStop(status,id);
        return Result.success();
    }
    @ApiOperation("日志导出")
    @GetMapping("/log/export")
    public void exportLog(@RequestParam("days") Integer days, HttpServletResponse response) throws IOException {
        LocalDate startDate = LocalDate.now().minusDays(days);
        List<SysLog> logs = sysLogMapper.selectLogsSince(startDate);
        List<SysLogExportVO> exportData = new ArrayList<>();
        for (SysLog sysLog : logs) {
            SysLogExportVO vo = new SysLogExportVO();
            BeanUtils.copyProperties(sysLog, vo);
            if (sysLog.getCreateTime() != null) {
                vo.setCreateTime(sysLog.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            }
            exportData.add(vo);
        }
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("操作日志", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), SysLogExportVO.class)
                .sheet("日志数据")
                .doWrite(exportData);
        log.info("导出日志报表成功，共 {} 条数据", exportData.size());
    }
}
