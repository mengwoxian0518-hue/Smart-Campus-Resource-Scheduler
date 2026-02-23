package com.campus.dto;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class CreditChangeDTO implements Serializable {

    /**
     * 学生ID (User表的主键)
     */
    private Long userId;

    /**
     * 操作类型
     * "ADD": 奖励加分
     * "DEDUCT": 违规扣分
     */
    private String action;

    /**
     * 变动分值 (必须大于0)
     */
    private Integer points;

    /**
     * 变动原因 (必填，用于记录日志)
     */
    private String reason;
    private Integer type;
    private LocalDateTime createTime;
}