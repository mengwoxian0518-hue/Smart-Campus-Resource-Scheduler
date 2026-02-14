package com.campus.controller.admin;

import com.campus.Result.PageResult;
import com.campus.Result.Result;
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
    @GetMapping("/list")
    public Result<PageResult<ResourceVO>> resourcePageList(ResourcePageQueryDTO resourcePageQueryDTO) {
        if(Objects.equals(resourcePageQueryDTO.getMaxPrice(), new BigDecimal(0))){
            resourcePageQueryDTO.setMaxPrice(new BigDecimal(Integer.MAX_VALUE));
        }
        PageResult<ResourceVO> resourceVOPageResult = resourceService.pageList(resourcePageQueryDTO);
        return Result.success(resourceVOPageResult);
    }
    @PostMapping
    public Result add(@RequestBody Resource resource){
        resourceService.add(resource);
        return Result.success();
    }
    @PutMapping
    public Result update(@RequestBody Resource resource){
        resourceService.update(resource);
        return Result.success();
    }
    @DeleteMapping
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
    public Result startOrStop(@PathVariable Integer status,Long id){
        resourceService.startOrStop(status,id);
        return Result.success();
    }
}
