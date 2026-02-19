package com.campus.service.impl;

import com.campus.context.BaseContext;
import com.campus.entity.User;
import com.campus.entity.UserDetail;
import com.campus.mapper.UserProfileMapper;
import com.campus.service.UserProfileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserProfileServiceImpl implements UserProfileService {
    @Autowired
    UserProfileMapper userProfileMapper;
    @Override
    public User getProfile() {
        User profile = userProfileMapper.getProfile(BaseContext.getCurrentId());
        UserDetail detail = userProfileMapper.getDetail(BaseContext.getCurrentId());
        profile.setCreditScore(detail.getCreditScore());
        profile.setCreditStatus(detail.getCreditStatus());
        return profile;
    }

    @Override
    public User updateProfile(User user) {
        user.setId(BaseContext.getCurrentId());
        userProfileMapper.updateProfile(user);
        User profile = userProfileMapper.getProfile(BaseContext.getCurrentId());
        UserDetail detail = userProfileMapper.getDetail(BaseContext.getCurrentId());
        profile.setCreditScore(detail.getCreditScore());
        profile.setCreditStatus(detail.getCreditStatus());
        return profile;
    }
}
