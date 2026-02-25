package com.campus.controller.admin;

import com.campus.Result.PageResult;
import com.campus.Result.Result;
import com.campus.annotation.Log;
import com.campus.dto.ResourcePageQueryDTO;
import com.campus.entity.Resource;
import com.campus.service.ResourceService;
import com.campus.vo.ResourceVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@RestController
@Slf4j
@RequestMapping("/admin/resource")
@Api(tags = "资源管理")
public class ResourceController {
    @Autowired
    ResourceService resourceService;
    @ApiOperation(value = "分页查询资源")
    @GetMapping("/page")
    public Result<PageResult<ResourceVO>> resourcePageList(ResourcePageQueryDTO resourcePageQueryDTO) {
        PageResult<ResourceVO> resourceVOPageResult = resourceService.pageList(resourcePageQueryDTO);
        return Result.success(resourceVOPageResult);
    }
    @ApiOperation(value = "获取指定场所下的所有资源")
    @GetMapping("/list")
    public Result<List<Resource>> list(Long facilityId) {
        List<Resource> list = resourceService.list(facilityId);
        return Result.success(list);
    }
    @ApiOperation(value = "增加资源")
    @PostMapping
    @Log(module = "资源管理", action = "增加资源")
    public Result add(@RequestBody Resource resource){
        resourceService.add(resource);
        return Result.success();
    }
    @ApiOperation(value = "修改资源")
    @PutMapping
    @Log(module = "资源管理", action = "修改资源")
    public Result update(@RequestBody Resource resource){
        resourceService.update(resource);
        return Result.success();
    }
    @ApiOperation(value = "删除资源")
    @DeleteMapping
    @Log(module = "资源管理", action = "删除资源")
    public Result delete(@RequestParam List<Long> ids){
        resourceService.delete(ids);
        return Result.success();
    }
    @ApiOperation(value = "获取指定资源")
    @GetMapping("/{id}")
    public Result<Resource> getById(@PathVariable Long id){
        Resource resource = resourceService.getById(id);
        return Result.success(resource);
    }
    @ApiOperation(value = "更改资源状态")
    @PostMapping("/status/{status}")
    @Log(module = "资源管理", action = "更改资源状态")
    public Result startOrStop(@PathVariable Integer status,Long id){
        resourceService.startOrStop(status,id);
        return Result.success();
    }
}
