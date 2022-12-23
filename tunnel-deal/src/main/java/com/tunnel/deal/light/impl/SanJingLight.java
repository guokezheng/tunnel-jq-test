package com.tunnel.deal.light.impl;

import com.ruoyi.system.service.ISysDictDataService;
import com.tunnel.business.datacenter.domain.enumeration.DictTypeEnum;
import com.tunnel.business.domain.dataInfo.ExternalSystem;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.dataInfo.SdTunnels;
import com.tunnel.business.domain.dataInfo.TunnelAssociation;
import com.tunnel.business.service.dataInfo.IExternalSystemService;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.service.dataInfo.ISdTunnelsService;
import com.tunnel.business.service.dataInfo.ITunnelAssociationService;
import com.tunnel.deal.light.Light;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.List;

@Component
public class SanJingLight implements Light {

    private String jessionId;

    @Value("${device.light.sanjing.login}")
    private String login;

    @Value("${device.light.sanjing.brightness}")
    private String brightness;

    @Value("${device.light.sanjing.lineControl}")
    private String lineControl;

    @Autowired
    private ISdDevicesService sdDevicesService;

    @Autowired
    private ITunnelAssociationService tunnelAssociationService;

    @Autowired
    private IExternalSystemService externalSystemService;

    @Autowired
    private ISdTunnelsService sdTunnelsService;

    @Autowired
    private ISysDictDataService sysDictDataService;

    /**
     * 登录获取会话ID
     * 用户名/密码默认是：admin/admin123
     *
     * @return
     * @throws IOException
     */
    public String login(String username, String password, String baseUrl) {
        if (null != jessionId) {
            return jessionId;
        }

        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        // String url = login + "?username=" + username + "&password=" + password;
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
        }
        List<String> headers = response.headers("Set-Cookie");
        jessionId = headers.get(0);
        return headers.get(0);
    }

    public String getExternalSystemTunnelId(String eqTunnelId, String eqDirection, Long externalSystemId) {
        TunnelAssociation association = new TunnelAssociation();
        association.setTunnelId(eqTunnelId);
        association.setTunnelDirection(eqDirection);
        association.setExternalSystemId(externalSystemId);

        SdTunnels sdTunnels = sdTunnelsService.selectSdTunnelsById(eqTunnelId);
        String directionName = sysDictDataService.selectDictLabel(DictTypeEnum.sd_direction.getCode(), eqDirection);
        ExternalSystem externalSystem = externalSystemService.selectExternalSystemById(externalSystemId);
        String msg = "【" + sdTunnels.getTunnelName() + "】 未查询到 方向为【" + directionName + "】系统名称为 【" + externalSystem.getSystemName() + "】的关联配置数据,请联系管理员";

        List<TunnelAssociation> associationList = tunnelAssociationService.selectTunnelAssociationList(association);
        Assert.notEmpty(associationList, msg);

        TunnelAssociation tunnelAssociation = associationList.get(0);
        return tunnelAssociation.getExternalSystemTunnelId();
    }


    @Override
    public int setBrightness(String deviceId, Integer bright) {
        SdDevices device = sdDevicesService.selectSdDevicesById(deviceId);

        String eqTunnelId = device.getEqTunnelId();
        String eqDirection = device.getEqDirection();
        Long externalSystemId = device.getExternalSystemId();
        String step = device.getExternalDeviceId();
        Assert.hasText(eqTunnelId, "未配置该设备所属隧道，请联系管理员！");
        Assert.hasText(eqDirection, "未配置该设备所属方向，请联系管理员！");
        Assert.notNull(externalSystemId, "未配置该设备关联的外部系统，请联系管理员！");
        Assert.hasText(step, "未配置该设备关联的段号，请联系管理员！");

        //确定隧道洞编号
        String externalSystemTunnelId = getExternalSystemTunnelId(eqTunnelId, eqDirection, externalSystemId);
        Assert.hasText(externalSystemTunnelId, "未配置该设备关联的隧道洞编号，请联系管理员！");

        //确定段号
        // String eqName = device.getEqName();
        // Integer step = TunnelStepEnum.getValue(eqName);

        ExternalSystem externalSystem = externalSystemService.selectExternalSystemById(externalSystemId);
        String baseUrl = externalSystem.getSystemUrl();
        Assert.hasText(baseUrl, "未配置该设备所属的外部系统地址，请联系管理员！");

        login(externalSystem.getUsername(), externalSystem.getPassword(), baseUrl);

        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        // 示例 "tunnelId=1&step=0&bright=98"
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
            e.printStackTrace();
            return 0;
        }
        return responseBody.contains("发送成功") ? 1 : 0;
    }

    @Override
    public int lineControl(String deviceId, Integer openClose) {
        SdDevices device = sdDevicesService.selectSdDevicesById(deviceId);

        String eqTunnelId = device.getEqTunnelId();
        String eqDirection = device.getEqDirection();
        Long externalSystemId = device.getExternalSystemId();
        String step = device.getExternalDeviceId();
        Assert.hasText(eqTunnelId, "未配置该设备所属隧道，请联系管理员！");
        Assert.hasText(eqDirection, "未配置该设备所属方向，请联系管理员！");
        Assert.notNull(externalSystemId, "未配置该设备关联的外部系统，请联系管理员！");
        Assert.hasText(step, "未配置该设备关联的段号，请联系管理员！");

        //确定隧道洞编号
        String externalSystemTunnelId = getExternalSystemTunnelId(eqTunnelId, eqDirection, externalSystemId);
        Assert.hasText(externalSystemTunnelId, "未配置该设备关联的隧道洞编号，请联系管理员！");

        ExternalSystem externalSystem = externalSystemService.selectExternalSystemById(externalSystemId);
        String baseUrl = externalSystem.getSystemUrl();
        Assert.hasText(baseUrl, "未配置该设备所属的外部系统地址，请联系管理员！");

        login(externalSystem.getUsername(), externalSystem.getPassword(), baseUrl);

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

        String responseBody = "";
        try {
            Response response = client.newCall(request).execute();
            //包含“发送成功"就可以
            responseBody = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
        return responseBody.contains("发送成功") ? 1 : 0;
    }
}
