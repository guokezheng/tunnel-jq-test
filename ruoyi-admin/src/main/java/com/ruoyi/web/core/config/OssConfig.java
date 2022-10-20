package com.ruoyi.web.core.config;

import com.ruoyi.common.oss.controller.OssUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OssConfig {
    @Value("${ruoyi.oss.endPoint}")
    private String endPoint;

    @Value("${ruoyi.oss.accessKeyId}")
    private String accessKeyId;

    @Value("${ruoyi.oss.accessKeySecret}")
    private String accessKeySecret;

    @Value("${ruoyi.oss.bucketName}")
    private String bucketName;

    @Value("${ruoyi.oss.upFolder}")
    private String upFolder;

    @Value("${ruoyi.uploadType}")
    private String uploadType;


    @Bean
    public void initOssBootConfiguration() {
        OssUtil.setEndPoint(endPoint);
        OssUtil.setAccessKeyId(accessKeyId);
        OssUtil.setAccessKeySecret(accessKeySecret);
        OssUtil.setBucketName(bucketName);
        OssUtil.setUpFolder(upFolder);
        OssUtil.setUploadType(uploadType);
    }
}
