package com.campus.controller.user;

import com.campus.Result.PageResult;
import com.campus.Result.Result;
import com.campus.annotation.RateLimit;
import com.campus.dto.ActivityPageQueryDTO;
import com.campus.service.UserActivityService;
import com.campus.vo.ActivityVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/activity")
@Slf4j
@Api(tags = "C端活动接口")
public class UserActivityController {

    @Autowired
    private UserActivityService userActivityService;

    @GetMapping("/page")
    @ApiOperation("活动列表查询")
    @RateLimit(key = "activity_page", time = 1, count = 10)
    public Result<PageResult> page(ActivityPageQueryDTO dto) {
        PageResult pageResult = userActivityService.pageQuery(dto);
        return Result.success(pageResult);
    }

    @GetMapping("/{id}")
    @ApiOperation("活动详情查询")
    @RateLimit(key = "activity_detail", time = 1, count = 20)
    public Result<ActivityVO> getById(@PathVariable Long id) {
        ActivityVO vo = userActivityService.getById(id);
        return Result.success(vo);
    }

    @PostMapping("/signup/{id}")
    @ApiOperation("活动报名")
    @RateLimit(key = "activity_signup", time = 1, count = 1)
    public Result signup(@PathVariable Long id) {
        userActivityService.signup(id);
        return Result.success();
    }

    @GetMapping("/my")
    @ApiOperation("我的活动")
    public Result<List<ActivityVO>> myActivities() {
        return Result.success(userActivityService.myActivities());
    }
}
