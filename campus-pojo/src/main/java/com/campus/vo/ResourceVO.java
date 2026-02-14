package com.campus.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class ResourceVO implements Serializable {
    private Long id;
    private String name;
    private Long categoryId;
    private String categoryName; // 🚀 冗余字段：显示类别名称
    private Long facilityId;
    private String facilityName; // 🚀 冗余字段：显示场所名称
    private BigDecimal creditCost;
    private String image;
    private String model;
    private String assetCode;
    private String description;
    private Integer status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}