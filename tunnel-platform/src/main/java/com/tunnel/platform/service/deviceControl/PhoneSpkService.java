package com.tunnel.platform.service.deviceControl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONValidator;
import com.google.gson.JsonObject;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.http.HttpUtils;
import com.tunnel.business.datacenter.domain.enumeration.*;
import com.tunnel.business.domain.dataInfo.ExternalSystem;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.dataInfo.SdDevicesProtocol;
import com.tunnel.business.domain.dataInfo.SdTunnels;
import com.tunnel.business.domain.event.SdEvent;
import com.tunnel.business.domain.event.SdEventType;
import com.tunnel.business.domain.logRecord.SdOperationLog;
import com.tunnel.business.mapper.dataInfo.SdDevicesMapper;
import com.tunnel.business.mapper.event.SdEventMapper;
import com.tunnel.business.mapper.event.SdEventTypeMapper;
import com.tunnel.business.service.dataInfo.*;
import com.tunnel.business.service.event.ISdEventService;
import com.tunnel.business.service.event.ISdEventTypeService;
import com.tunnel.business.service.logRecord.ISdOperationLogService;
import com.tunnel.business.utils.util.SpringContextUtils;
import com.tunnel.deal.phone.LdPhoneSpeak;
import com.tunnel.deal.phone.PhoneSpeak;
import com.tunnel.platform.service.SdOptDeviceService;
import com.zc.common.core.websocket.WebSocketService;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.ruoyi.common.utils.DictUtils.getCacheEventKey;

@Service
public class PhoneSpkService {

    @Value("${authorize.name}")
    private String deploymentType;
    @Autowired
    private SdDevicesMapper sdDevicesMapper;
    @Autowired
    private ISdDeviceDataService deviceDataService;
    @Autowired
    private IExternalSystemService externalSystemService;
    @Autowired
    private ISdDevicesService sdDevicesService;
    @Autowired
    private ISdDevicesProtocolService sdDevicesProtocolService;
    @Autowired
    private ISdTunnelsService sdTunnelsService;
    @Autowired
    private SdOptDeviceService sdOptDeviceService;
    @Autowired
    private ISdOperationLogService sdOperationLogService;

    @Autowired
    private SdEventTypeMapper sdEventTypeMapper;

    @Autowired
    private SdEventMapper sdEventMapper;

    @Autowired
    private ISdEventTypeService eventTypeService;

    @Autowired
    private ISdTunnelsService tunnelsService;

    @Autowired
    private ISdEventService sdEventService;

