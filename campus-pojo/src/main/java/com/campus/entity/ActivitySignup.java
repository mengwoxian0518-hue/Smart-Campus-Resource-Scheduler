package com.campus.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 活动报名实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivitySignup implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long activityId;
    private Long userId;
    private LocalDateTime signupTime;
}
