package com.campus.controller.user;

import com.campus.Result.Result;
import com.campus.dto.AppointmentDTO;
import com.campus.entity.Appointment;
import com.campus.service.UserAppointmentService;
import com.campus.vo.TimeSlotVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/user/appointment")
@Api(tags = "用户预约接口")
public class UserAppointmentController {
    @Autowired
    UserAppointmentService userAppointmentService;
    @ApiOperation("获取预约时间段")
    @GetMapping("/availability")
    public Result<List<TimeSlotVO>> getAvailability(Long resourceId, @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate date, String type)
    {
        List<TimeSlotVO> timeSlotVO = userAppointmentService.getAvailability(resourceId,date,type);
        return Result.success(timeSlotVO);
    }
    @ApiOperation("提交预约")
    @PostMapping("submit")
    public Result submitAppointment(@RequestBody AppointmentDTO dto)
    {
        userAppointmentService.submitAppointment(dto);
        return Result.success();
    }
    @ApiOperation("获取我的预约")
    @GetMapping("my")
    public Result<List<Appointment>> list(String type,Integer status)
    {
        List<Appointment> list=userAppointmentService.list(type,status);
        return Result.success(list);
    }
    @ApiOperation("取消预约")
    @PostMapping("cancel")
    public Result cancel(@RequestBody Appointment dto)
        {
            userAppointmentService.cancel(dto);
            return Result.success();
        }
}
