package com.campus.controller.admin;

import com.campus.Result.PageResult;
import com.campus.Result.Result;
import com.campus.dto.AllTypePageQueryDTO;
import com.campus.dto.AppointmentAuditDTO;
import com.campus.entity.Appointment;
import com.campus.service.AdminAppointmentService;
import com.campus.vo.AdminAppointmentDetailVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/admin/appointment")
public class AdminAppointmentController {
    @Autowired
    AdminAppointmentService adminAppointmentService;
    @GetMapping("/page")
    public Result<PageResult<Appointment>> page(AllTypePageQueryDTO allTypePageQueryDTO){
        log.info("进入查询");
        PageResult<Appointment> page = adminAppointmentService.page(allTypePageQueryDTO);
        return Result.success(page);
    }
    @PutMapping("/audit")
    public Result audit(@RequestBody AppointmentAuditDTO appointmentAuditDTO){
        adminAppointmentService.audit(appointmentAuditDTO);
        return Result.success();
    }
    @GetMapping("/detail/{id}")
    public Result<AdminAppointmentDetailVO> detail(@PathVariable Long id){
        AdminAppointmentDetailVO detail = adminAppointmentService.detail(id);
        return Result.success(detail);
    }
}
