package com.tunnel.deal.mqtt.task;

import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.dataInfo.SdDevicesProtocol;
import com.tunnel.business.service.dataInfo.ISdDevicesProtocolService;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.deal.enums.DeviceProtocolCodeEnum;
import com.tunnel.deal.mqtt.service.HongMengMqttService;
import com.tunnel.deal.mqtt.strategy.HongMengMqttStrategyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * describe: 鸿蒙控制器-Mqtt-定时任务类
 *
 * @author zs
 * @date 2023/5/29
 */
@Component("HongMengMqttTask")
public class HongMengMqttTask {

    @Autowired
    private ISdDevicesService sdDevicesService;

    @Autowired
    private ISdDevicesProtocolService sdDevicesProtocolService;

    @Autowired
    private HongMengMqttStrategyFactory hongMengMqttStrategyFactory;

    /**
     * 定时请求获取鸿蒙Mqtt设备的实时数据
     */
    public void getDeviceData(){
        //暂时写死协议标识，后期优化 todo
        String protocolCode = DeviceProtocolCodeEnum.HONGMENG_MQTT_PROTOCOL_CODE.getCode();
        SdDevicesProtocol sdDevicesProtocol = new SdDevicesProtocol();
        sdDevicesProtocol.setProtocolCode(protocolCode);
        //查询对应协议
       List<SdDevicesProtocol> protocolList = sdDevicesProtocolService.selectSdDevicesProtocolList(sdDevicesProtocol);
       if(protocolList != null && protocolList.size() > 0){
           sdDevicesProtocol = protocolList.get(0);
           //查询协议对应的设备
           SdDevices queryDevices = new SdDevices();
           queryDevices.setProtocolId(sdDevicesProtocol.getId());
           List<SdDevices> devicesList = sdDevicesService.selectDevicesByProtocol(queryDevices);
           devicesList.forEach(device->{
//               String deviceId = device.getEqId();
               String eqType = String.valueOf(device.getEqType());
               HongMengMqttService hongMengMqttService = hongMengMqttStrategyFactory.strategy(eqType);
               //查询设备状态或者数据
               hongMengMqttService.queryDeviceData(device);
           });
       }


    }
}
