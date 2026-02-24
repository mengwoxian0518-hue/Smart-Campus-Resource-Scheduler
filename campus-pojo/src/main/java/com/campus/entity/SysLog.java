package com.campus.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 系统操作日志实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysLog implements Serializable {

    private Long id;

    private Long operatorId;

    private String module;

    private String action;

    private String methodName;

    private String params;

    private String ip;

    private Long executionTime;

    private LocalDateTime createTime; // 操作时间
}