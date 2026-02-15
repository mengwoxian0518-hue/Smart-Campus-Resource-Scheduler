package com.campus.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FacilityPageQueryDto {
    private int page=1;
    private int pageSize=10;
    private String name;
    private Long categoryId;
    private Integer status;
}
