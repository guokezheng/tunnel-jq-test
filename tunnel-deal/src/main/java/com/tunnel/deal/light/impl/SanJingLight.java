package com.tunnel.deal.light.impl;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.redis.RedisCache;
import com.tunnel.business.datacenter.domain.enumeration.DeviceControlTypeEnum;
import com.tunnel.business.datacenter.domain.enumeration.DevicesStatusEnum;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeEnum;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeItemEnum;
import com.tunnel.business.domain.dataInfo.ExternalSystem;
import com.tunnel.business.domain.dataInfo.SdDeviceData;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.logRecord.SdOperationLog;
import com.tunnel.business.service.dataInfo.IExternalSystemService;
import com.tunnel.business.service.dataInfo.ISdDeviceDataService;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.service.dataInfo.ITunnelAssociationService;
import com.tunnel.business.service.logRecord.ISdOperationLogService;
import com.tunnel.deal.light.HttpUrlEscapeUtil;
import com.tunnel.deal.light.Light;
import com.tunnel.deal.light.enums.SanjingLightStateEnum;
import com.zc.common.core.ThreadPool.ThreadPool;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Component
public class SanJingLight implements Light {

    private static final Logger logger = LoggerFactory.getLogger(SanJingLight.class);

    @Autowired
    private ISdDevicesService sdDevicesService;

    @Autowired
    private ITunnelAssociationService tunnelAssociationService;

    @Autowired
    private IExternalSystemService externalSystemService;

    @Autowired
    private ISdOperationLogService sdOperationLogService;

    @Autowired
    private ISdDeviceDataService sdDeviceDataService;

    @Autowired
    private RedisCache redisCache;


