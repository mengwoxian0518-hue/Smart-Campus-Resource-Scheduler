package com.campus.mapper;

import com.campus.dto.AdminLoginDto;
import com.campus.entity.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AdminMapper {
    @Select("select * from admin where username = #{username} and password = #{password}")
    Admin login(AdminLoginDto adminLoginDto);
}
