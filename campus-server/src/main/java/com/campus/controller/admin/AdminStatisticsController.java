package com.campus.controller.admin;

import com.campus.Result.Result;
import com.campus.service.AdminStatisticsService;
import com.campus.vo.StatisticsOverviewVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/admin/statistics")
public class AdminStatisticsController {
    @Autowired
    AdminStatisticsService adminStatisticsService;
    @GetMapping("/overview")
    public Result<StatisticsOverviewVO> overview(String days){
        StatisticsOverviewVO overview = adminStatisticsService.overview(days);
        return Result.success(overview);
    }
}
