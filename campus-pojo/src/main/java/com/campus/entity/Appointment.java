package com.campus.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {
    private Long id;
    private Long userId;
    private Long resourceId;
    private String resourceName;
    private String resourceImage;
    private String type;
    //localdate格式化json
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private LocalDate appointDate;
    @JsonFormat(pattern = "HH:mm:ss",timezone = "GMT+8")
    private LocalTime startTime;
    @JsonFormat(pattern = "HH:mm:ss",timezone = "GMT+8")
    private LocalTime endTime;
    private String reason;
    private Integer status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime updateTime;
}