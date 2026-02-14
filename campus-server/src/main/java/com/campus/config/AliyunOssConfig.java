package com.campus.config;

import com.campus.properties.AliOssProperties;
import com.campus.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class AliyunOssConfig {
    @Bean
    public AliOssUtil aliOssUtil(AliOssProperties aliOssProperties) {
        AliOssUtil aliOssUtil = new AliOssUtil(null,null,null,null);
        aliOssUtil.setAccessKeyId(aliOssProperties.getAccessKeyId());
        aliOssUtil.setAccessKeySecret(aliOssProperties.getAccessKeySecret());
        aliOssUtil.setBucketName(aliOssProperties.getBucketName());
        aliOssUtil.setEndpoint(aliOssProperties.getEndpoint());
        return aliOssUtil;
    }
}
