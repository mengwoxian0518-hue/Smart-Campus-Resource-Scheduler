package com.campus.controller.admin;

import com.campus.Result.Result;
import com.campus.service.DashBoardService;
import com.campus.vo.LogVO;
import com.campus.vo.OverViewVO;
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
public class DashBoardController {
    @Autowired
    DashBoardService dashBoardService;
    @GetMapping("/overview")
    public Result<OverViewVO> overview() {
        OverViewVO overview = dashBoardService.overview();
        return Result.success(overview);
    }
    @GetMapping("/logs")
    public Result<List<LogVO>> logs() {
        List<LogVO> logs = dashBoardService.logs();
        return Result.success(logs);
    }
}
