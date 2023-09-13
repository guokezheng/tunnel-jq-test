package com.tunnel.business.service.dataInfo.impl;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.redis.RedisCache;
import com.tunnel.business.domain.dataInfo.ExternalSystem;
import com.tunnel.business.domain.dataInfo.LightConfiguration;
import com.tunnel.business.mapper.dataInfo.LuminationFluctuateMapper;
import com.tunnel.business.service.dataInfo.IExternalSystemService;
import com.tunnel.business.service.dataInfo.IlluminationFluctuateService;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Encoder;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.util.List;
@Service
public class lluminationFluctuateServiceImpl implements IlluminationFluctuateService {

    @Autowired
    private LuminationFluctuateMapper lluminationFluctuateMapper;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private IExternalSystemService externalSystemService;


    /**
     * 波动模式开启
     * @return
     */
    @Override
    public boolean openIllumination() {
        List<LightConfiguration> lightConfigurations = lluminationFluctuateMapper.getlightConfiguration();

        //查询外部系统信息
        ExternalSystem externalSystem = externalSystemService.selectExternalSystemById(22l);
        String baseUrl = externalSystem.getSystemUrl();

        String jessionId;

        String tokenKey = "sanSiToken";

        jessionId = redisCache.getCacheObject(tokenKey);

        if(jessionId == null || "".equals(jessionId)){
            jessionId =  login(externalSystem.getUsername(), externalSystem.getPassword(), baseUrl);
            redisCache.setCacheObject(tokenKey,jessionId);
            redisCache.expire(tokenKey,15*60);
        }

        if(jessionId == null || "".equals(jessionId)){
            return false;
        }
        try {
        for( LightConfiguration LightConfig :lightConfigurations){
            updateSwitch(jessionId, baseUrl, LightConfig.getExternalId(), Integer.valueOf(1));
            updateBrightness(jessionId, baseUrl, LightConfig.getExternalId() ,90);
            Thread.sleep(500);

        } } catch (InterruptedException e) {

        }

        return false;
    }

    /**
     * 波动模式关闭
     * @return
     */
    @Override
    public boolean closeIllumination() {
        List<LightConfiguration> lightConfigurations = lluminationFluctuateMapper.getlightConfiguration();

        //查询外部系统信息
        ExternalSystem externalSystem = externalSystemService.selectExternalSystemById(22l);
        String baseUrl = externalSystem.getSystemUrl();

        String jessionId;

        String tokenKey = "sanSiToken";

        jessionId = redisCache.getCacheObject(tokenKey);

        if(jessionId == null || "".equals(jessionId)){
            jessionId =  login(externalSystem.getUsername(), externalSystem.getPassword(), baseUrl);
            redisCache.setCacheObject(tokenKey,jessionId);
            redisCache.expire(tokenKey,15*60);
        }

        if(jessionId == null || "".equals(jessionId)){
            return false;
        }
        try {
            for( LightConfiguration LightConfig :lightConfigurations){
                updateSwitch(jessionId, baseUrl, LightConfig.getExternalId(), Integer.valueOf(1));
                updateBrightness(jessionId, baseUrl, LightConfig.getExternalId() ,30);
                Thread.sleep(500);

            } } catch (InterruptedException e) {

        }
        return false;
    }

    /**
     * 登录获取会话ID
     * 用户名/密码默认是：admin/admin123
     *
     * @return
     * @throws IOException
     */
    public String login(String username, String password, String baseUrl) {
        Response response = null;
        try {
            byte[] textByte = password.getBytes("UTF-8");
            String encode = new BASE64Encoder().encode(textByte);
            String data = "{\"username\":\"" + username + "\",\"password\":\"" + encode + "\"}";
            OkHttpClient client = new OkHttpClient().newBuilder().hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    //强行返回true 即验证成功
                    return true;
                }
            }).build();
            MediaType mediaType=MediaType.parse("application/json");
            RequestBody body=RequestBody.create(mediaType, data);
            Request request=new Request.Builder().url(baseUrl+"api/core/users/login").post(body).build();
            response=client.newCall(request).execute();
            JSONObject jo = JSONObject.parseObject(  response.body().string());
            return  jo.getString("id");
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }finally{
            if(response != null){
                response.close();
            }
        }
    }

    /**
     * 控制灯开关方法
     * @param jessionId cookit
     * @param baseUrl 外部系统地址
     * @param step 关联的段号
     * @param openClose 开关
     * @return
     */
    public int updateSwitch(String jessionId ,String baseUrl , String step , Integer openClose) {
        //灯开关
        OkHttpClient client = new OkHttpClient().newBuilder().hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                //强行返回true 即验证成功
                return true;
            }
        }).build();
        MediaType mediaType = MediaType.parse("application/json");
        //传入的请求参数
        String data = "{\"param\":" + openClose + "}";
        RequestBody body = RequestBody.create(mediaType, data);

        if (openClose == 2) {
            openClose = 0;
        }
        String url = baseUrl + "api/core/assetGroups/"+step+"/action/device-sensor:switch-status";

        Request request = new Request.Builder()
                .url(url)
                .method("POST", body)
                .addHeader("Content-Type","application/json")
                .addHeader("Authorization", jessionId)
                .build();

        String responseBody = "";
        Response response = null;
        try {
            response = client.newCall(request).execute();
            //包含“发送成功"就可以
            responseBody = response.body().string();
        } catch (IOException e) {
            return 0;
        }finally{
            if(response != null){
                response.close();
            }
        }
        return responseBody.contains("pipelineId") ? 1 : 0;
    }

    /**
     * 控制灯亮度方法
     * @param jessionId  cookit
     * @param baseUrl 外部系统地址
     * @param step 关联的段号
     * @param bright 亮度
     * @return
     */
    public int updateBrightness(String jessionId ,String baseUrl , String step , Integer bright) {

        OkHttpClient client = new OkHttpClient().newBuilder().hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                //强行返回true 即验证成功
                return true;
            }
        }).build();
        MediaType mediaType = MediaType.parse("application/json");
        //传入的请求参数
        String data = "{\"param\":" + bright + "}";
        RequestBody body = RequestBody.create(mediaType, data);
        String url = baseUrl + "api/core/assetGroups/"+step+"/action/device-sensor:dim-level";

        Request request = new Request.Builder()
                .url(url)
                .method("POST", body)
                .addHeader("Content-Type","application/json")
                .addHeader("Authorization", jessionId)
                .build();

        String responseBody = "";
        Response response = null;
        try {
            response = client.newCall(request).execute();
            //包含“发送成功"就可以
            responseBody = response.body().string();
        } catch (IOException e) {
            return 0;
        }finally{
            if(response != null){
                response.close();
            }
        }
        return responseBody.contains("pipelineId") ? 1 : 0;
    }
}
