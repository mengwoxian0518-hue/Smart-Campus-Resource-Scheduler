package com.campus.service.impl;

import com.campus.mapper.CommonMapper;
import com.campus.service.CommonService;
import com.campus.service.ResourceService;
import com.campus.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@Slf4j
public class CommonServiceImpl implements CommonService {
    @Autowired
    CommonMapper commonMapper;
    @Autowired
    AliOssUtil aliOssUtil;
    @Override
    public String upload(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String substring = originalFilename.substring(originalFilename.lastIndexOf("."));
        String uuid= UUID.randomUUID().toString();
        try {
            String upload = aliOssUtil.upload(file.getBytes(), uuid + substring);
            return upload;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
