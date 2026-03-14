package com.campus.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 活动实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Activity implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private Long facilityId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime signupStartTime;
    private LocalDateTime signupEndTime;
    private Integer maxParticipants;
    private Integer currentParticipants;
    private String description;
    // 0-已关闭, 1-报名中, 2-已满额, 3-已结束
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
