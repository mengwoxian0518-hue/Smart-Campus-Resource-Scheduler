package com.campus.service.impl;

import com.alibaba.druid.util.HttpClientUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.campus.entity.User;
import com.campus.mapper.UserLoginMapper;
import com.campus.properties.JwtProperties;
import com.campus.properties.WxProperties;
import com.campus.service.UserLoginService;
import com.campus.utils.HttpClientUtil;
import com.campus.utils.JwtUtil;
import com.campus.vo.UserLoginVO;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class UserLoginServiceImpl implements UserLoginService {
    @Autowired
    UserLoginMapper userLoginMapper;
    @Autowired
    JwtProperties jwtProperties;
    @Autowired
    WxProperties wxProperties;
    @Override
    public UserLoginVO Login(String code) {
        Boolean isNewUser=false;
        Map<String,String> m=new HashMap<>();
        m.put("appid",wxProperties.getAppid());
        m.put("secret",wxProperties.getSecret());
        m.put("js_code",code);
        m.put("grant_type","authorization_code");
        String s = HttpClientUtil.doGet("https://api.weixin.qq.com/sns/jscode2session?", m);
        JSONObject jsonObject = JSON.parseObject(s);
        String o =(String)jsonObject.get("openid");
        if(o==null)
        {
            throw new RuntimeException("登录失败");
        }
        User user = userLoginMapper.searchByOpenid(o);
        if(user==null)
        {
            user = User.builder().openid(o).createTime(LocalDateTime.now()).lastLoginTime(LocalDateTime.now()).build();
            userLoginMapper.insertUser(user);
            isNewUser=true;
            userLoginMapper.insertUserDetail(user.getId());
        }
        userLoginMapper.uplateLoginTime(user.getId(),LocalDateTime.now());
        Map<String,Object> claims=new HashMap<>();
        claims.put("id",user.getId());
        String jwt = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), claims);
        return UserLoginVO.builder().token(jwt).openid(o).isNewUser(isNewUser).build();
    }
}
