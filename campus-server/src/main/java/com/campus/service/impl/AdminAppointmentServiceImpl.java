package com.campus.service.impl;

import com.campus.Result.PageResult;
import com.campus.Result.Result;
import com.campus.dto.AllTypePageQueryDTO;
import com.campus.dto.AppointmentAuditDTO;
import com.campus.entity.Appointment;
import com.campus.mapper.AdminAppointmentMapper;
import com.campus.mapper.UserQueryMapper;
import com.campus.service.AdminAppointmentService;
import com.campus.vo.AdminAppointmentDetailVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AdminAppointmentServiceImpl implements AdminAppointmentService {
    @Autowired
    AdminAppointmentMapper adminAppointmentMapper;
    @Override
    public PageResult<Appointment> page(AllTypePageQueryDTO allTypePageQueryDTO) {
        PageHelper.startPage(allTypePageQueryDTO.getPage(), allTypePageQueryDTO.getPageSize());
        Page<Appointment> page =(Page<Appointment>) adminAppointmentMapper.page(allTypePageQueryDTO);
        return new PageResult<>(page.getTotal(),page.getResult());

    }

    @Override
    public void audit(AppointmentAuditDTO appointmentAuditDTO) {
        adminAppointmentMapper.audit(appointmentAuditDTO);
    }

    @Override
    public AdminAppointmentDetailVO detail(Long id) {
        AdminAppointmentDetailVO detail = adminAppointmentMapper.detail(id);
        return detail;
    }
}
