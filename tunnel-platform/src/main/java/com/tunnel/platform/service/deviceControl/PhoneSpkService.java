package com.tunnel.platform.service.deviceControl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.ruoyi.common.core.domain.AjaxResult;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeEnum;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeItemEnum;
import com.tunnel.business.datacenter.domain.enumeration.PhoneSpkEnum;
import com.tunnel.business.domain.dataInfo.ExternalSystem;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.dataInfo.SdDevicesProtocol;
import com.tunnel.business.mapper.dataInfo.SdDevicesMapper;
import com.tunnel.business.mapper.event.SdEventMapper;
import com.tunnel.business.mapper.event.SdEventTypeMapper;
import com.tunnel.business.service.dataInfo.IExternalSystemService;
import com.tunnel.business.service.dataInfo.ISdDevicesProtocolService;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.service.dataInfo.ISdTunnelsService;
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
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
    private RestTemplate restTemplate;
    @Autowired
    private SdEventMapper sdEventMapper;
    @Autowired
    private SdEventTypeMapper sdEventTypeMapper;

    /**
     * 从Spring容器中获取设备协议中配置的Class对象
     *
     * @param deviceId
     * @return
     */
    public PhoneSpeak getBeanOfDeviceProtocol(String deviceId) {
        SdDevices device = sdDevicesService.selectSdDevicesById(deviceId);

        String brandId = device.getBrandId();
        String fEqType = device.getfEqType();
        Assert.hasText(brandId, "未设置该设备的品牌");
        Assert.hasText(fEqType, "未设置该设备所属大类");

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
                System.out.println(device);
            }

            // TODO: 2022/12/30 有的device=null，后期排查下
            if (null != device) {
                String data = PhoneSpkEnum.getValue(attribute);
                sdDeviceControlService.updateDeviceData(device, data, itemId);


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
            Assert.notNull(externalSystemId, "未配置所选设备关联的外部系统");
        } else {
            String tunnelId = (String) map.get("tunnelId");
            String direction = (String) map.get("direction");
            Assert.hasText(tunnelId, "隧道ID参数必传");
            Assert.hasText(direction, "隧道方向参数必传");

            SdDevices device = new SdDevices();
            device.setEqTunnelId(tunnelId);
            device.setEqDirection(direction);
            device.setEqType(DevicesTypeEnum.LS.getCode());
            List<SdDevices> spkList = sdDevicesMapper.getSpkList(device);
            Assert.notEmpty(spkList, "该方向隧道未查询到广播设备");

            for (SdDevices devices : spkList) {
                externalSystemId = devices.getExternalSystemId();
                if (null != externalSystemId) {
                    break;
                }
            }
            Assert.notNull(externalSystemId, "该方向隧道的广播设备未配置 关联的外部系统");
        }
        ExternalSystem externalSystem = externalSystemService.selectExternalSystemById(externalSystemId);
        String systemUrl = externalSystem.getSystemUrl();
        Assert.hasText(systemUrl, "未配置该设备所属的外部系统地址");

        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .build();

        String url = systemUrl + "/api/speak/voiceList";
        Request request = new Request.Builder()
                .url(url)
                .build();

        String result = null;
        JSONArray jsonArray = new JSONArray();
        try {
            Response response = client.newCall(request).execute();
            if (response.body() != null) {
                result = response.body().string();
            }
        } catch (IOException e) {
            // e.printStackTrace();
            return AjaxResult.success(jsonArray);
        }

        if (StringUtils.isNotBlank(result)) {
            JSONObject jo = JSONObject.parseObject(result);
            jsonArray = jo.getJSONObject("data").getJSONArray("items");

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

        //参数校验
        Assert.notEmpty(fileList, "未选择音频文件！");
        Assert.notEmpty(spkDeviceIds, "未选择广播设备！");

        /*if ("GSY".equals(deploymentType)) {
            String operIp = (String) map.get("operIp");
            Assert.hasText(operIp, "IP参数{operIp}必传");

            SdTunnels tunnel = null;
            for (String spkDeviceId : spkDeviceIds) {
                SdDevices sdDevices = sdDevicesService.selectSdDevicesById(spkDeviceId);
                tunnel = sdTunnelsService.selectSdTunnelsById(sdDevices.getEqTunnelId());
                if (null != tunnel) {
                    break;
                }
            }
            //设备所属管理站host
            String host = sdOptDeviceService.getGlzHost(String.valueOf(tunnel.getDeptId()));
            //接口地址
            String url = "http://localhost:80000/phoneSpk/playVoice";
            // String url = host + "/phoneSpk/playVoice";

            ResponseEntity<AjaxResult> forEntity = restTemplate.getForEntity(url, AjaxResult.class, uriVariables);
            return forEntity.getBody();
        }*/


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

        ArrayList<Map> spkList = (ArrayList<Map>) map.get("items");
        for (Map spk : spkList) {
            spk.put("hostId", hostId);
            spk.put("hostType", "YeastarHost");
        }

        PhoneSpeak phoneSpeak = null;
        for (String spkDeviceId : spkDeviceIds) {
            phoneSpeak = getBeanOfDeviceProtocol(spkDeviceId);
            if (null != phoneSpeak) {
                break;
            }
        }
        int status = phoneSpeak.playVoice(systemUrl, map);

        return AjaxResult.success(status);
    }

    public AjaxResult playVoiceGroup(@RequestBody Map<String, Object> map) {
        ArrayList fileList = (ArrayList) map.get("fileNames");
        String tunnelId = (String) map.get("tunnelId");
        String direction = (String) map.get("direction");

        Assert.notEmpty(fileList, "未选择音频文件！");
        Assert.hasText(tunnelId, "隧道ID参数必传");
        Assert.hasText(direction, "隧道方向参数必传");

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
        int status = phoneSpeak.playVoice(systemUrl, map);

        return AjaxResult.success(status);
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


        int status = phoneSpeak.hungUp(systemUrl, map);
        return AjaxResult.success(status);
    }


}
