package com.campus.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    private Long id;
    private String openid;
    private String nickname;
    private String avatarUrl;
    private String studentId;
    private String realName;
    private Integer gender;
    private String college;
    private String major;
    private String className;
    private String phone;
    private String role;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private LocalDateTime lastLoginTime;
    private Integer creditScore;
    private Integer creditStatus; // 0:良好 1:较差
}