    /**
     * 登录获取会话ID
     * 用户名/密码默认是：admin/admin123
     *
     * @return
     * @throws IOException
     */
    public String login(String username, String password, String baseUrl) {
//        OkHttpClient client = new OkHttpClient().newBuilder().build();
        //设置超时时间
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(3, TimeUnit.SECONDS)
                .writeTimeout(3, TimeUnit.SECONDS)
                .readTimeout(3, TimeUnit.SECONDS)
                .build();

        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        //特殊字符转义
        password = HttpUrlEscapeUtil.escape(password);
        // http://47.119.189.80:8888/login
        String url = baseUrl + "/login?username=" + username + "&password=" + password;
        Request request = new Request.Builder()
                .url(url)
                .method("POST", body)
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            String responseBody = response.body().string();
            JSONObject jsonObject = JSONObject.parseObject(responseBody);
            String code = jsonObject.getString("code");
//            String msg = jsonObject.getString("msg");
            if(String.valueOf(HttpStatus.ERROR).equals(code)){
                logger.error("获取照明token报错：",responseBody);
            }else{
                List<String> headers = response.headers("Set-Cookie");
                String jessionIdStr = headers.get(0);
                if(jessionIdStr != null && !"".equals(jessionIdStr)){
                    String[] array = jessionIdStr.split(";");
                    return array.length > 0 ? array[0] : "";
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("请求三晶照明token报错：",e.getMessage());
            return "";
        }

        return "";
    }

    @Override
    public int setBrightness(String deviceId, Integer bright) {
        SdDevices device = sdDevicesService.selectSdDevicesById(deviceId);

        String eqTunnelId = device.getEqTunnelId();
        String eqDirection = device.getEqDirection();
        Long externalSystemId = device.getExternalSystemId();
        String step = device.getExternalDeviceId();
        Assert.hasText(eqTunnelId, "未配置该设备所属隧道");
        Assert.hasText(eqDirection, "未配置该设备所属方向");
        Assert.notNull(externalSystemId, "未配置该设备关联的外部系统");
        // Assert.hasText(step, "未配置该设备关联的段号");
        Assert.hasText(step, "该设备暂时不可控！");

        //确定隧道洞编号
        String externalSystemTunnelId = tunnelAssociationService.getExternalSystemTunnelId(eqTunnelId, eqDirection, externalSystemId);
        Assert.hasText(externalSystemTunnelId, "未配置该设备关联的隧道洞编号");

        ExternalSystem externalSystem = externalSystemService.selectExternalSystemById(externalSystemId);
        String baseUrl = externalSystem.getSystemUrl();
        Assert.hasText(baseUrl, "未配置该设备所属的外部系统地址");
        String jessionId;
        try {
            String tokenKey = "control:sanJingLighttoken";
            jessionId = redisCache.getCacheObject(tokenKey);
            if(jessionId == null){
                jessionId = login(externalSystem.getUsername(), externalSystem.getPassword(), baseUrl);
                redisCache.setCacheObject(tokenKey,jessionId);
                redisCache.expire(tokenKey,15*60);
            }
        } catch (Exception e) {
            logger.info("获取token异常，请联系管理员。");
            return 0;
        }
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        // 示例 "tunnelId=2&step=0&bright=98"
        String url = baseUrl + "/api/adjustBrightness";
        String content = "tunnelId=" + externalSystemTunnelId + "&step=" + step + "&bright=" + bright;
        RequestBody body = RequestBody.create(mediaType, content);
        Request request = new Request.Builder()
                .url(url)
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Cookie", jessionId)
                .build();

        String responseBody = "";
        try {
            Response response = client.newCall(request).execute();
            //包含“发送成功"就可以
            responseBody = response.body().string();
        } catch (IOException e) {
            logger.info("加强照明调光功能异常，请联系管理员。");
            return 0;
        }
        return responseBody.contains("发送成功") ? 1 : 0;
    }

    @Override
    public int lineControl(String deviceId, Integer openClose, Integer brightness) {
        SdDevices device = sdDevicesService.selectSdDevicesById(deviceId);

        String eqTunnelId = device.getEqTunnelId();
        String eqDirection = device.getEqDirection();
        Long externalSystemId = device.getExternalSystemId();
        String step = device.getExternalDeviceId();
        Assert.hasText(eqTunnelId, "未配置该设备所属隧道");
        Assert.hasText(eqDirection, "未配置该设备所属方向");
        Assert.notNull(externalSystemId, "未配置该设备关联的外部系统");
        Assert.hasText(step, "未配置该设备关联的段号");

        //确定隧道洞编号
        String externalSystemTunnelId = tunnelAssociationService.getExternalSystemTunnelId(eqTunnelId, eqDirection, externalSystemId);
        Assert.hasText(externalSystemTunnelId, "未配置该设备关联的隧道洞编号");

        ExternalSystem externalSystem = externalSystemService.selectExternalSystemById(externalSystemId);
        String baseUrl = externalSystem.getSystemUrl();
        Assert.hasText(baseUrl, "未配置该设备所属的外部系统地址");


//        String jessionId = login(externalSystem.getUsername(), externalSystem.getPassword(), baseUrl);
        String jessionId;

        String tokenKey = "control:sanJingLighttoken";

        jessionId = redisCache.getCacheObject(tokenKey);

        if(jessionId == null){
            jessionId =  login(externalSystem.getUsername(), externalSystem.getPassword(), baseUrl);
            redisCache.setCacheObject(tokenKey,jessionId);
            redisCache.expire(tokenKey,15*60);
        }

        if(jessionId == null || "".equals(jessionId)){
            return 0;
        }
        //开关对应关系
        openClose = SanjingLightStateEnum.getValue(openClose);

        //开关
        int switchType = updateSwitch(jessionId, baseUrl, externalSystemTunnelId, step, openClose);
        //亮度
        int brightnessType = 1;
        //如果亮度有值并且控制状态不是关，就控制亮度
        if(brightness != null && !Objects.equals(SanjingLightStateEnum.CLOSE.getState(), openClose)){
            brightnessType = updateBrightness(jessionId, baseUrl, externalSystemTunnelId, step ,brightness);
        }
        return switchType==1 && brightnessType==1 ? 1 : 0;
    }


    public int lineControlAddLog(String deviceId, Integer openClose, Integer brightness) {
        SdDevices device = sdDevicesService.selectSdDevicesById(deviceId);
        int resultStatus = lineControl(deviceId, openClose,brightness);
        // 如果控制成功
        if (resultStatus == 1) {
            //更新设备状态
            device.setEqStatus("1");
            device.setEqStatusTime(new Date());
            sdDevicesService.updateSdDevices(device);
            //更新设备实时数据
            updateDeviceData(device, String.valueOf(openClose), DevicesTypeItemEnum.JQ_LIGHT_OPENCLOSE.getCode());
        }

        //添加操作日志
        SdOperationLog sdOperationLog = new SdOperationLog();
        sdOperationLog.setEqTypeId(device.getEqType());
        sdOperationLog.setTunnelId(device.getEqTunnelId());
        sdOperationLog.setEqId(device.getEqId());
        sdOperationLog.setOperationState(String.valueOf(openClose));
        sdOperationLog.setControlType(DeviceControlTypeEnum.AUTO_EXEC.getCode());
        sdOperationLog.setCreateTime(new Date());
        try {
            sdOperationLog.setOperIp(InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        sdOperationLog.setState(String.valueOf(resultStatus));
        // 确定设备之前的开关状态
        SdDeviceData sdDeviceData = new SdDeviceData();
        sdDeviceData.setDeviceId(deviceId);


        if (String.valueOf(DevicesTypeEnum.JIA_QIANG_ZHAO_MING.getCode()).equals(device.getEqType())) {
            sdDeviceData.setItemId(Long.valueOf(DevicesTypeItemEnum.JQ_LIGHT_OPENCLOSE.getCode()));
        } else if (String.valueOf(DevicesTypeEnum.JI_BEN_ZHAO_MING.getCode()).equals(device.getEqType())) {
            sdDeviceData.setItemId(Long.valueOf(DevicesTypeItemEnum.JI_BEN_ZHAO_MING_OPENCLOSE.getCode()));
        }
        List<SdDeviceData> sdDeviceDataList = sdDeviceDataService.selectSdDeviceDataList(sdDeviceData);
        if (brightness != null && null != sdDeviceDataList && sdDeviceDataList.size() > 0) {
            sdOperationLog.setBeforeState(sdDeviceDataList.get(0).getData());
        }
        sdOperationLogService.insertSdOperationLog(sdOperationLog);
        return resultStatus;
    }

    /**
     * 批量控制等亮度方法
     * @param deviceIds  eqId 设备ID
     * @param bright 亮度值
     * @param controlType 操作方式
     * @param operIp IP地址
     */
    @Override
    public void setBrightnessByList(List<String> deviceIds, Integer bright, String controlType, String operIp) {
        for (String deviceId:deviceIds ) {
            ThreadPool.executor.execute(()->{
                SdDevices device = sdDevicesService.selectSdDevicesById(deviceId);
                int resultStatus = setBrightness(deviceId,bright);
                // 如果控制成功
                if (resultStatus == 1) {
                    // 更新设备在线状态
                    device.setEqStatus("1");
                    device.setEqStatusTime(new Date());
                    sdDevicesService.updateSdDevices(device);
                    //更新设备实时数据
                    updateDeviceData(device, String.valueOf(bright), DevicesTypeItemEnum.JQ_LIGHT_BRIGHNESS.getCode());
                }
                //添加操作日志
                SdOperationLog sdOperationLog = new SdOperationLog();
                sdOperationLog.setEqTypeId(device.getEqType());
                sdOperationLog.setTunnelId(device.getEqTunnelId());
                sdOperationLog.setEqId(device.getEqId());
                sdOperationLog.setOperationState(String.valueOf(bright));
                sdOperationLog.setControlType(controlType);
                sdOperationLog.setCreateTime(new Date());
                sdOperationLog.setOperIp(operIp);
                sdOperationLog.setState(String.valueOf(resultStatus));
                // 确定设备之前亮度值
                SdDeviceData sdDeviceData = new SdDeviceData();
                sdDeviceData.setDeviceId(deviceId);
                sdDeviceData.setItemId(Long.valueOf(DevicesTypeItemEnum.JQ_LIGHT_BRIGHNESS.getCode()));
                List<SdDeviceData> sdDeviceDataList = sdDeviceDataService.selectSdDeviceDataList(sdDeviceData);
                if (null != sdDeviceDataList && sdDeviceDataList.size() > 0) {
                    sdOperationLog.setBeforeState(sdDeviceDataList.get(0).getData());
                }
                sdOperationLogService.insertSdOperationLog(sdOperationLog);
            });
        }
    }

    /**
     * 控制灯开关方法
     * @param jessionId cookit
     * @param baseUrl 外部系统地址
     * @param externalSystemTunnelId  隧道洞编号
     * @param step 关联的段号
     * @param openClose 开关
     * @return
     */
    public int updateSwitch(String jessionId ,String baseUrl ,String externalSystemTunnelId, String step , Integer openClose) {
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
//        if (openClose == 2) {
//            openClose = 0;
//        }
        String url = baseUrl + "/api/lineControl?tunnelId=" + externalSystemTunnelId + "&step=" + step + "&openClose=" + openClose;
        // String url = lineControl + "?tunnelId=" + externalSystemTunnelId + "&step=" + step + "&openClose=" + openClose;
        Request request = new Request.Builder()
                .url(url)
                .method("POST", body)
                .addHeader("Cookie", jessionId)
                .build();
        //调用开关返回参数
        String responseBody = "";
        //调用调节亮度返回参数
        String responseLuminanceBody = "";

        try {
            Response response = client.newCall(request).execute();
            //包含“发送成功"就可以
            responseBody = response.body().string();
        } catch (IOException e) {
            return 0;
        }
        return responseBody.contains("发送成功") ? 1 : 0;
    }

    /**
     * 控制灯亮度方法
     * @param jessionId  cookit
     * @param baseUrl 外部系统地址
     * @param deviceId 隧道洞编号
     * @param step 关联的段号
     * @param bright 亮度
     * @return
     */
    public int updateBrightness(String jessionId ,String baseUrl ,String deviceId,String step , Integer bright) {

        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        // 示例 "tunnelId=2&step=0&bright=98"
        String url = baseUrl + "/api/adjustBrightness";
        String content = "tunnelId=" + deviceId + "&step=" + step + "&bright=" + bright;
        RequestBody body = RequestBody.create(mediaType, content);
        Request request = new Request.Builder()
                .url(url)
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Cookie", jessionId)
                .build();

        String responseBody = "";
        try {
            Response response = client.newCall(request).execute();
            //包含“发送成功"就可以
            responseBody = response.body().string();
        } catch (IOException e) {
            return 0;
        }
        return responseBody.contains("发送成功") ? 1 : 0;
    }


    /**
     *
     * @param deviceIds         设备集合
     * @param oldLuminanceRange         原有亮度值
     * @param luminanceRange            亮度值
     * @param controlType       控制方式     3：手动 1：时间控制 2：光强控制
     * @param operIp            操作地址
     */
    public void setBrightnessByList(List<SdDevices> deviceIds, Integer oldLuminanceRange, Integer luminanceRange, String controlType, String operIp) {
        for (SdDevices device:deviceIds ) {
            int resultStatus = 0;
            try{
                resultStatus = setBrightness(device.getEqId(),luminanceRange);
            }catch (Exception e){
                resultStatus = 0;
            }
            // 如果控制成功
            if (resultStatus == 1) {
                // 更新设备在线状态
                device.setEqStatus("1");
                device.setEqStatusTime(new Date());
                sdDevicesService.updateSdDevices(device);
                //更新设备实时数据
                updateDeviceData(device, String.valueOf(luminanceRange), DevicesTypeItemEnum.JQ_LIGHT_BRIGHNESS.getCode());
                redisCache.setCacheObject("control:"+device.getEqTunnelId()+"_"+device.getEqId()+"_LuminanceRange",luminanceRange);
            }else{
                redisCache.deleteObject("control:"+device.getEqTunnelId()+"_"+device.getEqId()+"_LuminanceRange");
            }
            //添加操作日志
            SdOperationLog sdOperationLog = new SdOperationLog();
            sdOperationLog.setEqTypeId(device.getEqType());
            sdOperationLog.setTunnelId(device.getEqTunnelId());
            sdOperationLog.setEqId(device.getEqId());
            sdOperationLog.setOperationState(String.valueOf(luminanceRange));
            sdOperationLog.setControlType(controlType);
            sdOperationLog.setCreateTime(new Date());
            try {
                sdOperationLog.setOperIp(InetAddress.getLocalHost().getHostAddress());
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            sdOperationLog.setState(String.valueOf(resultStatus));
            sdOperationLog.setBeforeState(device.getData());
            sdOperationLogService.insertSdOperationLog(sdOperationLog);
        }
    }


    /**
     * 单个设备调光
     * @param device
     * @param luminanceRange
     * @param controlType
     * @return
     */
    public int setBrightnessByDevice(SdDevices device,  Integer luminanceRange, String controlType) {
        int resultStatus;
        try{
            resultStatus = setBrightness(device.getEqId(),luminanceRange);
        }catch (Exception e){
            resultStatus = 0;
        }
        // 如果控制成功
        if (resultStatus == 1) {
            // 更新设备在线状态
            device.setEqStatus("1");
            device.setEqStatusTime(new Date());
            sdDevicesService.updateSdDevices(device);
            //更新设备实时数据
            updateDeviceData(device, String.valueOf(luminanceRange), DevicesTypeItemEnum.JQ_LIGHT_BRIGHNESS.getCode());
            //更新redis缓存
            String redisLuminanceRangeKey = "control:"+device.getEqId()+"_LuminanceRange";
            redisCache.setCacheObject(redisLuminanceRangeKey,luminanceRange);
        }
        //添加操作日志
        SdOperationLog sdOperationLog = new SdOperationLog();
        sdOperationLog.setEqTypeId(device.getEqType());
        sdOperationLog.setTunnelId(device.getEqTunnelId());
        sdOperationLog.setEqId(device.getEqId());
        sdOperationLog.setOperationState(String.valueOf(luminanceRange));
        sdOperationLog.setControlType(controlType);
        sdOperationLog.setCreateTime(new Date());
        try {
            sdOperationLog.setOperIp(InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        sdOperationLog.setState(String.valueOf(resultStatus));
        sdOperationLog.setBeforeState(device.getData());
        sdOperationLogService.insertSdOperationLog(sdOperationLog);
        return resultStatus;
    }


    /**
     * 单个设备调光
     * @param device
     * @param nowLuminanceRange    原有亮度
     * @param luminanceRange    当前亮度
     * @param controlType
     * @return
     */
    public int setBrightnessByDevice(SdDevices device,Integer nowLuminanceRange ,Integer luminanceRange, String controlType) {
        int resultStatus;
        try{
//            resultStatus = setBrightness(device.getEqId(),luminanceRange);
            resultStatus = 1;
        }catch (Exception e){
            resultStatus = 0;
        }
        // 如果控制成功
        if (resultStatus == 1) {
            // 更新设备在线状态
            device.setEqStatus(DevicesStatusEnum.DEVICE_ON_LINE.getCode());
            device.setEqStatusTime(new Date());
            sdDevicesService.updateSdDevices(device);
            //更新设备实时数据
            updateDeviceData(device, String.valueOf(luminanceRange), DevicesTypeItemEnum.JQ_LIGHT_BRIGHNESS.getCode());
            //更新redis缓存
            String redisLuminanceRangeKey = "control:"+device.getEqId()+"_LuminanceRange";
            redisCache.setCacheObject(redisLuminanceRangeKey,luminanceRange);
        }
        //添加操作日志
        SdOperationLog sdOperationLog = new SdOperationLog();
        sdOperationLog.setEqTypeId(device.getEqType());
        sdOperationLog.setTunnelId(device.getEqTunnelId());
        sdOperationLog.setEqId(device.getEqId());
        sdOperationLog.setOperationState(String.valueOf(luminanceRange));
        sdOperationLog.setControlType(controlType);
        sdOperationLog.setCreateTime(new Date());
        try {
            sdOperationLog.setOperIp(InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        sdOperationLog.setState(String.valueOf(resultStatus));
        sdOperationLog.setBeforeState(nowLuminanceRange.toString());
        sdOperationLogService.insertSdOperationLog(sdOperationLog);
        return resultStatus;
    }

    public void updateDeviceData(SdDevices sdDevices, String value, Integer itemId) {
        SdDeviceData sdDeviceData = new SdDeviceData();
        sdDeviceData.setDeviceId(sdDevices.getEqId());
        sdDeviceData.setItemId(Long.valueOf(itemId));
        List<SdDeviceData> deviceData = sdDeviceDataService.selectSdDeviceDataList(sdDeviceData);
        if (deviceData.size() > 0) {
            SdDeviceData data = deviceData.get(0);
            data.setData(value);
            data.setUpdateTime(new Date());
            sdDeviceDataService.updateSdDeviceData(data);
        } else {
            sdDeviceData.setData(value);
            sdDeviceData.setCreateTime(new Date());
            sdDeviceDataService.insertSdDeviceData(sdDeviceData);
        }
    }
}
