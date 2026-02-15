package com.campus.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FacilityVO {
    private Long id;
    private Long capacity;
    private String name;
    private String location;
    private String categoryName;
    private Integer status;
    private BigDecimal price;
}
