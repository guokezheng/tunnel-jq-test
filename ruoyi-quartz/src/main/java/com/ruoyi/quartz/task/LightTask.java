package com.ruoyi.quartz.task;

import com.alibaba.fastjson.JSONObject;
import com.tunnel.business.datacenter.domain.enumeration.DevicesBrandEnum;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeItemEnum;
import com.tunnel.business.datacenter.domain.enumeration.TunnelDirectionEnum;
import com.tunnel.business.datacenter.domain.enumeration.TunnelStepEnum;
import com.tunnel.business.domain.dataInfo.ExternalSystem;
import com.tunnel.business.domain.dataInfo.SdDeviceData;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.service.dataInfo.IExternalSystemService;
import com.tunnel.business.service.dataInfo.ISdDeviceDataService;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.service.dataInfo.ITunnelAssociationService;
import com.tunnel.deal.light.impl.SanJingLight;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component("lightTask")
public class LightTask {

    @Autowired
    private SanJingLight sanJingLight;
    @Autowired
    private ISdDevicesService sdDevicesService;
    @Autowired
    private IExternalSystemService externalSystemService;
    @Autowired
    private ISdDeviceDataService sdDeviceDataService;
    @Autowired
    private ITunnelAssociationService tunnelAssociationService;


    public void syncDeviceData() {
        ExternalSystem system = getExternalSystem();
        String tunnelId = system.getTunnelId();
        Long systemId = system.getId();

        String username = system.getUsername();
        String password = system.getPassword();
        String systemUrl = system.getSystemUrl();
        //获取cookie
        String jessionId = sanJingLight.login(username, password, systemUrl);
        // 上行对应的外部系统隧道洞ID
        String eSystemTunnelId1 = tunnelAssociationService.getExternalSystemTunnelId(tunnelId, TunnelDirectionEnum.UP_DIRECTION.getCode(), systemId);
        // 下行对应的外部系统隧道洞ID
        String eSystemTunnelId2 = tunnelAssociationService.getExternalSystemTunnelId(tunnelId, TunnelDirectionEnum.DOWN_DIRECTION.getCode(), systemId);

        //同步左洞
        // syncLeftDeviceData(tunnelId, systemUrl, eSystemTunnelId2, jessionId);
        //同步右洞
        syncRightDeviceData(tunnelId, systemUrl, eSystemTunnelId1, jessionId);

        // System.out.println(">>>>>>>>>>>>" + DateUtils.getTime());
    }

    public void syncLeftDeviceData(String eqTunnelId, String systemUrl, String eSystemTunnelId, String jessionId) {
        String url = systemUrl + "/api/getLatestDeviceData?tunnelId=" + eSystemTunnelId;

        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .method("GET", null)
                .addHeader("Cookie", jessionId)
                .build();

        try {
            Response response = client.newCall(request).execute();
            String responseBody = response.body().string();
            String direction = TunnelDirectionEnum.DOWN_DIRECTION.getCode();
            if (responseBody.contains("操作成功")) {
                JSONObject data = JSONObject.parseObject(responseBody).getJSONObject("data");
                SdDevices sdDevices = new SdDevices();
                sdDevices.setEqTunnelId(eqTunnelId);
                sdDevices.setEqDirection(direction);

                for (TunnelStepEnum tunnelStepEnum : TunnelStepEnum.values()) {
                    String name = tunnelStepEnum.getName();
                    Integer brightness = data.getInteger(name);

                    if (null != brightness) {
                        sdDevices.setEqType(TunnelStepEnum.getEqType(name));
                        sdDevices.setExternalDeviceId(TunnelStepEnum.getValue(name));
                        SdDevices device = sdDevicesService.getLight(sdDevices);
                        if (device != null) {
                            String desc = tunnelStepEnum.getDesc();
                            Integer itemId = DevicesTypeItemEnum.JQ_LIGHT_BRIGHNESS.getCode();
                            if ("基本段".equals(desc)) {
                                itemId = DevicesTypeItemEnum.JB_LIGHT_BRIGHNESS.getCode();
                            }
                            updateDeviceData(device, String.valueOf(brightness), itemId);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void syncRightDeviceData(String eqTunnelId, String systemUrl, String eSystemTunnelId, String jessionId) {
        String url = systemUrl + "/api/getLatestDeviceData?tunnelId=" + eSystemTunnelId;
        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .method("GET", null)
                .addHeader("Cookie", jessionId)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String responseBody = response.body().string();
            if (responseBody.contains("操作成功")) {
                JSONObject data = JSONObject.parseObject(responseBody).getJSONObject("data");

                SdDevices sdDevices = new SdDevices();
                sdDevices.setEqTunnelId(eqTunnelId);
                sdDevices.setEqDirection(TunnelDirectionEnum.UP_DIRECTION.getCode());

                for (TunnelStepEnum tunnelStepEnum : TunnelStepEnum.values()) {
                    String name = tunnelStepEnum.getName();
                    Integer brightness = data.getInteger(name);
                    if (null != brightness) {
                        sdDevices.setEqType(TunnelStepEnum.getEqType(name));
                        sdDevices.setExternalDeviceId(TunnelStepEnum.getValue(name));
                        SdDevices device = sdDevicesService.getLight(sdDevices);
                        if (device != null) {
                            String desc = tunnelStepEnum.getDesc();
                            Integer itemId = DevicesTypeItemEnum.JQ_LIGHT_BRIGHNESS.getCode();
                            if ("基本段".equals(desc)) {
                                itemId = DevicesTypeItemEnum.JB_LIGHT_BRIGHNESS.getCode();
                            }
                            updateDeviceData(device, String.valueOf(brightness), itemId);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public ExternalSystem getExternalSystem() {
        ExternalSystem system = new ExternalSystem();
        system.setBrandId(DevicesBrandEnum.SAN_JING.getCode());
        List<ExternalSystem> list = externalSystemService.selectExternalSystemList(system);
        if (list.size() > 1) {
            String brandName = DevicesBrandEnum.SAN_JING.getName();
            throw new RuntimeException("存在多个" + brandName + "品牌的外部系统！");
        }

        ExternalSystem externalSystem = list.get(0);
        String tunnelId = externalSystem.getTunnelId();
        String systemName = externalSystem.getSystemName();
        String username = externalSystem.getUsername();
        String password = externalSystem.getPassword();
        String systemUrl = externalSystem.getSystemUrl();
        Assert.hasText(tunnelId, systemName + "未配置所属隧道");
        Assert.hasText(username, systemName + "未配置登录用户名");
        Assert.hasText(password, systemName + "未配置登录密码");
        Assert.hasText(systemUrl, systemName + "未配置系统地址");
        return externalSystem;
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
