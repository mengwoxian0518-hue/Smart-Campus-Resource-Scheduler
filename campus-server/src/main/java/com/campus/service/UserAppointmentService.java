package com.campus.service;

import com.campus.dto.AppointmentDTO;
import com.campus.entity.Appointment;
import com.campus.vo.TimeSlotVO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface UserAppointmentService {
    List<TimeSlotVO> getAvailability(Long resourceId, LocalDate date, String type);
    void submitAppointment(AppointmentDTO dto);

    List<Appointment> list(String type, Integer status);

    void cancel(Appointment dto);
}
