package com.tunnel.platform.service;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.page.Result;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.mapper.dataInfo.SdDevicesMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class SdIntegratedVideoService {

    @Value("${video-platform.ip}")
    private String ip;
    @Value("${video-platform.port}")
    private String ipPort;
    @Value("${video-platform.deptId}")
    private String deptId;

    /**
     * 登录获取Token
     * @return
     */
    public String getToken(){
        String address = ip+":"+ipPort;
        String url = "http://"+address+"/apiLogin";
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);

        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("username", "hsdsdVideo");
        requestBody.put("password", "hsdsdVideo");

        HttpEntity<HashMap<String, Object>> httpEntity = new HttpEntity<>(requestBody, headers);

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setOutputStreaming(false);
        RestTemplate restTemplate = new RestTemplate();
        //restTemplate.setErrorHandler(new RtErrorHandler());
        ResponseEntity<Map> exchange = restTemplate.exchange(url, HttpMethod.POST, httpEntity, Map.class);
        Map body = exchange.getBody();
        if(body.isEmpty()) {
            throw new RuntimeException("接口调用失败");
        }
        return body.get("token").toString();
    }

    /**
     * 获取组织树
     * @param token
     * @return
     */
    public Map getDeptTree(String token){
        String url = "http://"+ip+":"+ipPort+"/system/dept/treeselect";
        HttpHeaders headers = new HttpHeaders();
        //String dataToken = getDataToken();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.add("Authorization", "Bearer" + token);
        headers.setContentType(type);

        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("username", "hsdsdVideo");
        requestBody.put("password", "hsdsdVideo");

        HttpEntity<HashMap<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setOutputStreaming(false);
        RestTemplate restTemplate = new RestTemplate();
//        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
//                .queryParam("carNum",plateNumber);
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
        String url = "http://"+ip+":"+ipPort+"/system/dept/camTreeselect";
        HttpHeaders headers = new HttpHeaders();
        //String dataToken = getDataToken();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.add("Authorization", "Bearer" + token);
        headers.setContentType(type);

        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("username", "hsdsdVideo");
        requestBody.put("password", "hsdsdVideo");

        HttpEntity<HashMap<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setOutputStreaming(false);
        RestTemplate restTemplate = new RestTemplate();
        //restTemplate.setErrorHandler(new RtErrorHandler());
//        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
//                .queryParam("carNum",plateNumber);
        ResponseEntity<String> exchange = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
        String data = JSONObject.parseObject(exchange.getBody()).getString("data");
        JSONObject object = JSONObject.parseObject(data);
        if(object.isEmpty()) {
            throw new RuntimeException("接口调用失败");
        }
        return object;
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
        String url = "http://"+ip+":"+ipPort+"/system/camera/camList";
        HttpHeaders headers = new HttpHeaders();
        //String dataToken = getDataToken();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.add("Authorization", getToken());
        headers.setContentType(type);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setOutputStreaming(false);
        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("deptId",deptId);
        ResponseEntity<String> exchange = restTemplate.exchange(builder.build().toUri(), HttpMethod.GET, requestEntity, String.class);
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
        //JSONObject object = JSONObject.parseObject(data);
//        if(test.isEmpty()) {
//            throw new RuntimeException("接口调用失败");
//        }

        return 1;
    }

    /**
     * 打开相机实时流
     * type 系统编号 默认1 后期通知更改
     * camId 相机编号
     * @return
     */
    public Map getVideoStreaming(String eqId){
        SdDevices devices = SpringUtil.getBean(SdDevicesMapper.class).selectSdDevicesById(eqId);
        Optional.ofNullable(devices.getExternalDeviceId()).orElseThrow(()->new RuntimeException("打开相机实时流查无此设备"+eqId));
        String url = "http://"+ip+":"+ipPort+"/videoInfo/api/videoStreaming";
        HttpHeaders headers = new HttpHeaders();

        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.add("Authorization", getToken());
        headers.setContentType(type);

        HashMap<String, Object> requestBody = new HashMap<>();
//        requestBody.put("type", 1);
//        requestBody.put("camId", devices.getExternalDeviceId());
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("type",1)
                .queryParam("camId",devices.getExternalDeviceId());

        HttpEntity<HashMap<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setOutputStreaming(false);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> exchange = restTemplate.exchange(builder.build().toUri(), HttpMethod.POST, requestEntity, String.class);
        JSONObject object = JSONObject.parseObject(exchange.getBody()).getJSONObject("data");
        Optional.ofNullable(object).orElseThrow(()->new RuntimeException("打开相机实时流接口调用失败"));
        return object;
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
    public Map PTZControl(Map param){
        SdDevices devices = SpringUtil.getBean(SdDevicesMapper.class).selectSdDevicesById(param.get("eqId").toString());
        Optional.ofNullable(devices.getExternalDeviceId()).orElseThrow(()->new RuntimeException("打开相机实时流查无此设备"+param.get("eqId").toString()));
        String url = "http://"+ip+":"+ipPort+"/videoInfo/api/PTZControl";
        HttpHeaders headers = new HttpHeaders();
        //String dataToken = getDataToken();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.add("Authorization", getToken());
        headers.setContentType(type);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setOutputStreaming(false);
        RestTemplate restTemplate = new RestTemplate();
        //restTemplate.setErrorHandler(new RtErrorHandler());
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("msgType", 3)
                .queryParam("camId", devices.getExternalDeviceId())
                .queryParam("cmdType", param.get("cmdType"))
                .queryParam("speed", param.get("speed"));
        ResponseEntity<String> exchange = restTemplate.exchange(builder.build().toUri(), HttpMethod.GET, requestEntity, String.class);
        JSONObject object = JSONObject.parseObject(exchange.getBody()).getJSONObject("data");
        Optional.ofNullable(object).orElseThrow(()->new RuntimeException(JSONObject.parseObject(exchange.getBody()).getString("msg")));
        return object;
    }

    /**
     * devLat	摄像机纬度
     * devLon	摄像机经度
     * distance	距离单位米
     */
    public Map nearCamList(Map param){
        String url = "http://"+ip+":"+ipPort+"/videoInfo/api/nearCamListDistance";
        HttpHeaders headers = new HttpHeaders();
        //String dataToken = getDataToken();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.add("Authorization", "Bearer" + param.get("token"));
        headers.setContentType(type);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setOutputStreaming(false);
        RestTemplate restTemplate = new RestTemplate();
        //restTemplate.setErrorHandler(new RtErrorHandler());
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("devLat",param.get("deptId"))
                .queryParam("devLon",param.get("deptId"))
                .queryParam("distance",param.get("deptId"));

        //调取第三方接口获取车辆数据
        ResponseEntity<String> exchange = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
        String data = JSONObject.parseObject(exchange.getBody()).getString("data");
        JSONObject object = JSONObject.parseObject(data);
        if(object.isEmpty()) {
            throw new RuntimeException("接口调用失败");
        }
        return object;
    }
}
