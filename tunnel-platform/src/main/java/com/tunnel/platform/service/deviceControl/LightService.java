package com.tunnel.platform.service.deviceControl;

import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeItemEnum;
import com.tunnel.business.domain.dataInfo.SdDeviceData;
import com.tunnel.business.domain.dataInfo.SdDeviceTypeItem;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.dataInfo.SdDevicesProtocol;
import com.tunnel.business.domain.logRecord.SdOperationLog;
import com.tunnel.business.service.dataInfo.ISdDeviceDataService;
import com.tunnel.business.service.dataInfo.ISdDeviceTypeItemService;
import com.tunnel.business.service.dataInfo.ISdDevicesProtocolService;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.service.logRecord.ISdOperationLogService;
import com.tunnel.business.utils.util.SpringContextUtils;
import com.tunnel.deal.light.Light;
import com.tunnel.platform.service.SdDeviceControlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class LightService {
    @Autowired
    private ISdOperationLogService sdOperationLogService;
    @Autowired
    private ISdDeviceDataService sdDeviceDataService;
    @Autowired
    private ISdDeviceTypeItemService sdDeviceTypeItemService;
    @Autowired
    private SdDeviceControlService sdDeviceControlService;
    @Autowired
    private ISdDevicesService sdDevicesService;
    @Autowired
    private ISdDevicesProtocolService sdDevicesProtocolService;


    /**
     * 从Spring容器中获取设备协议中配置的Class对象
     *
     * @param deviceId
     * @return
     */
    public Light getBeanOfDeviceProtocol(String deviceId) {
        SdDevices device = sdDevicesService.selectSdDevicesById(deviceId);

        String brandId = device.getBrandId();
        String fEqType = device.getfEqType();
        Assert.hasText(brandId, "未设置该设备的品牌，请联系管理员！");
        Assert.hasText(fEqType, "未设置该设备所属大类，请联系管理员！");

        SdDevicesProtocol protocol = new SdDevicesProtocol();
        protocol.setBrandId(brandId);
        protocol.setEqType(fEqType);
        List<SdDevicesProtocol> protocolList = sdDevicesProtocolService.selectSdDevicesProtocolList(protocol);
        Assert.notEmpty(protocolList, "未查询到该设备的相关协议配置，请联系管理员");

        SdDevicesProtocol sdDevicesProtocol = protocolList.get(0);
        String className = sdDevicesProtocol.getClassName();

        Light light = null;
        try {
            Class<?> aClass = Class.forName(className);
            Object object = aClass.newInstance();
            if (object instanceof Light) {
                light = (Light) SpringContextUtils.getBean(object.getClass());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return light;
    }


    public int setBrightness(String deviceId, Integer bright, String controlType, String operIp) {
        SdDevices device = sdDevicesService.selectSdDevicesById(deviceId);

        Light light = getBeanOfDeviceProtocol(deviceId);
        int resultStatus = light.setBrightness(deviceId, bright);

        // 更新设备在线状态
        device.setEqStatus("1");
        device.setEqStatusTime(new Date());
        sdDevicesService.updateSdDevices(device);

        //更新设备实时数据
        SdDeviceTypeItem sdDeviceTypeItem = new SdDeviceTypeItem();
        sdDeviceTypeItem.setDeviceTypeId(device.getEqType());
        List<SdDeviceTypeItem> sdDeviceTypeItems = sdDeviceTypeItemService.selectSdDeviceTypeItemList(sdDeviceTypeItem);
        Assert.notEmpty(sdDeviceTypeItems, "当前设备没有设备类型数据项数据，请添加后重试！");
        SdDeviceTypeItem typeItem = sdDeviceTypeItems.get(0);
        sdDeviceControlService.updateDeviceData(device, String.valueOf(bright), Integer.parseInt(typeItem.getId().toString()));

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
        sdDeviceData.setItemId(Long.valueOf(DevicesTypeItemEnum.JACK_LIGHT_BRIGHNESS.getCode()));
        List<SdDeviceData> sdDeviceDataList = sdDeviceDataService.selectSdDeviceDataList(sdDeviceData);
        if (null != sdDeviceDataList && sdDeviceDataList.size() > 0) {
            sdOperationLog.setBeforeState(sdDeviceDataList.get(0).getData());
        }

        sdOperationLogService.insertSdOperationLog(sdOperationLog);

        return resultStatus;
    }


    /**
     * @param deviceId    设备ID
     * @param openClose   状态（1-开，0-关）
     * @param controlType 控制类型（0-手动控制,1-定时控制,4-预案执行）
     * @return 控制结果 1-成功，0-失败
     */
    public int lineControl(String deviceId, Integer openClose, String controlType, String operIp) {
        SdDevices device = sdDevicesService.selectSdDevicesById(deviceId);

        Light light = getBeanOfDeviceProtocol(deviceId);
        int resultStatus = light.lineControl(deviceId, openClose);

        //更新设备状态
        device.setEqStatus("1");
        device.setEqStatusTime(new Date());
        sdDevicesService.updateSdDevices(device);

        //更新设备实时数据
        SdDeviceTypeItem sdDeviceTypeItem = new SdDeviceTypeItem();
        sdDeviceTypeItem.setDeviceTypeId(device.getEqType());
        List<SdDeviceTypeItem> sdDeviceTypeItems = sdDeviceTypeItemService.selectSdDeviceTypeItemList(sdDeviceTypeItem);
        Assert.notEmpty(sdDeviceTypeItems, "当前设备没有设备类型数据项数据，请添加后重试！");
        SdDeviceTypeItem typeItem = sdDeviceTypeItems.get(0);
        sdDeviceControlService.updateDeviceData(device, String.valueOf(openClose), Integer.parseInt(typeItem.getId().toString()));

        //添加操作日志
        SdOperationLog sdOperationLog = new SdOperationLog();
        sdOperationLog.setEqTypeId(device.getEqType());
        sdOperationLog.setTunnelId(device.getEqTunnelId());
        sdOperationLog.setEqId(device.getEqId());
        sdOperationLog.setOperationState(String.valueOf(openClose));
        sdOperationLog.setControlType(controlType);
        sdOperationLog.setCreateTime(new Date());
        sdOperationLog.setOperIp(operIp);
        sdOperationLog.setState(String.valueOf(resultStatus));
        // 确定设备之前的开关状态
        SdDeviceData sdDeviceData = new SdDeviceData();
        sdDeviceData.setDeviceId(deviceId);
        sdDeviceData.setItemId(Long.valueOf(DevicesTypeItemEnum.JACK_LIGHT_OPENCLOSE.getCode()));
        List<SdDeviceData> sdDeviceDataList = sdDeviceDataService.selectSdDeviceDataList(sdDeviceData);
        if (null != sdDeviceDataList && sdDeviceDataList.size() > 0) {
            sdOperationLog.setBeforeState(sdDeviceDataList.get(0).getData());
        }
        sdOperationLogService.insertSdOperationLog(sdOperationLog);

        return resultStatus;
    }

    /**
     * @param deviceId  设备ID
     * @param openClose 状态（1-开，0-关）
     * @return 控制结果 1-成功，0-失败
     */
    public int lineControl(String deviceId, Integer openClose) {
        Light light = getBeanOfDeviceProtocol(deviceId);
        int resultStatus = light.lineControl(deviceId, openClose);
        return resultStatus;
    }


    public String getLatestDeviceData(String deviceId) throws IOException {

       /* String externalSystemTunnelId = getExternalSystemTunnelId(deviceId);


        login(username, password);

        String url = deviceData + "?tunnelId=" + externalSystemTunnelId;
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        Request request = new Request.Builder()
                .url(url)
                .method("GET", null)
                .addHeader("Cookie", jessionId)
                .build();
        Response response = client.newCall(request).execute();

        String responseBoby = response.body().string();*/

        return null;
    }

}
