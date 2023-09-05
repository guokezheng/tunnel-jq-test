package com.tunnel.deal.mqtt.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.service.dataInfo.ISdDeviceDataService;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.deal.mqtt.config.MqttGateway;
import com.tunnel.deal.mqtt.service.HongMengMqttCommonService;
import com.tunnel.deal.mqtt.service.HongMengMqttService;

import java.util.Map;

/**
 * describe: 鸿蒙控制器-MQTT解析类
 *
 * @author zs
 * @date 2023/5/30
 */
public class HongMengControllerMqttServiceImpl implements HongMengMqttService{

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
        //没有控制功能
        return null;
    }

    /**
     * 查询设备运行状态或业务属性
     *
     * @param sdDevices
     */
    @Override
    public void queryDeviceData(SdDevices sdDevices) {
        //rhy/iot/control/controller/getRunStatus/{ctrlSn}
        hongMengMqttCommonService.queryDeviceData(sdDevices,"rhy/iot/control/controller/getRunStatus/");
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
        if(topic.contains("rhy/iot/receive/controller/bizAttr/")){
            //业务属性上报
          handleBizAttrReceiveData(sdDevices,payload);
        }
        if(topic.contains("rhy/iot/receive/controller/runStatus/")){
            //运行状态上报
            handleRunStateReceiveData(sdDevices,payload);
        }
        if(topic.contains("rhy/iot/receive/controller/log/")){
            //日志上报
            handleLogReceiveData(sdDevices,payload);
        }
        if(topic.contains("rhy/iot/receive/illuminance/execStatus/")){
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
//                "hFailure": "0",
//                "devInfo": [{
//            "devSn": "2",
//                    "devType": "01"
//        }]
//        }
//

        JSONObject jsonObject = JSONObject.parseObject(payload);
        // hFailure	是否硬件故障		0：无问题，1：存在问题
        String hFailure = String.valueOf(jsonObject.get("hFailure"));
        //        devInfo	接入设备信息	List	必填	接入设备信息
        JSONArray devInfo =  jsonObject.getJSONArray("devInfo");
        devInfo.forEach(object -> {
            JSONObject obj = (JSONObject) object;
            //        devSn	接入设备编号	String	必填	接入设备编号
            String devSn = String.valueOf(obj.get("devSn"));
            //        devType	接入设备类型	String	必填	01：车道指示器 (三灯版)，02：交通信号灯，03：车道指示器 (双灯版)，04：横洞门，05：风机，06：风速风向，07：COVI， 08：洞内照度检测器，09：洞外光强检测器 10:车道指示器 (六线版) 11:车道指示器 (四线版)
            String devType = String.valueOf(obj.get("devType"));
        });
        // todo
        if("1".equals(hFailure)){
            //存在问题
            String deviceId = sdDevices.getEqId();
            sdDevicesService.updateFaultStatus(deviceId,false);
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
//                "cpuUsage": "30",
//                "usedMemory": "30",
//                "totalMemory": "30",
//                "tickCount": "50",
//                "cpuTemp": "60"
//        }
        String deviceId = sdDevices.getEqId();

        JSONObject jsonObject = JSONObject.parseObject(payload);
//        cpuUsage	CPU使用率	String 		单位：%
        String cpuUsage = String.valueOf(jsonObject.get("cpuUsage"));
        //        usedMemory	已用内存大小	String 		单位：Byte
        String usedMemory = String.valueOf(jsonObject.get("usedMemory"));
        //        totalMemory	设备内存总大小	String 		单位：Byte
        String totalMemory = String.valueOf(jsonObject.get("totalMemory"));
        //        tickCount	启动至今 CPU（tick）周期总数	String 		单位：次
        String tickCount = String.valueOf(jsonObject.get("tickCount"));
        //        cpuTemp	cpu核心工作温度	String 		单位：°C
        String cpuTemp = String.valueOf(jsonObject.get("cpuTemp"));

        //设备掉线监测
        hongMengMqttCommonService.setRedisCacheDeviceStatus(deviceId);
    }

    /**
     * 处理日志上报数据
     * @param sdDevices 设备信息
     * @param payload    消息
     */
    private void handleLogReceiveData(SdDevices sdDevices, String payload){

//        {
//            "sn": "1",
//                "logTime": "2022-08-16 16:58:24",
//                "eventName": "",
//                "eventLvl": "01",
//                "eventDesc": "",
//                "timeStamp": ""
//        }

        JSONObject jsonObject = JSONObject.parseObject(payload);
//        logTime	日志记录时间	String		格式: YYYY-MM-DD hh:mm:ss
        String logTime = String.valueOf(jsonObject.get("logTime"));
//        eventName	事件名称	String		事件名称
        String eventName = String.valueOf(jsonObject.get("eventName"));
//        eventLvl	事件等级	String		01：info，02：warn，03：error
        String eventLvl = String.valueOf(jsonObject.get("eventLvl"));
//        eventDesc	日志描述	String		日志描述
        String eventDesc = String.valueOf(jsonObject.get("eventDesc"));
//        timeStamp	最后更新时间	String		秒时间戳
        String timeStamp = String.valueOf(jsonObject.get("timeStamp"));
        //todo
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