    /**
     * 从Spring容器中获取设备协议中配置的Class对象
     *
     * @param deviceId
     * @return
     */
    public PhoneSpeak getBeanOfDeviceProtocol(String deviceId) {
        SdDevices device = sdDevicesService.selectSdDevicesById(deviceId);

        String brandId = device.getBrandId();
        Long fEqType = device.getfEqType();
        Assert.hasText(brandId, "未设置该设备的品牌");
        Assert.notNull(fEqType, "未设置该设备所属大类");

        SdDevicesProtocol protocol = new SdDevicesProtocol();
        protocol.setBrandId(brandId);
        protocol.setEqType(fEqType);
        List<SdDevicesProtocol> protocolList = sdDevicesProtocolService.selectSdDevicesProtocolList(protocol);
        Assert.notEmpty(protocolList, "未查询到该设备的相关协议配置");

        SdDevicesProtocol sdDevicesProtocol = protocolList.get(0);
        String className = sdDevicesProtocol.getClassName();

        PhoneSpeak phoneSpeak = null;
        try {
            Class<?> aClass = Class.forName(className);
            Object object = aClass.newInstance();
            if (object instanceof PhoneSpeak) {
                phoneSpeak = (PhoneSpeak) SpringContextUtils.getBean(object.getClass());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return phoneSpeak;
    }

    public LdPhoneSpeak getLdBeanOfDeviceProtocol(String deviceId) {
        SdDevices device = sdDevicesService.selectSdDevicesById(deviceId);

        String brandId = device.getBrandId();
        Long fEqType = device.getfEqType();
        Assert.hasText(brandId, "未设置该设备的品牌");
        Assert.notNull(fEqType, "未设置该设备所属大类");

        SdDevicesProtocol protocol = new SdDevicesProtocol();
        protocol.setBrandId(brandId);
        protocol.setEqType(fEqType);
        List<SdDevicesProtocol> protocolList = sdDevicesProtocolService.selectSdDevicesProtocolList(protocol);
        Assert.notEmpty(protocolList, "未查询到该设备的相关协议配置");

        SdDevicesProtocol sdDevicesProtocol = protocolList.get(0);
        String className = sdDevicesProtocol.getClassName();

        LdPhoneSpeak ldPhoneSpeak = null;
        try {
            Class<?> aClass = Class.forName(className);
            Object object = aClass.newInstance();
            if (object instanceof LdPhoneSpeak) {
                ldPhoneSpeak = (LdPhoneSpeak) SpringContextUtils.getBean(object.getClass());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ldPhoneSpeak;
    }


    /**
     * 登录获取token
     * 测试过程发现请求接口不需要携带token,此方法暂时用不上
     *
     * @param username
     * @param password
     * @return
     */
    public static String login(String username, String password) {
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("username", "admin");
        jsonObject.addProperty("password", "admin");

        okhttp3.RequestBody body = okhttp3.RequestBody.create(mediaType, jsonObject.toString());
        String url = "http://10.7.187.6:8980/api/platform/login";
        Request request = new Request.Builder()
                .url(url)
                .method("POST", body)
                .build();
        Response response = null;
        String result = null;
        try {
            response = client.newCall(request).execute();
            if (response.body() != null) {
                result = response.body().string();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(response != null){
                response.close();
            }
        }
        if (StringUtils.isNotBlank(result)) {
            JSONObject jo = JSONObject.parseObject(result);
            result = jo.getJSONObject("data").getString("token");
        }
        return result;
    }

    /**
     * 电话广播设备向兴电紧急电话广播管理平台推送websocket消息（设备状态消息）
     * 设备状态消息存库
     *
     * @param jsonObject
     */
    public void onMessage(@RequestBody JSONObject jsonObject) {
        //所有事件类型Map
        Map<Long,String> eventTypeMap = eventTypeService.getEventTypeMap();
        //所有隧道Map
        Map<String,String> tunnelMap = tunnelsService.getTunnelNameMap();
        // System.out.println("电话广播websocket>>>>>>>>>>>" + jsonObject);
        String data1 = jsonObject.getString("data");
        JSONObject jsonObject1 = JSONObject.parseObject(data1);
        if (jsonObject1.containsKey("attribute") && jsonObject1.containsKey("ext")) {
            String attribute = jsonObject1.getString("attribute");
            String id = jsonObject1.getJSONObject("ext").getString("id");

            String deviceType = jsonObject1.getJSONObject("ext").getString("deviceType");
            if (StringUtils.isBlank(deviceType) && jsonObject1.containsKey("device")) {
                deviceType = jsonObject1.getJSONObject("device").getString("type");
            }
            Integer itemId = null;
            SdDevices devices = new SdDevices();
            devices.setExternalDeviceId(id);
            if ("linePhoneExt".equalsIgnoreCase(deviceType) || "masterPhoneExt".equalsIgnoreCase(deviceType)) {
                devices.setEqType(DevicesTypeEnum.ET.getCode());
                itemId = DevicesTypeItemEnum.JIN_JI_DIAN_HUA.getCode();
            } else if ("spk".equalsIgnoreCase(deviceType) || "Speak".equalsIgnoreCase(deviceType)) {
                devices.setEqType(DevicesTypeEnum.LS.getCode());
                itemId = DevicesTypeItemEnum.GUANG_BO.getCode();
            }
            SdDevices device = sdDevicesMapper.selectPhoneSpk(devices);

            if (null != device) {
                String data = PhoneSpkEnum.getValue(attribute);
                if(data == null || "".equals(data)){
                    return;
                }
                SdDevices sdDevices = new SdDevices();
                sdDevices.setEqStatus(data);
                sdDevices.setEqId(device.getEqId());
                sdDevicesMapper.updateSdDevices(sdDevices);
                deviceDataService.updateDeviceData(device, data, Long.valueOf(itemId));

                //判断设备如果数据杭山东隧道，此处对于接收到的报警信息不做处理。
                if(!TunnelEnum.HANG_SHAN_DONG.getCode().equals(device.getEqTunnelId()) && PhoneSpkEnum.ANSWERED.getCode().equals(data)){
                    //查询配置提示音事件类型
                    SdEventType eventType = new SdEventType();
                    eventType.setIsUsable("1");
                    eventType.setIsConfig("1");
                    List<SdEventType> typeList = sdEventTypeMapper.selectSdEventTypeList(eventType);
                    SdEventType sdEventType = new SdEventType();
                    sdEventType.setEventType("紧急电话");
                    List<SdEventType> sdEventTypes = sdEventTypeMapper.selectSdEventTypeList(sdEventType);
                    Long eventTypeId = sdEventTypes.get(0).getId();
                    //存储事件到事件表
                    SdEvent sdEvent = new SdEvent();
                    sdEvent.setTunnelId(device.getEqTunnelId());
                    sdEvent.setEventTypeId(eventTypeId);
                    sdEvent.setEventGrade("1");
                    sdEvent.setDirection(device.getEqDirection());
                    sdEvent.setEventTitle(sdEventService.getDefaultEventTitle(sdEvent,tunnelMap,eventTypeMap));
                    sdEvent.setEventSource("2");
                    sdEvent.setEventState(EventStateEnum.unprocessed.getCode());
                    sdEvent.setStakeNum(device.getPile());
                    sdEvent.setStartTime(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS,DateUtils.getNowDate()));
                    sdEvent.setEventTime(DateUtils.getNowDate());
                    sdEventMapper.insertSdEvent(sdEvent);
                    for(SdEventType item : typeList){
                        if(sdEvent.getEventTypeId() == item.getId()){
                            eventAudio();
                        }
                    }
                    eventSendWeb(sdEvent);
                }
            }
        }
    }

    /**
     * 将事件推送到前端
     *  @param sdEvent
     */
    public void eventSendWeb(SdEvent sdEvent){
        //新增后再查询数据库，返回给前端事件图标等字段
        SdEvent sdEventData = new SdEvent();
        sdEventData.setId(sdEvent.getId());
        List<SdEvent> sdEventList = sdEventMapper.selectSdEventList(sdEventData);
        sdEventList.stream().forEach(item -> item.setIds(item.getId().toString()));
        List<SdTunnels> sdTunnelsList = tunnelsService.selectTunnels(SecurityUtils.getDeptId());
        List<SdTunnels> collect = sdTunnelsList.stream().filter(item -> item.getTunnelId().equals(sdEventList.get(0).getTunnelId())).collect(Collectors.toList());
        if(collect.size() > 0){
            //新增事件后推送前端  弹出视频
            JSONObject object = new JSONObject();
            object.put("sdEventList", sdEventList);
            WebSocketService.broadcast("sdEventList",object.toString());
        }
    }


    /**
     * 获取音频文件列表
     *
     * @return
     */
    public AjaxResult getAudioFileList(Map<String, Object> map) {
        String deviceId = (String) map.get("deviceId");

        Long externalSystemId = null;
        if (StringUtils.isNotBlank(deviceId)) {
            SdDevices devices = sdDevicesMapper.selectSdDevicesById(deviceId);
            externalSystemId = devices.getExternalSystemId();
            if(externalSystemId==null||"".equals(externalSystemId)){
                throw new RuntimeException("未配置所选设备关联的外部系统！");
            }
            //Assert.notNull(externalSystemId, "未配置所选设备关联的外部系统");
        } else {
            String tunnelId = (String) map.get("tunnelId");
            String direction = (String) map.get("direction");
            if(tunnelId==null||"".equals(tunnelId)){
                throw new RuntimeException("未指定隧道信息！");
            }
            if(direction==null||"".equals(direction)){
                throw new RuntimeException("未指定隧道方向！");
            }
            //Assert.hasText(tunnelId, "未指定隧道信息");
            //Assert.hasText(direction, "未指定隧道方向");

            SdDevices device = new SdDevices();
            device.setEqTunnelId(tunnelId);
            device.setEqDirection(direction);
            device.setEqType(DevicesTypeEnum.LS.getCode());
            List<SdDevices> spkList = sdDevicesMapper.getSpkList(device);
            if(spkList==null||spkList.get(0)==null){
                throw new RuntimeException("该方向隧道未查询到广播设备！");
            }
            //Assert.notEmpty(spkList, "该方向隧道未查询到广播设备");

            for (SdDevices devices : spkList) {
                externalSystemId = devices.getExternalSystemId();
                if (null != externalSystemId) {
                    break;
                }
            }
            if(externalSystemId==null||"".equals(externalSystemId)){
                throw new RuntimeException("该方向隧道的广播设备未配置 关联的外部系统！");
            }
            //Assert.notNull(externalSystemId, "该方向隧道的广播设备未配置 关联的外部系统");
        }
        ExternalSystem externalSystem = externalSystemService.selectExternalSystemById(externalSystemId);
        String systemUrl = externalSystem.getSystemUrl();
        if(systemUrl==null||"".equals(systemUrl)){
            throw new RuntimeException("未配置该设备所属的外部系统地址！");
        }
        //Assert.hasText(systemUrl, "未配置该设备所属的外部系统地址");

        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(systemUrl + "/api/speak/voiceList")
                .build();

        JSONArray jsonArray = new JSONArray();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            String result = response.body().string();

            if (StringUtils.isNotBlank(result) && JSONValidator.from(result).validate()) {
                JSONObject jo = JSONObject.parseObject(result);
                JSONObject data = jo.getJSONObject("data");
                if (null != data) {
                    jsonArray = data.getJSONArray("items");
                }
            }
        } catch (IOException e) {
            return AjaxResult.success(jsonArray);
        }finally{
            if(response != null){
                response.close();
            }
        }

        return AjaxResult.success(jsonArray);
    }

    /**
     * 播放音频
     *
     * @param map
     * @return
     */
    public AjaxResult playVoice(@RequestBody Map<String, Object> map) {
        ArrayList fileList = (ArrayList) map.get("fileNames");
        ArrayList<String> spkDeviceIds = (ArrayList<String>) map.get("spkDeviceIds");
        String controlType = (String) map.get("controlType");
        String operIp = (String) map.get("operIp");
        String tunnelId = (String) map.get("tunnelId");
        //参数校验
        if(fileList.size() > 0){
            String file = fileList.get(0).toString();
            if("".equals(file) || file == null){
                Assert.notEmpty(new ArrayList<>(), "未选择音频文件！");
            }
        }
        Assert.notEmpty(fileList, "未选择音频文件！");
        Assert.notEmpty(spkDeviceIds, "未选择广播设备！");
        Assert.hasText(controlType, "未指定控制类型参数！");
        Assert.hasText(tunnelId, "未指定隧道信息");

        if ("GSY".equals(deploymentType)) {
            Assert.hasText(operIp, "未提供操作方IP地址参数！");
            SdTunnels tunnel = sdTunnelsService.selectSdTunnelsById(tunnelId);
            //设备所属管理站host
            String host = sdOptDeviceService.getGlzHost(String.valueOf(tunnel.getDeptId()));
            String url = host + "/api/speak/playVoice";
            // String url = "http://10.168.75.50:8000/api/speak/playVoice";

            String response = HttpUtils.sendPostByApplicationJson(url, JSONObject.toJSONString(map));
            if (StringUtils.isNotBlank(response) && JSONValidator.from(response).validate()) {
                JSONObject jsonObject = JSONObject.parseObject(response);
                jsonObject.containsKey("code");
                if (200 == jsonObject.getInteger("code")) {
                    return AjaxResult.success();
                }
            }
            return AjaxResult.error();
        } else if ("GLZ".equals(deploymentType) && StringUtils.isBlank(operIp)) {
            try {
                if(operIp == null || "".equals(operIp)){
                    operIp = InetAddress.getLocalHost().getHostAddress();
                }
            } catch (UnknownHostException e) {

            }
        }

        Long externalSystemId = null;
        String hostId = null;
        for (String spkDeviceId : spkDeviceIds) {
            SdDevices devices = sdDevicesMapper.selectSdDevicesById(spkDeviceId);
            externalSystemId = devices.getExternalSystemId();
            if (null != externalSystemId) {
                break;
            }
        }
        Assert.notNull(externalSystemId, "未配置所选设备关联的外部系统");
        for (String spkDeviceId : spkDeviceIds) {
            SdDevices devices = sdDevicesMapper.selectSdDevicesById(spkDeviceId);
            SdDevices fEq = sdDevicesMapper.selectSdDevicesById(devices.getfEqId());
            hostId = fEq.getExternalDeviceId();
            if (null != hostId) {
                break;
            }
        }
        Assert.hasText(hostId, "未配置所选设备的父设备的主机序号");

        ExternalSystem externalSystem = externalSystemService.selectExternalSystemById(externalSystemId);
        String systemUrl = externalSystem.getSystemUrl();
        Assert.hasText(systemUrl, "未配置该设备所属的外部系统地址");

        /*
            预案那里items参数不好传值,这里注释掉改为从后台组装
            ArrayList<Map> spkList = (ArrayList<Map>) map.get("items");
            for (Map spk : spkList) {
                spk.put("hostId", hostId);
                spk.put("hostType", "YeastarHost");
            }
        */

        ArrayList<Map> spkList = new ArrayList<>();

        for (String spkDeviceId : spkDeviceIds) {
            SdDevices devices = sdDevicesMapper.selectSdDevicesById(spkDeviceId);
            String externalDeviceId = devices.getExternalDeviceId();

            if (StringUtils.isNotBlank(externalDeviceId)) {
                Map<String, String> item = new HashMap<>();
                item.put("hostId", hostId);
                item.put("hostType", "YeastarHost");
                item.put("deviceId", externalDeviceId);
                spkList.add(item);
            }
        }
        map.put("items", spkList);

        PhoneSpeak phoneSpeak = null;
        LdPhoneSpeak ldPhoneSpeak = null;
        for (String spkDeviceId : spkDeviceIds) {
            phoneSpeak = getBeanOfDeviceProtocol(spkDeviceId);
            if (null != phoneSpeak) {
                break;
            }else {
                ldPhoneSpeak = getLdBeanOfDeviceProtocol(spkDeviceId);
            }
        }

        int status = 0;
        if(phoneSpeak == null){
            map.put("systemUrl",systemUrl);
            status = ldPhoneSpeak.playVoice(map,new SdDevices());
        }else {
            status = phoneSpeak.playVoice(systemUrl, map);
        }


        //添加操作日志
        List<SdOperationLog> list = new ArrayList<>();
        for(String eqId : spkDeviceIds){
            SdOperationLog sdOperationLog = new SdOperationLog();
            sdOperationLog.setEqTypeId(DevicesTypeEnum.LS.getCode());
            sdOperationLog.setTunnelId(tunnelId);
            sdOperationLog.setEqId(eqId);
            if(fileList.size() > 0){
                List<String> list1 = Arrays.asList(fileList.get(0).toString().split("\\\\"));
                sdOperationLog.setOperationState(list1.get(list1.size()-1));
            }else {
                sdOperationLog.setOperationState("无播放文件");
            }
            sdOperationLog.setControlType(controlType);
            sdOperationLog.setCreateTime(new Date());
            sdOperationLog.setOperIp(operIp);
            sdOperationLog.setState(String.valueOf(status));
            list.add(sdOperationLog);
        }

        sdOperationLogService.insertBatchSdOperationLog(list);

        if(status > 0){
            return AjaxResult.success(status);
        }
        return AjaxResult.error("控制失败");
    }


    public AjaxResult playVoiceGroup(@RequestBody Map<String, Object> map) {
        ArrayList fileList = (ArrayList) map.get("fileNames");
        String tunnelId = (String) map.get("tunnelId");
        String direction = (String) map.get("direction");
        String controlType = (String) map.get("controlType");
        String operIp = (String) map.get("operIp");

        if (fileList.size()>0 && fileList.get(0) != null){
        }else{
            throw new RuntimeException("未选择音频文件！");
            //Assert.notEmpty(fileList, "未选择音频文件！");
        }
        Assert.hasText(tunnelId, "未指定隧道信息");
        Assert.hasText(direction, "未指定隧道方向");
        Assert.hasText(controlType, "未指定控制类型！");

        if ("GSY".equals(deploymentType)) {
            Assert.hasText(operIp, "未提供操作方IP地址！");

            SdTunnels tunnel = sdTunnelsService.selectSdTunnelsById(tunnelId);
            //设备所属管理站host
            String host = sdOptDeviceService.getGlzHost(String.valueOf(tunnel.getDeptId()));
            String url = host + "/light/setBrightness";
            // String url = "http://10.168.75.50:8000/api/speak/playVoice";
            String response = HttpUtils.sendPostByApplicationJson(url, JSONObject.toJSONString(map));
            if (StringUtils.isNotBlank(response) && JSONValidator.from(response).validate()) {
                JSONObject jsonObject = JSONObject.parseObject(response);
                jsonObject.containsKey("code");
                if (200 == jsonObject.getInteger("code")) {
                    return AjaxResult.success();
                }
            }
            return AjaxResult.error();
        } else if ("GLZ".equals(deploymentType) && StringUtils.isBlank(operIp)) {
            try {
                operIp = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {

            }
        }
        SdDevices device = new SdDevices();
        device.setEqTunnelId(tunnelId);
        device.setEqDirection(direction);
        device.setEqType(DevicesTypeEnum.LS.getCode());
        List<SdDevices> spkList = sdDevicesMapper.getSpkList(device);
        Assert.notEmpty(spkList, "该方向隧道未查询到广播设备");

        Long externalSystemId = null;
        String hostId = null;

        for (SdDevices devices : spkList) {
            externalSystemId = devices.getExternalSystemId();
            if (null != externalSystemId) {
                break;
            }
        }
        Assert.notNull(externalSystemId, "未配置所选设备关联的外部系统");
        for (SdDevices devices : spkList) {
            SdDevices fEq = sdDevicesMapper.selectSdDevicesById(devices.getfEqId());
            hostId = fEq.getExternalDeviceId();
            if (null != hostId) {
                break;
            }
        }
        Assert.hasText(hostId, "未配置所选设备的父设备的主机序号");

        ExternalSystem externalSystem = externalSystemService.selectExternalSystemById(externalSystemId);
        String systemUrl = externalSystem.getSystemUrl();
        Assert.hasText(systemUrl, "未配置该设备所属的外部系统地址");

        ArrayList<Map> items = new ArrayList<>();
        for (SdDevices devices : spkList) {
            HashMap item = new HashMap();
            item.put("deviceId", devices.getExternalDeviceId());
            item.put("hostId", hostId);
            item.put("hostType", "YeastarHost");
            items.add(item);
        }
        map.put("items", items);

        PhoneSpeak phoneSpeak = null;
        LdPhoneSpeak ldPhoneSpeak = null;
        for (SdDevices devices : spkList) {
            phoneSpeak = getBeanOfDeviceProtocol(devices.getEqId());
            if (null != phoneSpeak) {
                break;
            }else {
                ldPhoneSpeak = getLdBeanOfDeviceProtocol(devices.getEqId());
            }
        }
        //int resultStatus = phoneSpeak.playVoice(systemUrl, map);

        int resultStatus = 0;
        if(phoneSpeak == null){
            resultStatus = ldPhoneSpeak.playVoice(map,new SdDevices());
        }else {
            resultStatus = phoneSpeak.playVoice(systemUrl, map);
        }

        //添加操作日志
        SdOperationLog sdOperationLog = new SdOperationLog();
        sdOperationLog.setEqTypeId(DevicesTypeEnum.LS.getCode());
        sdOperationLog.setTunnelId(tunnelId);
        String string = spkList.stream()
                .map(dev -> {
                    String eqId = dev.getEqId();
                    int index = eqId.lastIndexOf("-");
                    return eqId.substring(index + 1);
                })
                .collect(Collectors.joining(","));
        String eqIds = tunnelId + "-" + DevicesTypeEnum.LS.name() + "-" + string;
        sdOperationLog.setEqId(eqIds);
        if(fileList.size() > 0){
            List<String> list1 = Arrays.asList(fileList.get(0).toString().split("\\\\"));
            sdOperationLog.setOperationState(list1.get(list1.size()-1));
        }else {
            sdOperationLog.setOperationState("无播放文件");
        }
        sdOperationLog.setControlType(controlType);
        sdOperationLog.setCreateTime(new Date());
        sdOperationLog.setOperIp(operIp);
        sdOperationLog.setState(String.valueOf(resultStatus));
        sdOperationLogService.insertSdOperationLog(sdOperationLog);

        return resultStatus == 1 ? AjaxResult.success() : AjaxResult.error();
    }


    /**
     * 挂断广播
     *
     * @param paramMap
     * @return
     */
    public AjaxResult hungUp(Map<String, Object> paramMap) {
        ArrayList<String> deviceIds = (ArrayList<String>) paramMap.get("deviceIds");
        Assert.notEmpty(deviceIds, "请先选择设备");

        Long externalSystemId = null;
        for (String spkDeviceId : deviceIds) {
            SdDevices devices = sdDevicesMapper.selectSdDevicesById(spkDeviceId);
            externalSystemId = devices.getExternalSystemId();
            if (null != externalSystemId) {
                break;
            }
        }
        Assert.notNull(externalSystemId, "未配置所选设备关联的外部系统");
        ExternalSystem externalSystem = externalSystemService.selectExternalSystemById(externalSystemId);
        String systemUrl = externalSystem.getSystemUrl();
        Assert.hasText(systemUrl, "未配置该设备所属的外部系统地址");

        ArrayList<Map<String, Object>> list = new ArrayList<>();
        for (String deviceId : deviceIds) {
            SdDevices device = sdDevicesMapper.selectSdDevicesById(deviceId);
            Map<String, Object> item = new HashMap<>();
            item.put("deviceId", device.getExternalDeviceId());
            item.put("hostId", 1);
            item.put("hostType", "YeastarHost");
            list.add(item);
        }
        Map map = new HashMap();
        map.put("items", list);

        PhoneSpeak phoneSpeak = null;
        for (String spkDeviceId : deviceIds) {
            phoneSpeak = getBeanOfDeviceProtocol(spkDeviceId);
            if (null != phoneSpeak) {
                break;
            }
        }
        int resultStatus = phoneSpeak.hungUp(systemUrl, map);
        return resultStatus == 1 ? AjaxResult.success() : AjaxResult.error();
    }

    /**
     * 提示音
     */
    public static void eventAudio(){
        try {
            /*File file = new File("/home/tunnel/video/ding.WAV");
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(file));
            clip.start();*/
            WebSocketService.broadcast("playvideo",1);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
