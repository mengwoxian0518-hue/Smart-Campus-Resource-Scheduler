package com.campus.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Resource implements Serializable {
    private Long id;
    private String name;        // 资源名称
    private Long categoryId;    // 资源分类ID (如: 精密仪器)
    private Long facilityId;    // 🚀 所属场所ID (如: 理科楼101)
    private BigDecimal creditCost; // 每小时消耗信用分
    private String image;       // 图片URL
    private String model;       // 型号
    private String assetCode;   // 资产编号
    private String description; // 描述
    private Integer status;     // 1:可借 0:维护
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Long createUser;
    private Long updateUser;
}