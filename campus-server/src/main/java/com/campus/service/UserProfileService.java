package com.campus.service;

import com.campus.entity.User;

public interface UserProfileService {
    User getProfile();

    User updateProfile(User user);
}
