package com.campus.service;

import com.campus.vo.UserLoginVO;
import org.apache.ibatis.annotations.Mapper;


public interface UserLoginService {
    UserLoginVO Login(String code);
}
