package com.campus.mapper;

import com.campus.entity.Appointment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Mapper
public interface UserAppointmentMapper {
    @Select("select * from appointment where resource_id=#{resourceId} and type=#{type} and appoint_date=#{date} and status in(0,1)")
    List<Appointment> findOccupied(Long resourceId, String type, LocalDate date);
    @Select("select count(*) from appointment where resource_id=#{resourceId} and appoint_date=#{appointDate} and start_time=#{startTime} and end_time=#{endTime} and status in(0,1)")
    Integer countConflict(Long resourceId, LocalDate appointDate, LocalTime startTime, LocalTime endTime);
    void insert(Appointment appointment);
    List<Appointment> listResource(Long currentId, Integer status);

    List<Appointment> listFacility(Long currentId, Integer status);
    @Update("update appointment set status = 3 where id = #{id}")
    void cancel(Appointment dto);
}
