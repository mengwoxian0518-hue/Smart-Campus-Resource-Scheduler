package com.campus.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDetail {
    private Long id;
    private Long userId;
    private Integer creditScore;
    private Integer creditStatus; // 0:良好 1:较差
    private Integer violationCount;
    private Integer totalRentCount;
    private LocalDateTime updateTime;
}