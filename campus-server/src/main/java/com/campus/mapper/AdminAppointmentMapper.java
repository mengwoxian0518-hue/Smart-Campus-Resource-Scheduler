package com.campus.mapper;

import com.campus.dto.AllTypePageQueryDTO;
import com.campus.dto.AppointmentAuditDTO;
import com.campus.entity.Appointment;
import com.campus.vo.AdminAppointmentDetailVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface AdminAppointmentMapper {
    List<Appointment> page(AllTypePageQueryDTO allTypePageQueryDTO);
    @Update("update appointment set status=#{status},reject_reason=#{rejectReason} where id=#{id}")
    void audit(AppointmentAuditDTO appointmentAuditDTO);
    AdminAppointmentDetailVO detail(Long id);
    @Select("select count(*) from appointment where status=0")
    Integer getCountPending();
}
