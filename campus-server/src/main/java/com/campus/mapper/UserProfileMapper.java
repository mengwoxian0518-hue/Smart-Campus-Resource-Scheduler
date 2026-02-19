package com.campus.mapper;

import com.campus.entity.User;
import com.campus.entity.UserDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserProfileMapper {
    @Select("select * from user where id=#{id}")
    User getProfile(Long id);
    void updateProfile(User user);
    @Select("select * from user_detail where user_id= #{currentId}")
    UserDetail getDetail(Long currentId);
}
