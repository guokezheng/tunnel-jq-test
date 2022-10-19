package com.ruoyi.common.oss.controller;

import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectResult;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.utils.file.FileUploadUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public class OssUtil {

    private static String endPoint;

    private static String bucketName;

    private static String accessKeyId;

    private static String accessKeySecret;

    private static String upFolder;

    private static String uploadType;

    public static String getEndPoint() {
        return endPoint;
    }

    public static String getBucketName() {
        return bucketName;
    }

    public static String getAccessKeyId() {
        return accessKeyId;
    }

    public static String getAccessKeySecret() {
        return accessKeySecret;
    }

    public static String getUpFolder() {
        return upFolder;
    }
    public static String getUploadType() {
        return uploadType;
    }

    public static void setEndPoint(String endPoint) {
        OssUtil.endPoint = endPoint;
    }

    public static void setBucketName(String bucketName) {
        OssUtil.bucketName = bucketName;
    }

    public static void setAccessKeyId(String accessKeyId) {
        OssUtil.accessKeyId = accessKeyId;
    }

    public static void setAccessKeySecret(String accessKeySecret) {
        OssUtil.accessKeySecret = accessKeySecret;
    }

    public static void setUpFolder(String upFolder) {
        OssUtil.upFolder = upFolder;
    }

    public static void setUploadType(String uploadType) {
        OssUtil.uploadType = uploadType;
    }

    private static OSS ossClient;

    private static OSS initOSS(String endPoint, String accessKeyId, String accessKeySecret) {
        if (null == ossClient) {
            ClientBuilderConfiguration conf = new ClientBuilderConfiguration();
            // 关闭CNAME选项
            conf.setSupportCname(false);
            ossClient = new OSSClientBuilder().build(endPoint, accessKeyId, accessKeySecret, conf);
        }
        return ossClient;
    }

    /**
     * 阿里OSS存储
     * @param multipartFile 上传的文件对象
     * @param fileDir       上传目录
     * @return
     */
    public static String ossUpload(MultipartFile multipartFile, String fileDir) {
        //初始化Client
        initOSS(endPoint, accessKeyId, accessKeySecret);

        //原始文件名
        String originalFilename = multipartFile.getOriginalFilename();
        if (StringUtils.isBlank(originalFilename)) {
            originalFilename = multipartFile.getName();
        }
        //避免重名
        String filename = originalFilename.substring(0, originalFilename.lastIndexOf(".")) + "_" + System.currentTimeMillis() + originalFilename.substring(originalFilename.indexOf("."));
        //上传目录
        if (StringUtils.isEmpty(fileDir)) {
            fileDir = upFolder;
        }
        String fileUrl = fileDir + "/" + filename;
        //完整路径
        String filePath = "https://" + bucketName + "." + endPoint + "/" + fileUrl;

        try {
            PutObjectResult putObjectResult = ossClient.putObject(bucketName, fileUrl, multipartFile.getInputStream());

            if (null != putObjectResult) {
                System.out.println("》》》》》OSS文件上传成功《《《《《" + fileUrl);
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return filePath;
    }

    /**
     * @param file       上传的文件
     * @param upFolder   上传到哪个分目录下,如果传值为空（null、空字符串）则以默认配置进行文件上传
     * @return
     */
    public static String upload(MultipartFile file, String upFolder) {
        String savePath = null;
        try {
            if (Constants.UPLOAD_TYPE_LOCAL.equals(uploadType)) {
                if (StringUtils.isBlank(upFolder)) {
                    savePath = FileUploadUtils.upload(file);
                } else {
                    String baseDir = FileUploadUtils.getDefaultBaseDir() + "/" + upFolder;
                    savePath = FileUploadUtils.upload(baseDir, file);
                }
            } else if (Constants.UPLOAD_TYPE_OSS.equals(uploadType)) {
                String folder = StringUtils.isBlank(upFolder) ? "upload" : upFolder;
                savePath = ossUpload(file, folder);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return savePath;
    }


}
