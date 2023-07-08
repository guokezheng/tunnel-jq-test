package com.tunnel.platform.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * describe: 外部系统登录系统配置
 *
 * @author zs
 * @date 2023/1/13
 */
@Component
public class OpmConfig {

    @Value("${opm.externalLogin.appId}")
    public String appId;

    @Value("${opm.externalLogin.loginName}")
    public String loginName;

}
