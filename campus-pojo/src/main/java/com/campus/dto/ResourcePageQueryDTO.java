package com.campus.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel(description = "资源分页查询模型")
public class ResourcePageQueryDTO implements Serializable {
    @ApiModelProperty("资源名称")
    private String name;

    @ApiModelProperty("资源类别ID")
    private Long categoryId;

    @ApiModelProperty("所属场所ID")
    private Long facilityId;

    @ApiModelProperty("状态 0:维护 1:可借")
    private Integer status;

    @ApiModelProperty("最小信用分")
    private BigDecimal minPrice;

    @ApiModelProperty("最大信用分")
    private BigDecimal maxPrice;

    private int page;
    private int pageSize;
}