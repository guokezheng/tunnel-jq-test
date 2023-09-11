package com.tunnel.deal.mqtt.task;

import com.ruoyi.common.core.redis.RedisCache;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.dataInfo.SdDevicesProtocol;
import com.tunnel.business.service.dataInfo.ISdDevicesProtocolService;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.deal.enums.DeviceProtocolCodeEnum;
import com.tunnel.deal.mqtt.service.HongMengMqttService;
import com.tunnel.deal.mqtt.strategy.HongMengMqttStrategyFactory;
import com.zc.common.constant.RedisKeyConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * describe: 鸿蒙控制器-Mqtt-定时任务类
 *
 * @author zs
 * @date 2023/5/29
 */
@Component("HongMengMqttTask")
public class HongMengMqttTask {

    private static final Logger log = LoggerFactory.getLogger(HongMengMqttTask.class);

    @Autowired
    private ISdDevicesService sdDevicesService;

    @Autowired
    private ISdDevicesProtocolService sdDevicesProtocolService;

    @Autowired
    private HongMengMqttStrategyFactory hongMengMqttStrategyFactory;

    /**
     * Redis缓存工具类
     **/
    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ISdDevicesService devicesService;

    /**
     * 在线时间检测控制时间，定时任务传入方便修改
     */
    public static Integer onlineSecondInterval;


    /**
     * 鸿蒙控制器连接状态监测
     */
    public void connectStatus(Integer second){
        onlineSecondInterval = second;

        int onlineCount = 0;

        final String onlineFlag = "online";
        List<SdDevices> devicesList = getHongMengDeviceList();
        for(SdDevices sdDevices : devicesList){
            String deviceId = sdDevices.getEqId();
            String key = RedisKeyConstants.HONG_MENG_MQTT_STATUS + ":" + deviceId;
            String status = redisCache.getCacheObject(key);
            if(!onlineFlag.equals(status)){
                //鸿蒙控制器10秒内没有信息返回，设备设置为离线
                //2023-9-10修改：鸿蒙控制器长时间不上报数据，将子设备离线去掉
                devicesService.updateOfflineStatus(deviceId,false);
            }else{
                devicesService.updateOnlineStatus(deviceId,false);
                onlineCount++;
            }
        }
        log.error("鸿蒙控制器以及子设备（根据鸿蒙控制器以及子设备推送的数据）计算，在线数=："+ onlineCount);
    }

    /**
     * 定时请求获取鸿蒙Mqtt设备的实时数据
     */
    public void getDeviceData(){
        List<SdDevices> devicesList = getHongMengDeviceList();
        devicesList.forEach(device->{
            String eqType = String.valueOf(device.getEqType());
            HongMengMqttService hongMengMqttService = hongMengMqttStrategyFactory.strategy(eqType);
            //查询设备状态或者数据
            hongMengMqttService.queryDeviceData(device);
        });
    }

    /**
     * 获取所有配置MQTT对应协议的鸿蒙控制器设备信息
     * @return
     */
    public List<SdDevices> getHongMengDeviceList(){
        List<SdDevices> devicesList = new ArrayList<>();
        //协议标识
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
            devicesList = sdDevicesService.selectDevicesByProtocol(queryDevices);
        }
        return devicesList;
    }
}
