package com.tunnel.deal.mqtt.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeItemEnum;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.service.dataInfo.ISdDeviceDataService;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.deal.mqtt.config.MqttGateway;
import com.tunnel.deal.mqtt.service.HongMengMqttCommonService;
import com.tunnel.deal.mqtt.service.HongMengMqttService;

import java.util.Map;

/**
 * describe: 远传压力表-MQTT解析类
 *
 * @author zs
 * @date 2023/5/29
 */
public class PressureGaugeMqttServiceImpl implements HongMengMqttService {

    MqttGateway mqttGateway = SpringUtils.getBean(MqttGateway.class);

    private ISdDevicesService sdDevicesService = SpringUtils.getBean(ISdDevicesService.class);

    private ISdDeviceDataService sdDeviceDataService = SpringUtils.getBean(ISdDeviceDataService.class);

    private HongMengMqttCommonService hongMengMqttCommonService = SpringUtils.getBean(HongMengMqttCommonService.class);

    /**
     * 设备控制方法
     *
     * @param map
     * @param sdDevices
     */
    @Override
    public AjaxResult deviceControl(Map<String, Object> map, SdDevices sdDevices) {
        //压力表没有控制功能
        return null;
    }

    /**
     * 查询设备运行状态或业务属性
     *
     * @param sdDevices
     */
    @Override
    public void queryDeviceData(SdDevices sdDevices) {
//        rhy/iot/control/pressureInstrument/getBizAttr/{ctrlSn}
        hongMengMqttCommonService.queryDeviceData(sdDevices,"rhy/iot/control/pressureInstrument/getBizAttr/");
    }

    /**
     * 处理上报的数据
     *
     * @param topic     主题
     * @param sdDevices 设备信息
     * @param payload   消息
     */
    @Override
    public void handleReceiveData(String topic, SdDevices sdDevices, String payload) {
        if(topic.contains("rhy/iot/receive/pressureInstrument/bizAttr/")){
            //业务属性上报
            handleBizAttrReceiveData(sdDevices,payload);
        }
        if(topic.contains("rhy/iot/receive/pressureInstrument/devStatus/")){
            //设备状态上报
            handleDeviceStateReceiveData(sdDevices,payload);
        }
        if(topic.contains("rhy/iot/receive/pressureInstrument/execStatus/")){
            //指令执行情况上报
            handleExecStateReceiveData(sdDevices,payload);
        }
    }

    /**
     * 处理业务属性上报数据
     * @param sdDevices 设备信息
     * @param payload    消息
     */
    private void handleBizAttrReceiveData(SdDevices sdDevices, String payload){

//        {
//            "sn": "1",
//                "pressure": "1.2",
//                "collectTime": "2022-08-08 17:46:00"
//        }
        JSONObject jsonObject = JSONObject.parseObject(payload);
        String pressure = String.valueOf(jsonObject.get("pressure"));
        String collectTime = String.valueOf(jsonObject.get("collectTime"));
        //修改设备实时状态
        sdDeviceDataService.updateDeviceData(sdDevices,pressure, (long) DevicesTypeItemEnum.YUAN_CHUAN_YA_LI_ZHI.getCode());
    }


    /**
     * 处理设备状态上报数据
     * @param sdDevices 设备信息
     * @param payload    消息
     */
    private void handleDeviceStateReceiveData(SdDevices sdDevices, String payload){

        hongMengMqttCommonService.handleDeviceStateReceiveData(sdDevices,payload);

    }


    /**
     * 处理指令执行情况上报数据
     * @param sdDevices 设备信息
     * @param payload    消息
     */
    private void handleExecStateReceiveData(SdDevices sdDevices, String payload){

        hongMengMqttCommonService.handleExecStateReceiveData(sdDevices,payload);
    }
}
