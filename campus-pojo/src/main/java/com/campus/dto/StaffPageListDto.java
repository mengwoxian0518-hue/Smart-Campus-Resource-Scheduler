package com.campus.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.io.Serializable;
import java.time.LocalDate;

@Data
public class StaffPageListDto implements Serializable {
    private String name;        // 姓名（模糊查询）
    private String jobTitle;    // 职位/职责

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate beginTime; // 入职开始时间

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endTime;   // 入职结束时间

    private int page=1;           // 页码
    private int pageSize=10;       // 每页条数
}