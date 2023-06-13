package com.tunnel.business.utils.sso;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 将配置文件的数据注入到静态变量中，使用非静态方法set方法注入
 */
@Component
public class AuthConfig {

    public static String authCodeUrl;

    public static String tokenUrl;

    public static String userInfoUrl;


    public static String deptTreeUrl;


    public static String userListUrl;


    public static String appId;


    public static String generalTokenUrl;

    public static String defaultUser;


    public String getAuthCodeUrl() {
        return authCodeUrl;
    }

    @Value("${sso.authCode}")
    public void setAuthCodeUrl(String authCode) {
        authCodeUrl = authCode;
    }

    public String getTokenUrl() {
        return tokenUrl;
    }

    @Value("${sso.token}")
    public void setTokenUrl(String token) {
        tokenUrl = token;
    }

    public String getUserInfoUrl() {
        return userInfoUrl;
    }

    @Value("${sso.userInfo}")
    public void setUserInfoUrl(String userInfo) {
        userInfoUrl = userInfo;
    }

    public String getDeptTreeUrl() {
        return deptTreeUrl;
    }

    @Value("${sso.deptTree}")
    public void setDeptTreeUrl(String deptTree) {
        deptTreeUrl = deptTree;
    }

    public String getUserListUrl() {
        return userListUrl;
    }

    @Value("${sso.userList}")
    public void setUserListUrl(String userList) {
        userListUrl = userList;
    }

    public String getAppId() {
        return appId;
    }

    @Value("${sso.appId}")
    public void setAppId(String appIdSso) {
        appId = appIdSso;
    }

    public String getGeneralTokenUrl() {
        return generalTokenUrl;
    }

    @Value("${sso.generalTokenUrl}")
    public void setGeneralTokenUrl(String generalToken) {
        generalTokenUrl = generalToken;
    }

    @Value("${sso.defaultUser}")
    public void setDefaultUser(String defaultUserSso) {
        defaultUser = defaultUserSso;
    }


//    @Bean
//    public void initAuthBootConfiguration() {
//        AuthUtil.setAuthCodeUrl(authCodeUrl);
//        AuthUtil.setTokenUrl(tokenUrl);
//        AuthUtil.setUserInfoUrl(userInfoUrl);
//        AuthUtil.setDeptTreeUrl(deptTreeUrl);
//        AuthUtil.setUserListUrl(userListUrl);
//        AuthUtil.setAppId(appId);
//    }
}
