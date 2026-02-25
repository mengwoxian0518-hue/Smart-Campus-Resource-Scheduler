package com.campus.controller.admin;

import com.campus.Result.PageResult;
import com.campus.Result.Result;
import com.campus.annotation.Log;
import com.campus.dto.AllTypePageQueryDTO;
import com.campus.dto.AppointmentAuditDTO;
import com.campus.entity.Appointment;
import com.campus.service.AdminAppointmentService;
import com.campus.vo.AdminAppointmentDetailVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/admin/appointment")
@Api(tags = "预约管理")
public class AdminAppointmentController {
    @Autowired
    AdminAppointmentService adminAppointmentService;
    @ApiOperation("分页查询预约申请")
    @GetMapping("/page")
    public Result<PageResult<Appointment>> page(AllTypePageQueryDTO allTypePageQueryDTO){
        log.info("进入查询");
        PageResult<Appointment> page = adminAppointmentService.page(allTypePageQueryDTO);
        return Result.success(page);
    }
    @ApiOperation("预约审批")
    @PutMapping("/audit")
    @Log(module = "预约审批", action = "审核预约申请")
    public Result audit(@RequestBody AppointmentAuditDTO appointmentAuditDTO){
        adminAppointmentService.audit(appointmentAuditDTO);
        return Result.success();
    }
    @ApiOperation("预约详情")
    @GetMapping("/detail/{id}")
    public Result<AdminAppointmentDetailVO> detail(@PathVariable Long id){
        AdminAppointmentDetailVO detail = adminAppointmentService.detail(id);
        return Result.success(detail);
    }
}
