package com.campus.mapper;

import com.campus.Type.OperationType;
import com.campus.annotation.AutoFill;
import com.campus.dto.AdminLoginDto;
import com.campus.dto.StaffPageListDto;
import com.campus.entity.Admin;
import com.campus.entity.Staff;
import org.apache.ibatis.annotations.*;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;

@Mapper
public interface AdminMapper {
    @Select("select * from admin where username = #{username} and password = #{password}")
    Admin login(AdminLoginDto adminLoginDto);
//    @Select("select * from staff where name like '%${name}%' and job_title=#{jobTitle} and hire_date between #{beginTime} and #{endTime} order by create_time desc")
    List<Staff> listStaff(StaffPageListDto staffPageListDto);
    @AutoFill(value = OperationType.INSERT)
    @Insert("insert into staff(name,sex,job_title,hire_date,create_time,phone,username,update_time) values(#{name},#{sex},#{jobTitle},#{hireDate},#{createTime},#{phone},#{username},#{updateTime})")
    void addStaff(Staff staff);
    @Delete("delete from staff where id=#{id}")
    void deleteById(Long id);
    @Select("select * from staff where id=#{id}")
    Staff getStaffById(Long id);
    @AutoFill(OperationType.UPDATE)
    @Update("update staff set name=#{name},sex=#{sex},job_title=#{jobTitle},hire_date=#{hireDate},phone=#{phone},username=#{username},update_time=#{updateTime} where id=#{id}")
    void update(Staff staff);
    @Update("update staff set status= #{status} where id= #{id}")
    void startOrStop(Integer status, Long id);
}
