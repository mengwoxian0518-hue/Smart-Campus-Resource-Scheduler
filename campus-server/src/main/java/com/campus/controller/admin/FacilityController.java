package com.campus.controller.admin;

import com.campus.Result.Result;
import com.campus.entity.Facility;
import com.campus.service.FacilityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
