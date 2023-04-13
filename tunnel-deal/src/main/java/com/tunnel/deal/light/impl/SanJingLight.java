package com.tunnel.deal.light.impl;

import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.ip.IpUtils;
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
import com.tunnel.deal.light.Light;
import com.tunnel.deal.light.enums.SanjingLightStateEnum;
import com.zc.common.core.ThreadPool.ThreadPool;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class SanJingLight implements Light {

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
        // http://47.119.189.80:8888/login
        String url = baseUrl + "/login?username=" + username + "&password=" + password;
        Request request = new Request.Builder()
                .url(url)
                .method("POST", body)
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        List<String> headers = response.headers("Set-Cookie");
        return headers.get(0);
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
            jessionId = login(externalSystem.getUsername(), externalSystem.getPassword(), baseUrl);
        } catch (Exception e) {
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


        String jessionId = login(externalSystem.getUsername(), externalSystem.getPassword(), baseUrl);
        if(jessionId == null || "".equals(jessionId)){
            return 0;
        }
        //开关对应关系
        openClose = SanjingLightStateEnum.getValue(openClose);

        //开关
        int switchType = updateSwitch(jessionId, baseUrl, externalSystemTunnelId, step, openClose);
        //亮度
        int brightnessType = 0;
        //如果亮度有值并且控制状态不是关，就控制亮度
        if(brightness != null && !Objects.equals(SanjingLightStateEnum.CLOSE.getState(), openClose)){
            brightnessType = updateBrightness(jessionId, baseUrl, externalSystemTunnelId, step ,brightness);
        }
        return switchType==1 && brightnessType==1 ? 1 : 0;
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
        if (openClose == 2) {
            openClose = 0;
        }
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
