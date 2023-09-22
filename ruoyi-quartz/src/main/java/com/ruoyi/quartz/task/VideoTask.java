package com.ruoyi.quartz.task;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.tunnel.business.datacenter.domain.dataReport.ExternalSystemCode;
import com.tunnel.business.domain.dataInfo.ExternalSystem;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.mapper.dataInfo.SdDevicesMapper;
import com.tunnel.business.service.dataInfo.IExternalSystemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 摄像机定时任务
 *
 */
@Component("videoTask")
public class VideoTask {

    private static final Logger log = LoggerFactory.getLogger(VideoTask.class);

    @Resource(name = "HttpTemplate")
    private RestTemplate template;

    @Autowired
    private RedisCache redisCache;

    /**
     * 登录获取Token
     * @return
     */
    public String getToken(){

        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        String userName;
        String password;

        //查询外部系统配置
        ExternalSystem system = new ExternalSystem();
        system.setSystemCode(ExternalSystemCode.VIDEO_MANGE.getCode());
        List<ExternalSystem> list = SpringUtils.getBean(IExternalSystemService.class).selectExternalSystemList(system);

        if (list == null || list.size() < 1){
            return null;
        }
        ExternalSystem externalSystem = list.get(0);
        String address;
        // 获取数据库第三方配置信息
        if(externalSystem != null){
            address = externalSystem.getSystemUrl();
            userName = externalSystem.getUsername();
            password = externalSystem.getPassword();
        } else {
            return null;
        }

        String url = address+"/apiLogin";

        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("username", userName);
        requestBody.put("password", password);


        HttpEntity<HashMap<String, Object>> httpEntity = new HttpEntity<>(requestBody, headers);
        try{
            ResponseEntity<Map> exchange = template.exchange(url, HttpMethod.POST, httpEntity, Map.class);
            Map body = exchange.getBody();
            return Optional.ofNullable(body.get("token")).orElseGet(()->"").toString();
        }catch(Exception ex){
            log.info("打开相机实时流发生异常：{}",ex.getMessage());
        }
        return null;
    }

    /**
     * 获取缓存token
     * @return
     */
    public String getCacheToken(){
        //token缓存key值
        String key = "video_platform_token";
        //token有效时间15分钟
        Integer expireTime = 15;
        //获取缓存token
        String token = redisCache.getCacheObject(key);
        if(token == null || "".equals(token)){
            //缓存中获取不到token，重新从接口中获取，更新缓存
            token = getToken();
            redisCache.setCacheObject( key, token, expireTime, TimeUnit.MINUTES);
        }
        return token;
    }

    /**
     * 获取部门下相机列表
     * deptId 组织机构
     * camName 相机名称
     * road 道路编号如：G3
     * camOrientation 摄像机方向 0：上行1：下行2：上下行（双向）
     * camType 摄像机类型（0：默认1：道路沿线2：桥梁
     * 3：隧道4：收费广场5：收费站6：服务区
     * 7：ETC门架8：移动视频源）
     * ptzCtrl 是否有云台控制 0 有（球机）  1 无（枪机）
     * @return
     */
    public int getDeptCamList(){
        ExternalSystem system = new ExternalSystem();
        system.setSystemCode(ExternalSystemCode.VIDEO_MANGE.getCode());
        List<ExternalSystem> list = SpringUtils.getBean(IExternalSystemService.class).selectExternalSystemList(system);
        if (list == null || list.size() < 1){
            return 0;
        }

        //查询外部系统配置
        ExternalSystem externalSystem = list.get(0);
        String address;
        String deptId;
        // 获取数据库第三方配置信息
        if(externalSystem != null){
            address = externalSystem.getSystemUrl();
            deptId = externalSystem.getParam();
        } else {
            return 0;
        }

        String url = address +"/system/camera/camList";
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.add("Authorization", getCacheToken());
        headers.setContentType(type);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("deptId",deptId);
        ResponseEntity<String> exchange = template.exchange(builder.build().toUri(), HttpMethod.GET, requestEntity, String.class);
        try {
            String data = JSONObject.parseObject(exchange.getBody()).get("data").toString();
            List<Map> camList = JSONArray.parseObject(data,List.class);
            SdDevicesMapper mapper = SpringUtil.getBean(SdDevicesMapper.class);
            for(Map camInfo:camList){
                String camId = camInfo.get("camId").toString();
                SdDevices devices = mapper.getDevicesListByExternalId(camId);
                if (devices == null){
                    continue;
                }
                devices.setCamType(camInfo.get("camType").toString());
                devices.setLng(camInfo.get("camLong").toString());
                devices.setLat(camInfo.get("camLat").toString());
                String status;
                if (camInfo.get("status") != null && "0".equals(camInfo.get("status"))){
                    status = "1";
                } else if (camInfo.get("status") != null && "1".equals(camInfo.get("status"))){
                    status = "2";
                } else {
                    status = "3";
                }
                devices.setEqStatus(status);
                devices.setEqStatusTime(new Date());
                mapper.updateSdDevices(devices);
            }
        }catch (Exception ex){
            log.info("同步相机数据发生异常：{}",ex.getMessage());
        }
        return 1;
    }
}
