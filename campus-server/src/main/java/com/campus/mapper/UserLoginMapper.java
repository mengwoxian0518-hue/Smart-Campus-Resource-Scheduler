package com.campus.mapper;

import com.campus.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;

@Mapper
public interface UserLoginMapper {
    @Select("select * from user where openid=#{openid}")
    User searchByOpenid(String openid);
    @Insert("insert into user (openid,create_time) values (#{openid},#{createTime})")
    @Options(useGeneratedKeys = true,keyProperty = "id")
    void insertUser(User user);
}
