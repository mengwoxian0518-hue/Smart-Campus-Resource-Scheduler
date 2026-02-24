package com.campus.controller.admin;

import com.campus.Result.PageResult;
import com.campus.Result.Result;
import com.campus.annotation.Log;
import com.campus.dto.ResourcePageQueryDTO;
import com.campus.entity.Resource;
import com.campus.service.ResourceService;
import com.campus.vo.ResourceVO;
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
public class ResourceController {
    @Autowired
    ResourceService resourceService;
    @GetMapping("/page")
    public Result<PageResult<ResourceVO>> resourcePageList(ResourcePageQueryDTO resourcePageQueryDTO) {
        if(Objects.equals(resourcePageQueryDTO.getMaxPrice(), new BigDecimal(0))){
            resourcePageQueryDTO.setMaxPrice(new BigDecimal(Integer.MAX_VALUE));
        }
        PageResult<ResourceVO> resourceVOPageResult = resourceService.pageList(resourcePageQueryDTO);
        return Result.success(resourceVOPageResult);
    }
    @GetMapping("/list")
    public Result<List<Resource>> list(Long facilityId) {
        List<Resource> list = resourceService.list(facilityId);
        return Result.success(list);
    }
    @PostMapping
    @Log(module = "资源管理", action = "增加资源")
    public Result add(@RequestBody Resource resource){
        resourceService.add(resource);
        return Result.success();
    }
    @PutMapping
    @Log(module = "资源管理", action = "修改资源")
    public Result update(@RequestBody Resource resource){
        resourceService.update(resource);
        return Result.success();
    }
    @DeleteMapping
    @Log(module = "资源管理", action = "删除资源")
    public Result delete(@RequestParam List<Long> ids){
        resourceService.delete(ids);
        return Result.success();
    }
    @GetMapping("/{id}")
    public Result<Resource> getById(@PathVariable Long id){
        Resource resource = resourceService.getById(id);
        return Result.success(resource);
    }
    @PostMapping("/status/{status}")
    @Log(module = "资源管理", action = "更改资源状态")
    public Result startOrStop(@PathVariable Integer status,Long id){
        resourceService.startOrStop(status,id);
        return Result.success();
    }
}
