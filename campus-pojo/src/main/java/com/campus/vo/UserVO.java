package com.campus.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserVO implements Serializable {

    // 基础信息
    private Long id;              // 用户主键ID
    private String realName;      // 真实姓名
    private String avatarUrl;     // 头像链接 (已修改为 avatarUrl)
    private String studentId;     // 学号
    private String phone;         // 手机号

    // 院系班级信息
    private String college;       // 学院
    private String major;         // 专业
    private String className;     // 班级

    // 信用信息 (来自 user_detail 表)
    private Integer creditScore;  // 当前信用分
    
}