package com.campus.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 管理员及教职工实体类
 * 对应数据库中的 admin 表
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Admin implements Serializable {

    private static final long serialVersionUID = 1L;
    private long id;
    // 用户名/工号
    private String username;
    // 真实姓名
    private String name;
    // 密码（存储MD5加密后的密文）
    private String password;
    // 手机号
    private String phone;
    // 性别 1:男 0:女
    private String sex;
    // 身份证号
    private String idNumber;
    // 账号状态 1:启用 0:禁用
    private Integer status;
    // 创建时间
    private LocalDateTime createTime;
    // 更新时间
    private LocalDateTime updateTime;
    // 创建人ID
    private Long createUser;
    // 修改人ID
    private Long updateUser;

}