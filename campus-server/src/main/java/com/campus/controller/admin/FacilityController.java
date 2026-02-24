package com.campus.controller.admin;

import com.campus.Result.PageResult;
import com.campus.Result.Result;
import com.campus.annotation.Log;
import com.campus.dto.FacilityPageQueryDto;
import com.campus.entity.Facility;
import com.campus.service.FacilityService;
import com.campus.vo.FacilityVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/admin/facility")
public class FacilityController {
    @Autowired
    FacilityService facilityService;
    @GetMapping("/list")
    public Result<List<Facility>> list() {
        List<Facility> list = facilityService.list();
        return Result.success(list);
    }
    @GetMapping("/page")
    public Result<PageResult<FacilityVO>> page(FacilityPageQueryDto  page) {
        PageResult<FacilityVO> pageResult = facilityService.page(page);
        return Result.success(pageResult);
    }
    @GetMapping("/{id}")
    public Result<Facility> getById(@PathVariable Long id){
        Facility facility = facilityService.getById(id);
        return Result.success(facility);
    }
    @PostMapping
    @Log(module = "场所管理", action = "增加场所")
    public Result add(@RequestBody Facility facility){
        facilityService.add(facility);
        return Result.success();
    }
    @PutMapping
    @Log(module = "场所管理", action = "修改场所")
    public Result update(@RequestBody Facility facility){
        facilityService.update(facility);
        return Result.success();
    }
    @DeleteMapping
    @Log(module = "场所管理", action = "删除场所")
    public Result delete(@RequestParam List<Long> ids){
        facilityService.delete(ids);
        return Result.success();
    }
    @PostMapping("/status/{status}")
    @Log(module = "场所管理", action = "修改场所状态")
    public Result startOrStop(@PathVariable Integer status,Long id){
        facilityService.startOrStop(status,id);
        return Result.success();
    }
}
