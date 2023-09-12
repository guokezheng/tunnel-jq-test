package com.tunnel.deal.mqtt.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.spring.SpringUtils;
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
 * describe: 警示灯带-MQTT解析类
 *
 * @author zs
 * @date 2023/5/30
 */
public class WarnLightStripMqttServiceImpl implements HongMengMqttService {


    MqttGateway mqttGateway = SpringUtils.getBean(MqttGateway.class);

    private ISdDevicesService sdDevicesService = SpringUtils.getBean(ISdDevicesService.class);

    private ISdDeviceDataService sdDeviceDataService = SpringUtils.getBean(ISdDeviceDataService.class);

    private HongMengMqttCommonService hongMengMqttCommonService = SpringUtils.getBean(HongMengMqttCommonService.class);

    private CommonControlService commonControlService = SpringUtils.getBean(CommonControlService.class);

    private static final Logger log = LoggerFactory.getLogger(WarnLightStripMqttServiceImpl.class);

    /**
     * 设备控制方法
     *
     * @param map
     * @param sdDevices
     */
    @Override
    public AjaxResult deviceControl(Map<String, Object> map, SdDevices sdDevices) {
        String devId = Optional.ofNullable(map.get("devId")).orElse("").toString();

        Integer controlState = 0;
        //控制设备之前获取设备状态
        String beforeState = commonControlService.selectBeforeState(devId, (long) DevicesTypeItemEnum.JING_SHI_DENG_DAI.getCode());
        //控制设备
        AjaxResult ajaxResult = null;
        try {
            ajaxResult = sendMqtt(map,sdDevices);
        } catch (Exception e) {
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
        //        TODO 警示灯带业务逻辑

        //{
        //  "sn": "1",
        //  "wslRunStatus":[
        //      "warnLightStrip_00_09_01_09",
        //      "warnLightStrip_01_00_04_09",
        //      "warnLightStrip_02_09_05_09",
        //      "warnLightStrip_03_05_09_09"
        //   ]
        //  "actionId": "1"
        //}
//        由于警示灯带存在多种颜色，其控制状态采用数组形式表示：
//        warnLightStrip_[颜色]_[亮度]_[频率]_[占空比]
//        颜色：00:绿色  01:黄色  02:红色 03:蓝色
//        亮度：00-09 （00表示不亮、09表示最亮）
//        频率：01-09 （01表示闪烁频率最慢、09表示闪烁频率最快）
//        占空比：01-09 （01表示占空比为20%、09表示占空比为100%）

//        rhy/iot/control/warnLightStrip/runStatus/{sn}
        //颜色
        String color = Optional.ofNullable(map.get("color")).orElse("").toString();
        //亮度
        String lightness = Optional.ofNullable(map.get("lightness")).orElse("").toString();
        //频率
        String frequency = Optional.ofNullable(map.get("frequency")).orElse("").toString();
        //占空比
        String ratio = Optional.ofNullable(map.get("ratio")).orElse("").toString();

        String[] array = {color,lightness,frequency,ratio};
        String ctrlSn = sdDevices.getFEqId();
        String externalDeviceId = sdDevices.getExternalDeviceId();
        JSONObject jsonObject = new JSONObject();
        //映射设备Id
        jsonObject.put("sn",externalDeviceId);
        //状态映射
        jsonObject.put("wslRunStatus",array);
        //与回复指令对应，使用时间戳
        jsonObject.put("actionId", hongMengMqttCommonService.getActionId());

        mqttGateway.sendToMqtt("rhy/iot/control/warnLightStrip/runStatus/"+ctrlSn,1,jsonObject.toJSONString());
        return AjaxResult.success();
    }

    /**
     * 查询设备运行状态或业务属性
     *
     * @param sdDevices
     */
    @Override
    public void queryDeviceData(SdDevices sdDevices) {
//rhy/iot/control/warnLightStrip/getRunStatus/{ctrlSn}
        hongMengMqttCommonService.queryDeviceData(sdDevices,"rhy/iot/control/warnLightStrip/getRunStatus/");
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
        if(topic.contains("rhy/iot/receive/warnLightStrip/runStatus/")){
            //运行状态上报
            handleRunStateReceiveData(sdDevices,payload);
        }
        if(topic.contains("rhy/iot/receive/warnLightStrip/devStatus/")){
            //设备状态上报
            handleDeviceStateReceiveData(sdDevices,payload);
        }
        if(topic.contains("rhy/iot/receive/warnLightStrip/execStatus/")){
            //指令执行情况上报
            handleExecStateReceiveData(sdDevices,payload,"rhy/iot/control/warnLightStrip/getRunStatus/");
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
//                "wslRunStatus":[
//            "warnLightStrip_00_09_01_09",
//                    "warnLightStrip_01_00_04_09",
//                    "warnLightStrip_02_09_05_09",
//                    "warnLightStrip_03_05_09_09"
//   ]
//        }

        JSONObject jsonObject = JSONObject.parseObject(payload);
        JSONArray mappingStatus =  jsonObject.getJSONArray("wslRunStatus");

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
    private void handleExecStateReceiveData(SdDevices sdDevices, String payload,String topic){

        hongMengMqttCommonService.handleExecStateReceiveData(sdDevices,payload,topic);
    }

    /**
     * 更新实时状态
     * 外部平台定义的状态转为平台定义的状态
     * @param sdDevices
     * @param mappingStatus
     * @return
     */
    private void updateRunStatus(SdDevices sdDevices, JSONArray mappingStatus){
        //状态设备类型数据项
        long colorItemCode = DevicesTypeItemEnum.JING_SHI_DENG_DAI.getCode();

        //        由于警示灯带存在多种颜色，其控制状态采用数组形式表示：
//        warnLightStrip_[颜色]_[亮度]_[频率]_[占空比]
//        颜色：00:绿色  01:黄色  02:红色 03:蓝色
//        亮度：00-09 （00表示不亮、09表示最亮）
//        频率：01-09 （01表示闪烁频率最慢、09表示闪烁频率最快）
//        占空比：01-09 （01表示占空比为20%、09表示占空比为100%）

        //颜色
        String color = String.valueOf(mappingStatus.get(0));
        //亮度
        String lightness = String.valueOf(mappingStatus.get(1));
        //频率
        String frequency = String.valueOf(mappingStatus.get(2));
        //占空比
        String ratio = String.valueOf(mappingStatus.get(3));

        //获取平台对应的颜色值
       String colorSystem = getColor(color);

        //修改设备实时状态
        sdDeviceDataService.updateDeviceData(sdDevices,colorSystem, colorItemCode);
        // TODO 修改其他参数

    }

    /**
     * 获取平台对应的颜色值
     * @param color
     * @return
     */
    private String getColor(String color){
        //平台颜色值
        String colorSystem = "";
        switch(color){
            case "00":
                colorSystem = "2";
                break;
            case "01":
                colorSystem = "3";
                break;
            case "02":
                colorSystem = "1";
                break;
            default:
                break;
        }
        return colorSystem;
    }


}
