package com.campus.service;

import com.campus.Result.PageResult;
import com.campus.Result.Result;
import com.campus.dto.AllTypePageQueryDTO;
import com.campus.dto.AppointmentAuditDTO;
import com.campus.entity.Appointment;
import com.campus.vo.AdminAppointmentDetailVO;

public interface AdminAppointmentService {
   PageResult<Appointment> page(AllTypePageQueryDTO allTypePageQueryDTO);

    void audit(AppointmentAuditDTO appointmentAuditDTO);

    AdminAppointmentDetailVO detail(Long id);
}
