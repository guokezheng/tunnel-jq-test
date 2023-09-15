package com.tunnel.deal.mqtt.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeEnum;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeItemEnum;
import com.tunnel.business.datacenter.domain.enumeration.OperationLogEnum;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.service.dataInfo.ISdDeviceDataService;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.strategy.service.CommonControlService;
import com.tunnel.deal.mqtt.config.MqttGateway;
import com.tunnel.deal.mqtt.service.HongMengMqttCommonService;
import com.tunnel.deal.mqtt.service.HongMengMqttService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.MessageHandlingException;

import java.util.Map;
import java.util.Optional;

/**
 * describe: 风机处理类
 *
 * @author zs
 * @date 2023/5/26
 */
public class FanMqttServiceImpl implements HongMengMqttService
{

    MqttGateway mqttGateway = SpringUtils.getBean(MqttGateway.class);

    private ISdDevicesService sdDevicesService = SpringUtils.getBean(ISdDevicesService.class);

    private ISdDeviceDataService sdDeviceDataService = SpringUtils.getBean(ISdDeviceDataService.class);

    private HongMengMqttCommonService hongMengMqttCommonService = SpringUtils.getBean(HongMengMqttCommonService.class);

    private CommonControlService commonControlService = SpringUtils.getBean(CommonControlService.class);

    private static final Logger log = LoggerFactory.getLogger(FanMqttServiceImpl.class);


        /**
         * 设备控制方法
         *
         * @param map
         * @param sdDevices
         */
    @Override
    public AjaxResult deviceControl(Map<String, Object> map, SdDevices sdDevices) {
        String deviceId = sdDevices.getEqId();
        Integer controlState = 0;
        //控制设备之前获取设备状态
        Long itemCode = (long) DevicesTypeItemEnum.FENG_JI_STATUS.getCode();
        String beforeState = commonControlService.selectBeforeState(deviceId,itemCode);
        //控制设备
        AjaxResult ajaxResult = null;
        try {
            ajaxResult = sendMqtt(map,sdDevices);
        }catch (Exception e){
            e.printStackTrace();
            log.error("mqtt服务报错：",e.getMessage());
        }
        if(ajaxResult != null){
            Integer code = Integer.valueOf(String.valueOf(ajaxResult.get("code")));
            if( code == HttpStatus.SUCCESS){
                controlState = Integer.valueOf(OperationLogEnum.STATE_SUCCESS.getCode());
            }

            //操作日志
            commonControlService.addOperationLog(map,sdDevices,beforeState,controlState);
        }

        return AjaxResult.success(controlState);

    }


    /**
     * 控制设备
     * @param map
     * @param sdDevices
     * @return
     */
    private AjaxResult sendMqtt(Map<String, Object> map, SdDevices sdDevices) throws MessageHandlingException {
        //{
        //  "sn": "1",
        //  "fanRunStatus": "fan_00",
        //  "actionId": "1"
        //}
        //设备状态
        String state = Optional.ofNullable(map.get("state")).orElse("").toString();
        String ctrlSn = sdDevices.getFEqId();
        String externalDeviceId = sdDevices.getExternalDeviceId();
        JSONObject jsonObject = new JSONObject();
        //映射设备Id
        jsonObject.put("sn",externalDeviceId);
        //状态映射
        jsonObject.put("fanRunStatus",getRunStatusMapping(state));
        //与回复指令对应，使用时间戳
        jsonObject.put("actionId", getRunStatusMapping(state));

        mqttGateway.sendToMqtt("rhy/iot/control/fan/alterRunStatus/"+ctrlSn,1,jsonObject.toJSONString());
        return AjaxResult.success();
    }

    /**
     * 查询设备运行状态或业务属性
     *
     * @param sdDevices
     */
    @Override
    public void queryDeviceData(SdDevices sdDevices) {
//        {
//            "sn": "1",
//                "actionId": "1"
//        }

        hongMengMqttCommonService.queryDeviceData(sdDevices,"rhy/iot/control/fan/getRunStatus/");


    }

