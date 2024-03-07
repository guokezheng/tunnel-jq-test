package com.ruoyi.quartz.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONValidator;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.framework.web.domain.server.Sys;
import com.tunnel.business.domain.dataInfo.SdTunnels;
import com.tunnel.business.mapper.event.SdEventMapper;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.service.dataInfo.ISdTunnelsService;
import com.tunnel.deal.light.HttpUrlEscapeUtil;
import com.zc.common.core.httpclient.OkHttpClientUtil;
import com.zc.common.core.websocket.WebSocketService;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Encoder;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 工作台事件预警定时任务
 */
@Component("WorkbenchIncidentTask")
public class WorkbenchIncidentTask {
    private static final Logger log = LoggerFactory.getLogger(WorkbenchIncidentTask.class);

    @Autowired
    private ISdTunnelsService sdTunnelsService;


//    @Scheduled(fixedDelay=500)
    public void incidentNumTask(){
        try{
//            Integer eventUntreatedNum = SpringUtils.getBean(SdEventMapper.class).getEventUntreatedNum();
//////            WebSocketService.broadcastCloser("eventUntreatedNum", eventUntreatedNum);
//            WebSocketService.broadcast("eventUntreatedNum", eventUntreatedNum);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void syncTunnelConfig(){
        String baseLogUrl = "http://10.166.157.185:7080/prod-api/login";
        String baseUrl = "http://10.166.157.185:7080/prod-api/tunnels";
        String username = "admin";
        String password = "Tunnel123!@#";
        //获取token
        String token = login( username, password,baseLogUrl);
        //发送数据
        if(!StringUtils.isEmpty(token)){
            List<SdTunnels> sdTunnelList = sdTunnelsService.selectSdTunnelsLists();
            for(SdTunnels sdTunnels  :sdTunnelList){
                Response response = null;
                try {
                    String data = JSON.toJSONString(sdTunnels);
                    OkHttpClient client = new OkHttpClient().newBuilder().hostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            //强行返回true 即验证成功
                            return true;
                        }
                    }).connectTimeout(30, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS).build();

                    MediaType mediaType=MediaType.parse("application/json");
                    RequestBody body=RequestBody.create(mediaType, data);
                    Request request=new Request.Builder().url(baseUrl).put(body).addHeader("Authorization", token).build();
                    response=client.newCall(request).execute();
                    JSONObject jo = JSONObject.parseObject(  response.body().string());
                    token = jo.getString("token");
                } catch (IOException e) {
                    e.printStackTrace();
                }finally{
                    if(response != null){
                        response.close();
                    }
                }
            }
        }
    }
    public String login(String username, String password, String baseUrl) {
        Response response = null;
        String token = "";
        try {
            String data = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";
            OkHttpClient client = new OkHttpClient().newBuilder().hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    //强行返回true 即验证成功
                    return true;
                }
            }).build();
            MediaType mediaType=MediaType.parse("application/json");
            RequestBody body=RequestBody.create(mediaType, data);
            Request request=new Request.Builder().url(baseUrl).post(body).build();
            response=client.newCall(request).execute();
            JSONObject jo = JSONObject.parseObject(  response.body().string());
            token = jo.getString("token");
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(response != null){
                response.close();
            }
        }
        return token;
    }
}
