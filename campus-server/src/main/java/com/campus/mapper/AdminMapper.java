package com.campus.mapper;

import com.campus.dto.AdminLoginDto;
import com.campus.dto.StaffPageListDto;
import com.campus.entity.Admin;
import com.campus.entity.Staff;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AdminMapper {
    @Select("select * from admin where username = #{username} and password = #{password}")
    Admin login(AdminLoginDto adminLoginDto);
//    @Select("select * from staff where name like '%${name}%' and job_title=#{jobTitle} and hire_date between #{beginTime} and #{endTime} order by create_time desc")
    List<Staff> listStaff(StaffPageListDto staffPageListDto);
}
