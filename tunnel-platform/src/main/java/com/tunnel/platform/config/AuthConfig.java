package com.tunnel.platform.config;

import com.ruoyi.common.core.redis.RedisCache;
import com.tunnel.platform.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AuthConfig {

    @Value("${sso.authCode}")
    private String authCodeUrl;

    @Value("${sso.token}")
    private String tokenUrl;

    @Value("${sso.userInfo}")
    private String userInfoUrl;

    @Value("${sso.deptTree}")
    private String deptTreeUrl;

    @Value("${sso.userList}")
    private String userListUrl;

    @Value("${sso.appId}")
    private String appId;

    @Autowired
    private static RestTemplate restTemplate;

    @Autowired
    private static RedisCache redisCache;

    @Bean
    public void initAuthBootConfiguration() {
        AuthUtil.setAuthCodeUrl(authCodeUrl);
        AuthUtil.setTokenUrl(tokenUrl);
        AuthUtil.setUserInfoUrl(userInfoUrl);
        AuthUtil.setDeptTreeUrl(deptTreeUrl);
        AuthUtil.setUserListUrl(userListUrl);
        AuthUtil.setAppId(appId);
    }
}
