package com.campus.controller.admin;

import com.campus.Result.Result;
import com.campus.service.AdminStatisticsService;
import com.campus.vo.StatisticsOverviewVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/admin/statistics")
@Api(tags = "统计管理")
public class AdminStatisticsController {
    @Autowired
    AdminStatisticsService adminStatisticsService;
    @ApiOperation(value = "统计总览")
    @GetMapping("/overview")
    public Result<StatisticsOverviewVO> overview(String days){
        StatisticsOverviewVO overview = adminStatisticsService.overview(days);
        return Result.success(overview);
    }
}
