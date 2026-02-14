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
public class Facility implements Serializable {
    private Long id;
    private Long categoryId;    // 场所分类ID (如: 教学实验室)
    private String name;        // 场所名称 (如: 基础物理实验室)
    private BigDecimal price;   // 包场费用
    private Integer status;     // 1:开放 0:关闭
    private String location;    // 物理位置
    private Integer capacity;   // 容纳人数
    private String image;
    private String description;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Long createUser;
    private Long updateUser;
}