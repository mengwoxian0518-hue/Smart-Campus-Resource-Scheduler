package com.campus.mapper;

import com.campus.entity.Appointment;
import com.campus.vo.OverViewVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Mapper
public interface DashBoardMapper {
    Integer overviewPending();
    Integer overviewTotalResources();
    Integer overviewInUse(LocalDate date, LocalTime now);
    Integer overviewUnderRepair();
    @Select("select * from appointment order by update_time desc limit 10")
    List<Appointment> getAppointment();
    @Select("select real_name from user where id=#{userId}")
    String getUserName(Long userId);
    String getResourceName(Long resourceId,String type);
}