    /**
     * 处理上报的数据
     *
     * @param topic      主题
     * @param sdDevices 设备信息
     * @param payload    消息
     */
    @Override
    public void handleReceiveData(String topic, SdDevices sdDevices, String payload) {
        if(topic.contains("rhy/iot/receive/fan/alterRunStatus/")){
            handleRunStateReceiveData(sdDevices,payload);
        }
        if(topic.contains("rhy/iot/receive/fan/devStatus/")){
            handleDeviceStateReceiveData(sdDevices,payload);
        }
        if(topic.contains("rhy/iot/receive/fan/execStatus/")){
            handleExecStateReceiveData(sdDevices,payload,"rhy/iot/control/fan/getRunStatus/");
        }
    }


    /**
     * 处理运行状态上报数据
     * @param sdDevices 设备信息
     * @param payload    消息
     */
    private void handleRunStateReceiveData(SdDevices sdDevices, String payload){
        //{
        // "sn": "1",
        // "fanRunStatus": "fan_00"
        //}
        JSONObject jsonObject = JSONObject.parseObject(payload);
        String mappingStatus = String.valueOf(jsonObject.get("fanRunStatus"));
        updateRunStatus(sdDevices,mappingStatus);

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

        String deviceId = sdDevices.getEqId();

//        {
//            "sn": "1",
//                "actionId": "1",
//                "error": "00",
//                "timeStamp": ""
//        }
//        error
//        00：无错误
//        01：指令执行错误
//        02：命令设备与控制器挂在设备不符合
//        03：错误的参数
        JSONObject jsonObject = JSONObject.parseObject(payload);
        String actionId = String.valueOf(jsonObject.get("actionId"));
        String error = String.valueOf(jsonObject.get("error"));
        String timeStamp = String.valueOf(jsonObject.get("timeStamp"));
        //todo 如何处理
        if(!"00".equals(error)){
            log.error("鸿蒙MQTT指令上报，设备ID="+deviceId+",设备故障码="+error);
        }else{
            updateRunStatus(sdDevices,actionId);
            //TODO 死循环，厂家流程问题
            // queryDeviceData(sdDevices,topic);
        }

        //hongMengMqttCommonService.handleExecStateReceiveData(sdDevices,payload,topic);
    }


    /**
     * 获得运行状态映射的状态
     * @param status
     * @return
     */
    private String getRunStatusMapping(String status){
        String mappingStatus = "";
        //00：停止，01：正转，02：反转，FF：故障
        switch (status){
            case "1":
                mappingStatus = "fan_01";
                break;
            case "2":
                mappingStatus = "fan_02";
                break;
            case "3":
                mappingStatus = "fan_00";
                break;
            default:
                mappingStatus = "fan_FF";
                break;
        }
        return mappingStatus;
    }

    /**
     * 更新实时状态
     * 外部平台定义的状态转为平台定义的状态
     * @param sdDevices
     * @param mappingStatus
     * @return
     */
    private void updateRunStatus(SdDevices sdDevices, String mappingStatus){
        //状态设备类型数据项
        Long eqType = sdDevices.getEqType();
        long statusItemCode = getItemCode(eqType);

        String status = "";
        switch(mappingStatus){
            case "fan_01":
                status = "1";
                //修改设备实时状态
                sdDeviceDataService.updateDeviceData(sdDevices,status, statusItemCode);
                break;
            case "fan_02":
                status = "2";
                //修改设备实时状态
                sdDeviceDataService.updateDeviceData(sdDevices,status, statusItemCode);
                break;
            case "fan_00":
                status = "3";
                //修改设备实时状态
                sdDeviceDataService.updateDeviceData(sdDevices,status, statusItemCode);
                break;
            case "fan_FF":
                //故障处理
                String deviceId = sdDevices.getEqId();
                sdDevicesService.updateFaultStatus(deviceId,false);
                break;
                default:
                    break;

        }
    }

    /**
     * 获取设备类型数据项
     * @param eqType
     * @return
     */
    private Long getItemCode(Long eqType){
        //状态设备类型数据项
        long statusItemCode;
        if(DevicesTypeEnum.FENG_JI.getCode().equals(eqType)){
            //射流风机
            statusItemCode = DevicesTypeItemEnum.FENG_JI_STATUS.getCode();
        }else{
            //轴流风机
            statusItemCode = DevicesTypeItemEnum.ZHOU_LIU_FENG_JI.getCode();
        }
        return statusItemCode;
    }
}
