package com.tunnel.business.service.energyManagement.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tunnel.business.datacenter.domain.enumeration.ZeroTunnelEnum;
import com.tunnel.business.service.energyManagement.SdZeroCarBonService;
import com.tunnel.business.utils.sso.AuthUtil;
import com.tunnel.business.utils.util.SpringContextUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tunnel.business.utils.sso.AuthConfig.*;

/**
 * @author zhai
 * @date 2023/9/4
 */
@Service
public class SdZeroCarBonServiceImpl implements SdZeroCarBonService {

    @Value("${sso.zeroToken}")
    public String zeroToken;

    @Value("${sso.zeroCode}")
    public String zeroCode;

    @Value("${sso.zeroName}")
    public String zeroName;

    @Value("${sso.zeroPassword}")
    public String zeroPassword;

    public String getZeroUrl(String tunnelId){
        String zeroToken = getZeroToken();
        String zeroCode = getZeroCode(zeroToken);
        String url = "http://10.166.132.26/carbon-ui/#/login?code="+zeroCode+"&&asideCollapse=false&&route=tunnel/home?pointId="+ ZeroTunnelEnum.getValue(tunnelId);
        return url;
    }

    /**
     * 获取token
     * @return
     */
    public String getZeroToken(){
        RestTemplate restTemplate = (RestTemplate) SpringContextUtils.getBean("OkHttpRestTemplate");
        //设置请求头
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        //请求参数
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username",zeroName);
        jsonObject.put("password",zeroPassword);
        jsonObject.put("captcha","1");
        jsonObject.put("uuid","b46e26fe-2981-4f00-8da4-07ac8b8ad07d");
        jsonObject.put("appId","app_carbon");
        //设置请求体
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(jsonObject,httpHeaders);
        //发送请求
        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(zeroToken, httpEntity, Map.class);

        HashMap<String, Object> body = (HashMap<String, Object>) responseEntity.getBody();
        String token = null;
        if (body.get("code").equals(0)) {
            String data = JSONObject.toJSONString(body.get("data"));
            JSONObject jsonObject1 = JSONObject.parseObject(data);
            token = jsonObject1.getString("access_token");
        }
        return token;
    }

    /**
     * 获取code
     * @param zeroToken
     * @return
     */
    public String getZeroCode(String zeroToken){
        RestTemplate restTemplate = (RestTemplate) SpringContextUtils.getBean("OkHttpRestTemplate");

        //设置请求头
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.add("Authorization", "Bearer " + zeroToken);
        //设置请求体
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(httpHeaders);
        //发送请求
        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(zeroCode, httpEntity, Map.class);
        HashMap<String, Object> body = (HashMap<String, Object>) responseEntity.getBody();
        String code = null;
        if (body.get("code").equals(0)) {
            code = body.get("data").toString();
        }
        return code;
    }
}
