package com.tunnel.deal.light.impl;

import com.alibaba.fastjson.JSONObject;
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
import com.zc.common.core.ThreadPool.ThreadPool;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Component
public class SansiLightImpl implements Light {

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
            OkHttpClient client = new OkHttpClient().newBuilder().build();
            MediaType mediaType=MediaType.parse("application/json");
            RequestBody body=RequestBody.create(mediaType, data);
            Request request=new Request.Builder().url(baseUrl+"/  /core/users/login").method("POST",body).addHeader("Content-Type","application/json")
                    .addHeader("Accept","application/json").build();
            response=client.newCall(request).execute();

        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        JSONObject jo = JSONObject.parseObject(  response.body().toString());
        return  jo.getJSONObject("data").getString("id");
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

        String jessionId = login(externalSystem.getUsername(), externalSystem.getPassword(), baseUrl);

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

    /**
     * 照明灯开关控制
     * @param deviceId
     * @param openClose
     * @return
     */
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
        //获取三思Cookie
        String jessionId = login(externalSystem.getUsername(), externalSystem.getPassword(), baseUrl);

        //灯开关
        int switchType = updateSwitch(jessionId, baseUrl, baseUrl, openClose);
        //亮度
        int brightnessType = 0;
        //2表示关
        if(openClose!=2){
            brightnessType = updateBrightness(jessionId, baseUrl, baseUrl, openClose);
        }
        return switchType==1 && brightnessType==1 ? 1 : 0;
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
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("text/plain");
        //传入的请求参数
        String data = "{\"param\":\"" + openClose + "\"}";
        RequestBody body = RequestBody.create(mediaType, data);

        if (openClose == 2) {
            openClose = 0;
        }
        String url = baseUrl + "api/core/assets/"+step+"/action/device-sensor:switch-status";

        Request request = new Request.Builder()
                .url(url)
                .method("POST", body)
                .addHeader("Content-Type","application/json")
                .addHeader("Authorization", jessionId)
                .build();

        String responseBody = "";
        try {
            Response response = client.newCall(request).execute();
            //包含“发送成功"就可以
            responseBody = response.body().string();
        } catch (IOException e) {
            return 0;
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

        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("text/plain");
        //传入的请求参数
        String data = "{\"param\":\"" + bright + "\"}";
        RequestBody body = RequestBody.create(mediaType, data);

        String url = baseUrl + "api/core/assets/"+step+"/action/device-sensor:dim-level";

        Request request = new Request.Builder()
                .url(url)
                .method("POST", body)
                .addHeader("Content-Type","application/json")
                .addHeader("Authorization", jessionId)
                .build();

        String responseBody = "";
        try {
            Response response = client.newCall(request).execute();
            //包含“发送成功"就可以
            responseBody = response.body().string();
        } catch (IOException e) {
            return 0;
        }
        return responseBody.contains("pipelineId") ? 1 : 0;
    }

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
