package com.tunnel.platform.utils.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 全局配置类
 * 
 * @author xiaofang
 */

@Component
@ConfigurationProperties(prefix = "bun")
public class Global
{

    /** 上传路径 */
    private static String profile;


    public static String getProfile()
    {
        return profile;
    }

    public void setProfile(String profile)
    {
        Global.profile = profile;
    }


    /**
     * 获取下载路径
     */
    public static String getDownloadPath()
    {
        return getProfile() + "/download/";
    }

    /**
     * 获取上传路径
     */
    public static String getUploadPath()
    {
        return getProfile() + "/upload";
    }
}
