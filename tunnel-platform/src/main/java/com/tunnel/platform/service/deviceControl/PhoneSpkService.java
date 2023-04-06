package com.tunnel.platform.service.deviceControl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONValidator;
import com.google.gson.JsonObject;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.http.HttpUtils;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeEnum;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeItemEnum;
import com.tunnel.business.datacenter.domain.enumeration.PhoneSpkEnum;
import com.tunnel.business.domain.dataInfo.ExternalSystem;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.dataInfo.SdDevicesProtocol;
import com.tunnel.business.domain.dataInfo.SdTunnels;
import com.tunnel.business.domain.logRecord.SdOperationLog;
import com.tunnel.business.mapper.dataInfo.SdDevicesMapper;
import com.tunnel.business.service.dataInfo.IExternalSystemService;
import com.tunnel.business.service.dataInfo.ISdDevicesProtocolService;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.service.dataInfo.ISdTunnelsService;
import com.tunnel.business.service.logRecord.ISdOperationLogService;
import com.tunnel.business.utils.util.SpringContextUtils;
import com.tunnel.deal.phone.PhoneSpeak;
import com.tunnel.platform.service.SdDeviceControlService;
import com.tunnel.platform.service.SdOptDeviceService;
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

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class PhoneSpkService {

    @Value("${authorize.name}")
    private String deploymentType;
    @Autowired
    private SdDevicesMapper sdDevicesMapper;
    @Autowired
    private SdDeviceControlService sdDeviceControlService;
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
        // System.out.println("电话广播websocket>>>>>>>>>>>" + jsonObject);

        if (jsonObject.containsKey("attribute") && jsonObject.containsKey("ext")) {
            String attribute = jsonObject.getString("attribute");
            String id = jsonObject.getJSONObject("ext").getString("id");

            String deviceType = jsonObject.getJSONObject("ext").getString("deviceType");
            if (StringUtils.isBlank(deviceType) && jsonObject.containsKey("device")) {
                deviceType = jsonObject.getJSONObject("device").getString("type");
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
                sdDeviceControlService.updateDeviceData(device, data, itemId);

                //接收杭山东隧道的华为推送的紧急电话事件数据，此处对于接收到的报警信息不做处理。
                /*SdEventType sdEventType = new SdEventType();
                sdEventType.setEventType("紧急电话");
                List<SdEventType> sdEventTypes = sdEventTypeMapper.selectSdEventTypeList(sdEventType);
                Long eventTypeId = sdEventTypes.get(0).getId();
                //存储事件到事件表
                SdEvent sdEvent = new SdEvent();
                sdEvent.setTunnelId(device.getEqTunnelId());
                sdEvent.setEventTypeId(eventTypeId);

                sdEvent.setEventTitle("紧急电话告警事件");
                sdEvent.setEventSource("2");
                sdEvent.setEventState("0");
                sdEvent.setStakeNum(device.getPile());
                sdEvent.setStartTime(new Date().toString());
                sdEventMapper.insertSdEvent(sdEvent);*/
            }
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
        try {
            Response response = client.newCall(request).execute();
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
                operIp = InetAddress.getLocalHost().getHostAddress();
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
        for (String spkDeviceId : spkDeviceIds) {
            phoneSpeak = getBeanOfDeviceProtocol(spkDeviceId);
            if (null != phoneSpeak) {
                break;
            }
        }
        int status = phoneSpeak.playVoice(systemUrl, map);

        //添加操作日志
        List<SdOperationLog> list = new ArrayList<>();
        for(String eqId : Arrays.asList(spkDeviceIds.toString().split(","))){
            SdOperationLog sdOperationLog = new SdOperationLog();
            sdOperationLog.setEqTypeId(DevicesTypeEnum.LS.getCode());
            sdOperationLog.setTunnelId(tunnelId);
            sdOperationLog.setEqId(eqId);
            sdOperationLog.setOperationState("playVoice");
            sdOperationLog.setControlType(controlType);
            sdOperationLog.setCreateTime(new Date());
            sdOperationLog.setOperIp(operIp);
            sdOperationLog.setState(String.valueOf(status));
            list.add(sdOperationLog);
        }

        sdOperationLogService.insertBatchSdOperationLog(list);

        return AjaxResult.success(status);
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
        for (SdDevices devices : spkList) {
            phoneSpeak = getBeanOfDeviceProtocol(devices.getEqId());
            if (null != phoneSpeak) {
                break;
            }
        }
        int resultStatus = phoneSpeak.playVoice(systemUrl, map);

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
        sdOperationLog.setOperationState("playVoice");
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

}
