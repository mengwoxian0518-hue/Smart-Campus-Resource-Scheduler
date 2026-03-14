package com.campus.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(description = "活动分页查询DTO")
public class ActivityPageQueryDTO implements Serializable {
    @ApiModelProperty("页码")
    private int page = 1;

    @ApiModelProperty("每页大小")
    private int pageSize = 10;

    @ApiModelProperty("活动名称（模糊查询）")
    private String name;

    @ApiModelProperty("状态 0-已关闭, 1-报名中, 2-已满额, 3-已结束")
    private Integer status;
}
