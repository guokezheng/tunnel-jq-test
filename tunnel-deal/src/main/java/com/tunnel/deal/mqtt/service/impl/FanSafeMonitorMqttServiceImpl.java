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
 * describe: 风机安全检测仪-MQTT解析类
 *
 * @author zs
 * @date 2023/5/30
 */
public class FanSafeMonitorMqttServiceImpl implements HongMengMqttService {

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
        //风机安全检测仪 没有控制功能
        return null;
    }

    /**
     * 查询设备运行状态或业务属性
     *
     * @param sdDevices
     */
    @Override
    public void queryDeviceData(SdDevices sdDevices) {
//        rhy/iot/control/fanSafeMonitor/getBizAttr/{ctrlSn}

        hongMengMqttCommonService.queryDeviceData(sdDevices,"rhy/iot/control/fanSafeMonitor/getBizAttr/");
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
        if(topic.contains("rhy/iot/receive/fanSafeMonitor/bizAttr/")){
            //业务属性上报
            handleRunStateReceiveData(sdDevices,payload);
        }
        if(topic.contains("rhy/iot/receive/fanSafeMonitor/devStatus/")){
            //设备状态上报
            handleDeviceStateReceiveData(sdDevices,payload);
        }
        if(topic.contains("rhy/iot/receive/fanSafeMonitor/execStatus/")){
            //指令执行情况上报
            handleExecStateReceiveData(sdDevices,payload,"rhy/iot/control/fanSafeMonitor/getBizAttr/");
        }
    }

    /**
     * 处理运行状态上报数据
     * @param sdDevices 设备信息
     * @param payload    消息
     */
    private void handleRunStateReceiveData(SdDevices sdDevices, String payload){
//        {
//            "sn": "1",
//                "shakeSpeed": "5",
//                "amplitude": "6",
//                "subside": "0",
//                "slope": "6",
//                "shakeAlaram": "00",
//                "subsideSlopeAlaram": "00",
//                "collectTime": "2022-08-08 17:46:00"
//        }

        JSONObject jsonObject = JSONObject.parseObject(payload);
        //振动速度值
        String shakeSpeed = String.valueOf(jsonObject.get("shakeSpeed"));
        //振动幅度值
        String amplitude = String.valueOf(jsonObject.get("amplitude"));
        //沉降值
        String subside = String.valueOf(jsonObject.get("subside"));
        //倾斜值
        String slope = String.valueOf(jsonObject.get("slope"));
        //振动告警
        String shakeAlaram = String.valueOf(jsonObject.get("shakeAlaram"));
        //沉降倾斜告警
        String subsideSlopeAlaram = String.valueOf(jsonObject.get("subsideSlopeAlaram"));
        //采集时间
        String collectTime = String.valueOf(jsonObject.get("collectTime"));

        String shakeAlaramSystem = getShakeAlaram(shakeAlaram);
        String subsideSlopeAlaramSystem = getSubsideSlopeAlaram(subsideSlopeAlaram);
        //修改设备实时状态
        sdDeviceDataService.updateDeviceData(sdDevices,shakeSpeed, (long) DevicesTypeItemEnum.ZHEN_DONG_SU_DU.getCode(),true);
        sdDeviceDataService.updateDeviceData(sdDevices,amplitude, (long) DevicesTypeItemEnum.ZHEN_DONG_FU_DU.getCode(),true);
        sdDeviceDataService.updateDeviceData(sdDevices,subside, (long) DevicesTypeItemEnum.CHEN_JIANG_ZHI.getCode(),true);
        sdDeviceDataService.updateDeviceData(sdDevices,slope, (long) DevicesTypeItemEnum.QING_XIE_ZHI.getCode(),true);
        sdDeviceDataService.updateDeviceData(sdDevices,shakeAlaramSystem, (long) DevicesTypeItemEnum.ZHEN_DONG_GAO_JING.getCode(),true);
        sdDeviceDataService.updateDeviceData(sdDevices,subsideSlopeAlaramSystem, (long) DevicesTypeItemEnum.CHEN_JIANG_QING_XIE_GAO_JING.getCode(),true);


        String deviceId = sdDevices.getEqId();
        //设备掉线监测
        hongMengMqttCommonService.setRedisCacheDeviceStatus(deviceId);
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
    private void handleExecStateReceiveData(SdDevices sdDevices, String payload,String topic){

        hongMengMqttCommonService.handleExecStateReceiveData(sdDevices,payload,topic);
    }

    /**
     * 获取平台对应的振动告警值
     * @param shakeAlaram
     * @return
     */
    private String getShakeAlaram(String shakeAlaram){
//        00：正常 01：报警 02：危险
// 平台       0：正常 1：报警 2：危险
        String shakeAlaramSystem = "";
        switch(shakeAlaram){
            case "00":
                shakeAlaramSystem = "0";
                break;
            case "01":
                shakeAlaramSystem = "1";
                break;
            case "02":
                shakeAlaramSystem = "2";
                break;
            default:
                break;
        }
        return shakeAlaramSystem;
    }


    /**
     * 获取平台对应的沉降倾斜告警值
     * @param subsideSlopeAlaram
     * @return
     */
    private String getSubsideSlopeAlaram(String subsideSlopeAlaram){
//        00：正常 01：低限位报警 02：高限位报警
        // 平台 0：正常 1：低限位报警 2：高限位报警
        String subsideSlopeAlaramSystem = "";
        switch(subsideSlopeAlaram){
            case "00":
                subsideSlopeAlaramSystem = "0";
                break;
            case "01":
                subsideSlopeAlaramSystem = "1";
                break;
            case "02":
                subsideSlopeAlaramSystem = "2";
                break;
            default:
                break;
        }
        return subsideSlopeAlaramSystem;
    }
}
