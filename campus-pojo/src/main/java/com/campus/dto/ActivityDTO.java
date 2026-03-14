package com.campus.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ApiModel(description = "活动新增/修改DTO")
public class ActivityDTO implements Serializable {

    @ApiModelProperty("活动ID（修改时必填）")
    private Long id;

    @ApiModelProperty("活动名称")
    private String name;

    @ApiModelProperty("活动地点(关联facility表)")
    private Long facilityId;

    @ApiModelProperty("活动开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @ApiModelProperty("活动结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    @ApiModelProperty("报名开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime signupStartTime;

    @ApiModelProperty("报名截止时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime signupEndTime;

    @ApiModelProperty("最大报名人数")
    private Integer maxParticipants;

    @ApiModelProperty("活动详细内容")
    private String description;

    @ApiModelProperty("状态 0-已关闭, 1-报名中, 2-已满额, 3-已结束")
    private Integer status;
}
