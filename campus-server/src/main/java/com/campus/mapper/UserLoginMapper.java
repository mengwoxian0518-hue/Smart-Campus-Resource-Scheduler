package com.campus.mapper;

import com.campus.entity.User;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;

@Mapper
public interface UserLoginMapper {
    @Select("select * from user where openid=#{openid}")
    User searchByOpenid(String openid);
    @Insert("insert into user (openid,create_time) values (#{openid},#{createTime})")
    @Options(useGeneratedKeys = true,keyProperty = "id")
    void insertUser(User user);
    @Update("update user set last_login_time=#{lastLoginTime} where id=#{id}")
    void uplateLoginTime(Long id,LocalDateTime lastLoginTime);
    @Insert("insert into user_detail (id) values (#{id})")
    void insertUserDetail(Long id);
}
