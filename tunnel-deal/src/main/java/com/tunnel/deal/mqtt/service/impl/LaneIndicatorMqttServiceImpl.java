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

import java.util.Map;
import java.util.Optional;

/**
 * describe: 车道指示器处理类
 *
 * @author zs
 * @date 2023/5/26
 */
public class LaneIndicatorMqttServiceImpl implements HongMengMqttService {

    MqttGateway mqttGateway = SpringUtils.getBean(MqttGateway.class);

    private ISdDevicesService sdDevicesService = SpringUtils.getBean(ISdDevicesService.class);

    private ISdDeviceDataService sdDeviceDataService = SpringUtils.getBean(ISdDeviceDataService.class);

    private HongMengMqttCommonService hongMengMqttCommonService = SpringUtils.getBean(HongMengMqttCommonService.class);

    private CommonControlService commonControlService = SpringUtils.getBean(CommonControlService.class);

    /**
     * 设备控制方法
     *
     * @param map
     * @param sdDevices
     */
    @Override
    public AjaxResult deviceControl(Map<String, Object> map, SdDevices sdDevices) {
        String deviceId = sdDevices.getEqId();
        Long eqType = sdDevices.getEqType();
        Integer controlState = 0;
        //控制设备之前获取设备状态
        Long itemCode = getItemCode(eqType);
        String beforeState = commonControlService.selectBeforeState(deviceId,itemCode);
        //控制设备
        AjaxResult ajaxResult = sendMqtt(map,sdDevices);
        Integer code = Integer.valueOf(String.valueOf(ajaxResult.get("code")));
        if( code == HttpStatus.SUCCESS){
            controlState = Integer.valueOf(OperationLogEnum.STATE_SUCCESS.getCode());
        }

        //操作日志
        commonControlService.addOperationLog(map,sdDevices,beforeState,controlState);

        return AjaxResult.success(controlState);
    }

    /**
     * 控制设备
     * @param map
     * @param sdDevices
     * @return
     */
    private AjaxResult sendMqtt(Map<String, Object> map, SdDevices sdDevices){
        //
//        {
//            "sn": "1",
//                "liRunStatus": "laneIndicator_00",
//                "actionId": "1"
//        }
        //设备状态
        String state = Optional.ofNullable(map.get("state")).orElse("").toString();
        String externalDeviceId = sdDevices.getExternalDeviceId();
        JSONObject jsonObject = new JSONObject();
        //映射设备Id
        jsonObject.put("sn",externalDeviceId);
        //状态映射
        jsonObject.put("liRunStatus",getRunStatusMapping(state));
        //与回复指令对应，使用时间戳
        jsonObject.put("actionId", hongMengMqttCommonService.getActionId());

        mqttGateway.sendToMqtt("rhy/iot/control/laneIndicator/runStatus/{"+externalDeviceId+"}",jsonObject.toJSONString());
        return AjaxResult.success();
    }

    /**
     * 查询设备运行状态或业务属性
     *
     * @param sdDevices
     */
    @Override
    public void queryDeviceData(SdDevices sdDevices) {
//rhy/iot/control/laneIndicator/getRunStatus/{ctrlSn}
        hongMengMqttCommonService.queryDeviceData(sdDevices,"rhy/iot/control/laneIndicator/getRunStatus/");
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
        if(topic.contains("rhy/iot/receive/laneIndicator/runStatus/")){
            //运行状态上报
            handleRunStateReceiveData(sdDevices,payload);
        }
        if(topic.contains("rhy/iot/receive/laneIndicator/devStatus/")){
            //设备状态上报
            handleDeviceStateReceiveData(sdDevices,payload);
        }
        if(topic.contains("rhy/iot/receive/laneIndicator/execStatus/")){
            //指令执行情况上报
            handleExecStateReceiveData(sdDevices,payload);
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
//                "liRunStatus": "laneIndicator_00"
//        }
        JSONObject jsonObject = JSONObject.parseObject(payload);
        String mappingStatus = String.valueOf(jsonObject.get("liRunStatus"));
        updateRunStatus(sdDevices,mappingStatus);
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

    /**
     * 更新实时状态
     * 外部平台定义的状态转为平台定义的状态
     * @param sdDevices
     * @param mappingStatus
     * @return
     */
    private void updateRunStatus(SdDevices sdDevices, String mappingStatus){
        //状态设备类型数据项
        long statusItemCode;
        String eqType = String.valueOf(sdDevices.getEqType());
        if(DevicesTypeEnum.PU_TONG_CHE_ZHI.getCode().equals(eqType)){
            //普通车道指示器
            statusItemCode = DevicesTypeItemEnum.PU_TONG_CHE_ZHI.getCode();
        }else{
            //带左转车道指示器
            statusItemCode = DevicesTypeItemEnum.ZHUO_ZHUAN_CHE_ZHI.getCode();
        }

        //00：关闭，01：正绿反红，02：正红反绿，03：正红反红，04：转向，05：正绿反绿 FF：故障
        String status = "";
        switch(mappingStatus){
            case "00":
                status = "4";
                //修改设备实时状态
                sdDeviceDataService.updateDeviceData(sdDevices,status, statusItemCode);
                break;
            case "01":
                status = "1";
                //修改设备实时状态
                sdDeviceDataService.updateDeviceData(sdDevices,status, statusItemCode);
                break;
            case "02":
                status = "2";
                //修改设备实时状态
                sdDeviceDataService.updateDeviceData(sdDevices,status, statusItemCode);
                break;
            case "03":
                status = "3";
                //修改设备实时状态
                sdDeviceDataService.updateDeviceData(sdDevices,status, statusItemCode);
                break;
            case "04":
                status = "5";
                //修改设备实时状态
                sdDeviceDataService.updateDeviceData(sdDevices,status, statusItemCode);
                break;
            case "FF":
                //故障处理
                String deviceId = sdDevices.getEqId();
                sdDevicesService.updateFaultStatus(deviceId,false);
                break;
            default:
                break;

        }
    }


    /**
     * 获得运行状态映射的状态
     *  todo 后期改为从配置表中读取
     * @param status
     * @return
     */
    private String getRunStatusMapping(String status){
        String mappingStatus = "";
        //00：关闭，01：正绿反红，02：正红反绿，03：正红反红，04：转向，05：正绿反绿 FF：故障
        switch (status){
            case "1":
                mappingStatus = "01";
                break;
            case "2":
                mappingStatus = "02";
                break;
            case "3":
                mappingStatus = "03";
                break;
            case "4":
                mappingStatus = "00";
                break;
            case "5":
                mappingStatus = "04";
                break;
            default:
                mappingStatus = "FF";
                break;
        }
        return mappingStatus;
    }


    /**
     * 获取设备类型数据项
     * @param eqType
     * @return
     */
    private Long getItemCode(Long eqType){
        //状态设备类型数据项
        long statusItemCode;
        if(DevicesTypeEnum.PU_TONG_CHE_ZHI.getCode().equals(eqType)){
            //普通车道指示器
            statusItemCode = DevicesTypeItemEnum.PU_TONG_CHE_ZHI.getCode();
        }else{
            //带左转车道指示器
            statusItemCode = DevicesTypeItemEnum.ZHUO_ZHUAN_CHE_ZHI.getCode();
        }
        return statusItemCode;
    }
}
