package com.campus.controller.admin;

import com.campus.Result.Result;
import com.campus.annotation.Log;
import com.campus.service.CommonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@RequestMapping("/admin/common")
@Api(tags = "通用功能")
public class CommonController {
    @Autowired
    CommonService commonService;
    @ApiOperation("上传图片")
    @PostMapping("/upload")
    @Log(module = "通用功能", action = "上传图片")
    public Result<String> upload(MultipartFile file){
        String upload = commonService.upload(file);
        return Result.success(upload);
    }
}
