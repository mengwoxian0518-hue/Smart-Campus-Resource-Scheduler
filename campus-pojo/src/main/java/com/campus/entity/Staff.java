package com.campus.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Staff implements Serializable {
    private Long id;
    private String username;    // 工号
    private String name;        // 姓名
    private String password;
    private String phone;
    private String sex;
    private String avatar;      // 头像
    private String idNumber;    // 身份证
    private String jobTitle;    // 职责/职位 (如: 实验室管理员)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate hireDate; // 入职日期
    private Integer status;     // 1:启用 0:禁用
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    private Long createUser;
    private Long updateUser;
}