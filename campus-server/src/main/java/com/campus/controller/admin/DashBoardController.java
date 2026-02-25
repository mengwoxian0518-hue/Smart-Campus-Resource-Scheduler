package com.campus.controller.admin;

import com.campus.Result.Result;
import com.campus.service.DashBoardService;
import com.campus.vo.LogVO;
import com.campus.vo.OverViewVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.nullability.AlwaysNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/dashboard")
@Slf4j
@Api(tags = "主页")
public class DashBoardController {
    @Autowired
    DashBoardService dashBoardService;
    @ApiOperation("获取概览信息")
    @GetMapping("/overview")
    public Result<OverViewVO> overview() {
        OverViewVO overview = dashBoardService.overview();
        return Result.success(overview);
    }
    @ApiOperation("获取日志信息")
    @GetMapping("/logs")
    public Result<List<LogVO>> logs() {
        List<LogVO> logs = dashBoardService.logs();
        return Result.success(logs);
    }
}
