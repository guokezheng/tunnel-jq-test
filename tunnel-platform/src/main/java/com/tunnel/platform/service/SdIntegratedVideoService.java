package com.tunnel.platform.service;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.page.Result;
import com.ruoyi.common.core.redis.RedisCache;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.mapper.dataInfo.SdDevicesMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class SdIntegratedVideoService {

    private static final Logger log = LoggerFactory.getLogger(SdIntegratedVideoService.class);

    @Value("${video-platform.address}")
    private String address;
    @Value("${video-platform.deptId}")
    private String deptId;
    @Resource(name = "HttpTemplate")
    private RestTemplate template;

    @Autowired
    private RedisCache redisCache;


    /**
     * 登录获取Token
     * @return
     */
    public String getToken(){
        String url = address+"/apiLogin";
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);

        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("username", "hsdsdVideo");
        requestBody.put("password", "hsdsdVideo");

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
    @Scheduled(fixedRate = 1000*60*60*24)
    public int getDeptCamList(){
        String url = address+"/system/camera/camList";
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.add("Authorization", getToken());
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
                devices.setCamType(camInfo.get("camType").toString());
                devices.setLng(camInfo.get("camLong").toString());
                devices.setLat(camInfo.get("camLat").toString());
                String status = "1";
                if(!camInfo.get("status").equals("0") || camInfo.get("status") == null){
                    status = "3";
                }
                devices.setEqStatus(status);
                mapper.updateSdDevices(devices);
            }
        }catch (Exception ex){
            log.info("同步相机数据发生异常：{}",ex.getMessage());
        }
        return 1;
    }

    /**
     * 打开相机实时流
     * type 系统编号 默认1 后期通知更改
     * camId 相机编号
     * @return
     */
    public Result getVideoStreaming(String eqId){
        SdDevices devices = SpringUtil.getBean(SdDevicesMapper.class).selectSdDevicesById(eqId);
        JSONObject result = new JSONObject();
        if(devices.getExternalDeviceId() == null){
            return Result.success();
        }
        String url = address+"/videoInfo/api/videoStreaming";
        HttpHeaders headers = new HttpHeaders();

        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        String token = getCacheToken();
        if(token == null || "".equals(token)){
//            return Result.error("获取视频平台token失败");
            return Result.success();
        }

        headers.add("Authorization", token);
        headers.setContentType(type);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("type",1)
                .queryParam("camId",devices.getExternalDeviceId());
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        try{
            HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
            httpRequestFactory.setConnectionRequestTimeout(3 * 1000);
            httpRequestFactory.setConnectTimeout(3 * 1000);
            httpRequestFactory.setReadTimeout(3 * 1000);
            RestTemplate restTemplate = new RestTemplate(httpRequestFactory);
            ResponseEntity<String> exchange = restTemplate.exchange(builder.build().toUri(), HttpMethod.POST, requestEntity, String.class);
            JSONObject object = JSONObject.parseObject(exchange.getBody()).getJSONObject("data");
            return Result.success(Optional.ofNullable(object).orElseGet(()->result));
        }catch(Exception ex){
            log.info("打开相机实时流发生异常：{}",ex.getMessage());
            return Result.success();
        }
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
     * msgType 传入值“3”
     * camId 相机编号
     * cmdType
     * 12			焦距变大(倍率变大)
     * 11            焦距变小(倍率变小)
     * 13            焦点前调
     * 14            焦点后调
     * 15            光圈扩大
     * 16            光圈缩小
     * 21            云台向上
     * 22            云台向下
     * 23            云台左转
     * 24            云台右转
     * 50            云台左上转
     * 51            云台左下转
     * 52            云台右上转
     * 53            云台右下转
     * 8            设置预置位
     * 39            调用预置位
     * 49            辅助开关开、如雨刷
     * 48          辅助开关关、如雨刷
     * 51            设置巡航起始点
     * 52            设置巡航结束点
     * 53            开始巡航
     * 54            停止巡航
     * (-1)      自动雨刷
     * 99            各方向动作停止
     * speed     当cmdType小于等于28时为云台动作的速度，预置位设置及调用表示对应的预置位编号；
*/
    public Map PTZControl(Map param) {
        SdDevices devices = SpringUtil.getBean(SdDevicesMapper.class).selectSdDevicesById(param.get("eqId").toString());
//        Optional.ofNullable(devices.getExternalDeviceId()).orElseThrow(() -> new RuntimeException("打开相机实时流查无此设备" + param.get("eqId").toString()));
        if(devices.getExternalDeviceId() == null){
            return null;
        }
        String url = address + "/videoInfo/api/PTZControl";
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.add("Authorization", getToken());
        headers.setContentType(type);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("msgType", 3)
                .queryParam("camId", devices.getExternalDeviceId())
                .queryParam("cmdType", param.get("cmdType"))
                .queryParam("speed", param.get("speed"));
        ResponseEntity<String> exchange = template.exchange(builder.build().toUri(), HttpMethod.GET, requestEntity, String.class);
        JSONObject object = JSONObject.parseObject(exchange.getBody()).getJSONObject("data");
        return Optional.ofNullable(object).orElseGet(() -> JSONObject.parseObject(exchange.getBody()));
    }
    /**
     * 查询附近相机
     * devLat	摄像机纬度
     * devLon	摄像机经度
     * distance	距离单位米
     */
    public Map nearCamList(Map param){
        String url = address+"/videoInfo/api/nearCamListDistance";
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.add("Authorization", getToken());
        headers.setContentType(type);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("devLat",param.get("devLat"))
                .queryParam("devLon",param.get("devLon"))
                .queryParam("distance",param.get("distance"));
        ResponseEntity<String> exchange = template.exchange(builder.build().toUri(), HttpMethod.GET, requestEntity, String.class);
        String data = JSONObject.parseObject(exchange.getBody()).getString("data");
        return JSONObject.parseObject(data);
    }

    /**
     * 查询预置位
     * camId
     * @return
     */
    public Map presetList(String eqId){
        SdDevices devices = SpringUtil.getBean(SdDevicesMapper.class).selectSdDevicesById(eqId);
        if(devices.getExternalDeviceId() == null){
            return null;
        }
        String url = address+"/preset/presetList";
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.add("Authorization", getToken());
        headers.setContentType(type);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setOutputStreaming(false);
        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("camId",devices.getExternalDeviceId());
        ResponseEntity<String> exchange = restTemplate.exchange(builder.build().toUri(), HttpMethod.GET, requestEntity, String.class);
        String data = JSONObject.parseObject(exchange.getBody()).getString("data");
        return JSONObject.parseObject(data);
    }

    /**
     * 新增预置位
     * @param param
     * @return
     */
    public Map addPreset(Map param) {
        SdDevices devices = SpringUtil.getBean(SdDevicesMapper.class).selectSdDevicesById(param.get("eqId").toString());
//        Optional.ofNullable(devices.getExternalDeviceId()).orElseThrow(() -> new RuntimeException("打开相机实时流查无此设备" + param.get("eqId").toString()));
        if(devices.getExternalDeviceId() == null){
            return null;
        }
        String url = address + "/preset/addPreset";
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.add("Authorization", getToken());
        headers.setContentType(type);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setOutputStreaming(false);
        RestTemplate restTemplate = new RestTemplate();
        //restTemplate.setErrorHandler(new RtErrorHandler());
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("camId", devices.getExternalDeviceId())
                .queryParam("presetName", param.get("presetName"));
        ResponseEntity<String> exchange = restTemplate.exchange(builder.build().toUri(), HttpMethod.GET, requestEntity, String.class);
        JSONObject object = JSONObject.parseObject(exchange.getBody()).getJSONObject("data");
        return Optional.ofNullable(object).orElseGet(() -> JSONObject.parseObject(exchange.getBody()));
    }

    /**
     * 获取组织树
     * @return
     */
    public Map getDeptTree(){
        String url = address+"/system/dept/treeselect";
        HttpHeaders headers = new HttpHeaders();
        //String dataToken = getDataToken();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.add("Authorization", getToken());
        headers.setContentType(type);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setOutputStreaming(false);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> exchange = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
        String data = JSONObject.parseObject(exchange.getBody()).getString("data");
        JSONObject object = JSONObject.parseObject(data);
        if(object.isEmpty()) {
            throw new RuntimeException("接口调用失败");
        }
        return object;
    }

    /**
     * 获取所有相机接口
     * @param token
     * @return
     */
    public Map getCamTree(String token){
        String url = address+"/system/dept/camTreeselect";
        HttpHeaders headers = new HttpHeaders();
        //String dataToken = getDataToken();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.add("Authorization", "Bearer" + token);
        headers.setContentType(type);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setOutputStreaming(false);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> exchange = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
        String data = JSONObject.parseObject(exchange.getBody()).getString("data");
        JSONObject object = JSONObject.parseObject(data);
        if(object.isEmpty()) {
            throw new RuntimeException("接口调用失败");
        }
        return object;
    }
}
