package com.campus.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimeSlotVO {
    private Integer index;       // 排序索引
    private String timeDisplay;  // 显示时间 (如 08:00)
    private LocalTime startTime; // 完整开始时间
    private LocalTime endTime;   // 完整结束时间
    private String status;       // FREE, BUSY, EXPIRED
    private String tips;         // 提示文字
}