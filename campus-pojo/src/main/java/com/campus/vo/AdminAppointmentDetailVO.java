package com.campus.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminAppointmentDetailVO implements Serializable {

    // 1. 预约基础信息
    private Long id;              // 预约单ID
    private Integer status;       // 状态: 0待审核, 1成功, 2驳回, 3取消
    private String type;          // 类型: RESOURCE (物资), VENUE (场地)

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime; // 申请提交时间

    // 2. 预约的具体时段
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate appointDate; // 预约日期

    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime startTime;   // 开始时间

    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime endTime;     // 结束时间

    // 3. 申请原因与审批反馈
    private String reason;         // 学生填写的申请事由
    private String rejectReason;   // 管理员填写的驳回原因

    // 4. 申请人详细信息 (关联 user 表)
    private String userName;       // 申请人姓名/昵称
    private String studentId;      // 学号/工号

    // 5. 租借物品/场地详细信息
    private String resourceName;   // 物资或场地名称
    private String image;          // 物品图片
    private String location;       // 具体位置
}