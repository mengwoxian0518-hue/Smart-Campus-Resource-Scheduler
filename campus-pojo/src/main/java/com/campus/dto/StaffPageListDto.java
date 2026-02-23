package com.campus.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.io.Serializable;
import java.time.LocalDate;

@Data
public class StaffPageListDto implements Serializable {
    private String name;
    private String jobTitle;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate beginTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endTime;

    private int page=1;
    private int pageSize=10;
}