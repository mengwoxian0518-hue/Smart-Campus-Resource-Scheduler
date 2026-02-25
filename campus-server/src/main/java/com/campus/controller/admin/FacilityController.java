package com.campus.controller.admin;

import com.campus.Result.PageResult;
import com.campus.Result.Result;
import com.campus.annotation.Log;
import com.campus.dto.FacilityPageQueryDto;
import com.campus.entity.Facility;
import com.campus.service.FacilityService;
import com.campus.vo.FacilityVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/admin/facility")
@Api(tags = "场所管理")
public class FacilityController {
    @Autowired
    FacilityService facilityService;
    @ApiOperation("查询所有场所")
    @GetMapping("/list")
    public Result<List<Facility>> list() {
        List<Facility> list = facilityService.list();
        return Result.success(list);
    }
    @ApiOperation("分页查询所有场所")
    @GetMapping("/page")
    public Result<PageResult<FacilityVO>> page(FacilityPageQueryDto  page) {
        PageResult<FacilityVO> pageResult = facilityService.page(page);
        return Result.success(pageResult);
    }
    @ApiOperation("查询指定场所")
    @GetMapping("/{id}")
    public Result<Facility> getById(@PathVariable Long id){
        Facility facility = facilityService.getById(id);
        return Result.success(facility);
    }
    @ApiOperation("增加场所")
    @PostMapping
    @Log(module = "场所管理", action = "增加场所")
    public Result add(@RequestBody Facility facility){
        facilityService.add(facility);
        return Result.success();
    }
    @ApiOperation("修改场所")
    @PutMapping
    @Log(module = "场所管理", action = "修改场所")
    public Result update(@RequestBody Facility facility){
        facilityService.update(facility);
        return Result.success();
    }
    @ApiOperation("删除场所")
    @DeleteMapping
    @Log(module = "场所管理", action = "删除场所")
    public Result delete(@RequestParam List<Long> ids){
        facilityService.delete(ids);
        return Result.success();
    }
    @ApiOperation("修改场所状态")
    @PostMapping("/status/{status}")
    @Log(module = "场所管理", action = "修改场所状态")
    public Result startOrStop(@PathVariable Integer status,Long id){
        facilityService.startOrStop(status,id);
        return Result.success();
    }
}
