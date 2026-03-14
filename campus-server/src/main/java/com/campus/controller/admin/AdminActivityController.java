package com.campus.controller.admin;

import com.campus.Result.PageResult;
import com.campus.Result.Result;
import com.campus.dto.ActivityDTO;
import com.campus.dto.ActivityPageQueryDTO;
import com.campus.service.AdminActivityService;
import com.campus.vo.ActivityVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/activity")
@Slf4j
@Api(tags = "活动管理接口")
public class AdminActivityController {

    @Autowired
    private AdminActivityService adminActivityService;

    @GetMapping("/page")
    @ApiOperation("活动分页查询")
    public Result<PageResult> page(ActivityPageQueryDTO dto) {
        PageResult pageResult = adminActivityService.pageQuery(dto);
        return Result.success(pageResult);
    }

    @PostMapping
    @ApiOperation("新增活动")
    public Result save(@RequestBody ActivityDTO dto) {
        adminActivityService.add(dto);
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("根据ID查询活动")
    public Result<ActivityVO> getById(@PathVariable Long id) {
        ActivityVO vo = adminActivityService.getById(id);
        return Result.success(vo);
    }

    @PutMapping
    @ApiOperation("修改活动")
    public Result update(@RequestBody ActivityDTO dto) {
        adminActivityService.update(dto);
        return Result.success();
    }

    @DeleteMapping
    @ApiOperation("删除活动")
    public Result delete(Long id) {
        adminActivityService.delete(id);
        return Result.success();
    }
}
