package com.tunnel.business.utils.sso;

import cn.hutool.json.JSONUtil;
import com.tunnel.business.utils.util.SpringContextUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tunnel.business.utils.sso.AuthConfig.*;

@Component
public class AuthUtil {


    public static String getAuthCode(String username) {
        RestTemplate restTemplate = (RestTemplate) SpringContextUtils.getBean("OkHttpRestTemplate");

        //设置请求头
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        //设置请求体
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(httpHeaders);
        //发送请求
        String url = authCodeUrl + "?username=" + username;
        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(url, httpEntity, Map.class);

        HashMap<String, Object> body = (HashMap<String, Object>) responseEntity.getBody();
        String deptTree = null;
        if (body.get("code").equals(0)) {
            deptTree = (String) body.get("data");
        }
        return deptTree;
    }


    public static String getToken(String authCode) {
        RestTemplate restTemplate = (RestTemplate) SpringContextUtils.getBean("OkHttpRestTemplate");

        //注释代码：如果切换了登录用户，还是获取之前在redis中缓存的token，在退出登录时并没有清空此token
//        RedisCache redisCache = (RedisCache) SpringContextUtils.getBean(RedisCache.class);

//        String tocc_token = redisCache.getCacheObject(Constants.TOCC_TOKEN_KEY);
//        if (null != tocc_token) {
//            return tocc_token;
//        }

        //设置请求头
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        //设置请求体
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(httpHeaders);
        //发送请求
        String url = tokenUrl + "?code=" + authCode + "&app_id=" + appId;
        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(url, httpEntity, Map.class);

        HashMap<String, Object> body = (HashMap<String, Object>) responseEntity.getBody();
        String token = null;
        if (body.get("code").equals(0)) {
            token = (String) body.get("access_token");
//            redisCache.setCacheObject(Constants.TOCC_TOKEN_KEY, token, 30, TimeUnit.MINUTES);
        }
        return token;
    }

    public static Map<String, Object> getUserInfo(String token) {
        RestTemplate restTemplate = (RestTemplate) SpringContextUtils.getBean("OkHttpRestTemplate");

        //设置请求头
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add("Authorization", "Bearer " + token);
        //设置请求体
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(httpHeaders);
        //发送请求
        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(userInfoUrl, httpEntity, Map.class);

        HashMap<String, Object> body = (HashMap<String, Object>) responseEntity.getBody();
        Map<String, Object> userInfo = null;
        if (body.get("code").equals(0)) {
            userInfo = JSONUtil.toBean(JSONUtil.parse(body.get("data")).toString(), Map.class);
        }
        return userInfo;
    }


    public static List<Map<String, Object>> getDeptTree(String token, String id) {
        RestTemplate restTemplate = (RestTemplate) SpringContextUtils.getBean("OkHttpRestTemplate");

        //设置请求头
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.add("Authorization", "Bearer " + token);
        //设置请求体
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(httpHeaders);
        //发送请求
        String url = StringUtils.isNotBlank(id) ? (deptTreeUrl + "?id=" + id) : deptTreeUrl;
        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(url, httpEntity, Map.class);
        HashMap<String, Object> body = (HashMap<String, Object>) responseEntity.getBody();

        List<Map<String, Object>> list = null;
        if (body.get("code").equals(0)) {
            list = (List<Map<String, Object>>) body.get("data");
        }
        return list;
    }


    public static List<Map<String, Object>> treeToList(List<Map<String, Object>> treeList, List<Map<String, Object>> deptList) {
        if (null != treeList && treeList.size() > 0) {
            for (Map<String, Object> deptMap : treeList) {
                deptList.add(deptMap);
                Object children = deptMap.get("children");
                if (null != children) {
                    List<Map<String, Object>> childs = (List<Map<String, Object>>) children;
                    treeToList(childs, deptList);
                }
            }
        }
        return deptList;
    }


//    /**
//     * 根据当前登录用户获取token
//     * @return token
//     */
//    public static String getAuthToken(){
//        String username = SecurityUtils.getUsername();
//        String code = getAuthCode(username);
//        String token = getToken(code);
//        return token;
//    }

    /**
     * 使用鉴权平台分配的统一标识获取token
     * @return
     */
    public static String getGeneralToken(){
        String token = "";

        RestTemplate restTemplate = (RestTemplate) SpringContextUtils.getBean("OkHttpRestTemplate");

        //设置请求头
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        //设置请求体
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(httpHeaders);
        //发送请求
        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(AuthConfig.generalTokenUrl, httpEntity, Map.class);

        Map body = responseEntity.getBody();
        if (body != null) {
            token = String.valueOf(body.get("access_token"));
        }
        return token;
    }

}
