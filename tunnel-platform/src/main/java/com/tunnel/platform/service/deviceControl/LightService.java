package com.tunnel.platform.service.deviceControl;

import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeEnum;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeItemEnum;
import com.tunnel.business.domain.dataInfo.SdDeviceData;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.dataInfo.SdDevicesProtocol;
import com.tunnel.business.domain.logRecord.SdOperationLog;
import com.tunnel.business.service.dataInfo.ISdDeviceDataService;
import com.tunnel.business.service.dataInfo.ISdDevicesProtocolService;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.service.logRecord.ISdOperationLogService;
import com.tunnel.business.utils.util.SpringContextUtils;
import com.tunnel.deal.light.Light;
import com.tunnel.deal.light.SansiLight;
import com.tunnel.deal.light.impl.SansiLightImpl;
import com.tunnel.platform.service.SdDeviceControlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class LightService {

    private static final Logger logger = LoggerFactory.getLogger(LightService.class);

    @Autowired
    private ISdOperationLogService sdOperationLogService;
    @Autowired
    private ISdDeviceDataService sdDeviceDataService;
    @Autowired
    private SdDeviceControlService sdDeviceControlService;
    @Autowired
    private ISdDevicesService sdDevicesService;
    @Autowired
    private ISdDevicesProtocolService sdDevicesProtocolService;

    /**
     * 获取该设备classname地址
     *
     * @param deviceId
     * @return
     */
    public String getSdDevicesProtocolStrl(String deviceId){
        SdDevices device = sdDevicesService.selectSdDevicesById(deviceId);

        String brandId = device.getBrandId();
        Long fEqType = device.getfEqType();
        Assert.hasText(brandId, "未设置该设备的品牌，请联系管理员！");
        Assert.notNull(fEqType, "未设置该设备所属大类，请联系管理员！");

        SdDevicesProtocol protocol = new SdDevicesProtocol();
        protocol.setBrandId(brandId);
        protocol.setEqType(fEqType);
        List<SdDevicesProtocol> protocolList = sdDevicesProtocolService.selectSdDevicesProtocolList(protocol);
        Assert.notEmpty(protocolList, "未查询到该设备的相关协议配置，请联系管理员");

        SdDevicesProtocol sdDevicesProtocol = protocolList.get(0);
        String className = sdDevicesProtocol.getClassName();
        return className ;
    }

    /**
     * 从Spring容器中获取设备协议中配置的Class对象
     *
     * @param className
     * @return
     */
    public Light getBeanOfDeviceProtocol(String className) {
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
    /**
     * 从Spring容器中获取设备协议中配置的Class对象
     *
     * @param className
     * @return
     */
    public SansiLight getBeanOfSansiLightImpl(String className) {
        SansiLight light = null;
        try {
            Class<?> aClass = Class.forName(className);
            Object object = aClass.newInstance();
            if (object instanceof SansiLight) {
                light = (SansiLightImpl) SpringContextUtils.getBean(object.getClass());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return light;
    }

    /**
     * @param deviceId    设备ID
     * @param bright      亮度
     * @param controlType 控制类型
     * @param operIp      操作者IP地址
     * @return
     */
    public int setBrightness(String deviceId, Integer bright, String controlType, String operIp) {
        SdDevices device = sdDevicesService.selectSdDevicesById(deviceId);

        Light light = getBeanOfDeviceProtocol(deviceId);
        int resultStatus = light.setBrightness(deviceId, bright);

        // 如果控制成功
        if (resultStatus == 1) {
            // 更新设备在线状态
            device.setEqStatus("1");
            device.setEqStatusTime(new Date());
            sdDevicesService.updateSdDevices(device);

            //更新设备实时数据
            sdDeviceControlService.updateDeviceData(device, String.valueOf(bright), DevicesTypeItemEnum.JQ_LIGHT_BRIGHNESS.getCode());
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
        int resultStatus = light.lineControl(deviceId, openClose,null);
        // 如果控制成功
        if (resultStatus == 1) {
            //更新设备状态
            device.setEqStatus("1");
            device.setEqStatusTime(new Date());
            sdDevicesService.updateSdDevices(device);

            //更新设备实时数据
            sdDeviceControlService.updateDeviceData(device, String.valueOf(openClose), DevicesTypeItemEnum.JQ_LIGHT_OPENCLOSE.getCode());
        }

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


        if (String.valueOf(DevicesTypeEnum.JIA_QIANG_ZHAO_MING.getCode()).equals(device.getEqType())) {
            sdDeviceData.setItemId(Long.valueOf(DevicesTypeItemEnum.JQ_LIGHT_OPENCLOSE.getCode()));
        } else if (String.valueOf(DevicesTypeEnum.JI_BEN_ZHAO_MING.getCode()).equals(device.getEqType())) {
            sdDeviceData.setItemId(Long.valueOf(DevicesTypeItemEnum.JI_BEN_ZHAO_MING_OPENCLOSE.getCode()));
        }
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
     *   @param brightness 亮度
     * @return 控制结果 1-成功，0-失败
     */
    public int lineControl(String deviceId, Integer openClose,Integer brightness) {
        int resultStatus = 0;
        //获取该设备classname地址
        String className = getSdDevicesProtocolStrl(deviceId);
        if(className.contains("SanJingLight")){
            Light light = getBeanOfDeviceProtocol(className);
            resultStatus = light.lineControl(deviceId, openClose,brightness);
        }else if(className.contains("SansiLightImpl")){
            SansiLight light = getBeanOfSansiLightImpl(className);
            resultStatus = light.lineControl(deviceId, openClose);
        }

        return resultStatus;
    }


    /**
     * 批量控制车灯
     * @param devices
     * @param controlType
     * @param operIp
     * @return
     */
    public int setBrightnessList(List<Map<String,Object>> devices, String controlType, String operIp)  {
        int flag = 1;
        for (Map<String,Object> device: devices) {
            String deviceId = device.get("deviceId").toString();
            Integer openClose = Integer.parseInt(device.get("openClose").toString());
            int result = setBrightness(deviceId,openClose,controlType,operIp);
            if(result==0){
                flag = 0;
                logger.error("【{}】调整亮度请求响应失败。请联系管理员",deviceId);
            }
        }
        return flag;
    }
}
