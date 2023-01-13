package com.ruoyi.quartz.task;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.tunnel.business.datacenter.domain.enumeration.DevicesStatusEnum;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeEnum;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeItemEnum;
import com.tunnel.business.domain.dataInfo.SdDeviceData;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.event.SdDeviceNowState;
import com.tunnel.business.mapper.dataInfo.SdDeviceDataMapper;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.service.digitalmodel.RadarEventService;
import com.tunnel.business.service.sendDataToKafka.SendDeviceStatusToKafkaService;
import com.tunnel.deal.warninglightstrip.WarningLightStripHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 警示灯带定时任务调度
 */
@Component("WarningLightStripTask")
public class WarningLightStripTask {

    @Value("${authorize.name}")
    private String deploymentType;

    private static final Logger log = LoggerFactory.getLogger(WarningLightStripTask.class);

    private static SdDeviceDataMapper deviceDataMapper = SpringUtils.getBean(SdDeviceDataMapper.class);
    private static RadarEventService radarEventService = SpringUtils.getBean(RadarEventService.class);
    private static SendDeviceStatusToKafkaService sendData = SpringUtils.getBean(SendDeviceStatusToKafkaService.class);
    private static ISdDevicesService sdDevicesService = SpringUtils.getBean(ISdDevicesService.class);

    public void handle() {
        //定时获取警示灯带当前状态
        Long warningLightStripId = Long.valueOf(DevicesTypeEnum.JING_SHI_DENG_DAI.getCode());
        SdDevices sdDevices = new SdDevices();
        sdDevices.setEqType(warningLightStripId);
        List<SdDevices> warningLightStripDevicesList = sdDevicesService.selectSdDevicesList(sdDevices);
        for (int i = 0;i < warningLightStripDevicesList.size();i++) {
            SdDevices devices = warningLightStripDevicesList.get(i);
            if (devices.getIp() == null && devices.getPort() == null && ("").equals(devices.getIp())) {
                continue;
            } else if (deploymentType != null && deploymentType.equals("GLZ")) {
                //进行状态查询
                sendCommand(devices, devices.getIp(), devices.getPort());
            }
        }
    }

    private String handleDeviceStatus(SdDevices sdDevices, Map<String, Object> codeMap) {
        String state = "";
        if (codeMap == null || codeMap.isEmpty()) {
            //当前诱导灯控制器已经离线，存储状态到devices
            sdDevices.setEqStatus(DevicesStatusEnum.DEVICE_OFF_LINE.getCode());
            sdDevices.setEqStatusTime(new Date());
            sendData.pushDevicesStatusToOtherSystem(sdDevices, "1", "off");
            sendData.pushDevicesStatusToOtherSystem(sdDevices, "2", "off");
        } else {
            if (codeMap.get("openState") != null) {
                state = codeMap.get("openState").toString();
            } else if (codeMap.get("runMode") != null) {
                state = codeMap.get("runMode").toString();
            }
            sdDevices.setEqStatus(DevicesStatusEnum.DEVICE_ON_LINE.getCode());
            sdDevices.setEqStatusTime(new Date());
            sendData.pushDevicesStatusToOtherSystem(sdDevices, "1", "on");
            sendData.pushDevicesStatusToOtherSystem(sdDevices, "2", "on");
        }
        //更新诱导灯控制器状态，诱导灯子设备状态也需要更改
        sdDevicesService.updateSdDevices(sdDevices);
        SdDevices sonDevices = new SdDevices();
        sonDevices.setFEqId(sdDevices.getEqId());
        sonDevices.setEqStatus(sdDevices.getEqStatus());
        sdDevicesService.updateSdDevicesByFEqId(sonDevices);
        return state;
    }

    private static void sendNowDeviceStatusByWebsocket(SdDevices sdDevices, String[] state) {
        List<SdDeviceNowState> dataList = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        SdDeviceNowState sdDeviceNowState = new SdDeviceNowState();
        sdDeviceNowState.setEqId(sdDevices.getEqId());
        sdDeviceNowState.setEqType(sdDevices.getEqType());
        sdDeviceNowState.setEqStatus(sdDevices.getEqStatus());
        sdDeviceNowState.setEqDirection(sdDevices.getEqDirection());
        sdDeviceNowState.setEqName(sdDevices.getEqName());
        sdDeviceNowState.setEqTunnelId(sdDevices.getEqTunnelId());
        sdDeviceNowState.setPile(sdDevices.getPile());
        sdDeviceNowState.setState(state[0]);
        sdDeviceNowState.setBrightness(state[1]);
        sdDeviceNowState.setFrequency(state[2]);
        dataList.add(sdDeviceNowState);
        jsonObject.put("deviceStatus", dataList);
//        WebSocketService.broadcast("deviceStatus", jsonObject.toString());
    }

    private static void sendDataToWanJi(SdDevices sdDevices, String runStatus, String runMode) {
        Map<String, Object> map = new HashMap<>();
        map.put("deviceId", sdDevices.getEqId());
        map.put("deviceType", sdDevices.getEqType());
        JSONObject jsonObject = new JSONObject();
        if (runStatus.equals("lightOn")) {
            runStatus = "1";
        } else if (runStatus.equals("lightOff")) {
            runStatus = "2";
        }
        jsonObject.put("runStatus", Integer.valueOf(runStatus));
        jsonObject.put("runMode", Integer.valueOf(runMode));
        map.put("deviceData", jsonObject);
        radarEventService.sendBaseDeviceStatus(map);
    }

    private static void handleCodeMap(SdDevices sdDevices, Map<String, Object> codeMap) {
        if (codeMap == null || codeMap.isEmpty() || codeMap.get("mode") == null) {
            return;
        }
        String mode = codeMap.get("mode").toString();
        if (mode.equals("1")) {
            mode = "2";
        } else if (mode.equals("4")) {
            mode = "3";
        }
        //当前数据库中，控制器一定有子级设备，而且状态是一致的
        SdDevices dev = new SdDevices();
        dev.setFEqId(sdDevices.getEqId());
        List<SdDevices> list = sdDevicesService.selectSdDevicesList(dev);
        if (sdDevices.getEqType().longValue() == DevicesTypeEnum.YOU_DAO_DENG_CONTROL.getCode().longValue()) {
            saveDataIntoSdDeviceData(sdDevices, "2", DevicesTypeItemEnum.GUIDANCE_LAMP_CONTROL_MODE.getCode());
            if (!list.isEmpty()) {
                for (int i = 0; i < list.size(); i++) {
                    SdDevices devo = list.get(i);
                    saveDataIntoSdDeviceData(devo, "2", DevicesTypeItemEnum.GUIDANCE_LAMP_CONTROL_MODE.getCode());
                }
            }
        } else if (sdDevices.getEqType().longValue() == DevicesTypeEnum.LUN_KUO_BIAO_CONTROL.getCode().longValue()) {
            saveDataIntoSdDeviceData(sdDevices, "2", DevicesTypeItemEnum.DELINEATOR_CONTROL_MODE.getCode());
            if (!list.isEmpty()) {
                for (int i = 0; i < list.size(); i++) {
                    SdDevices devo = list.get(i);
                    saveDataIntoSdDeviceData(devo, "2", DevicesTypeItemEnum.DELINEATOR_CONTROL_MODE.getCode());
                }
            }
        }
        sendDataToWanJi(sdDevices, "lightOn", mode);
        String brightness = codeMap.get("brightness").toString();
        if (sdDevices.getEqType().longValue() == DevicesTypeEnum.YOU_DAO_DENG_CONTROL.getCode().longValue()) {
            saveDataIntoSdDeviceData(sdDevices, brightness, DevicesTypeItemEnum.GUIDANCE_LAMP_BRIGHNESS.getCode());
            if (!list.isEmpty()) {
                for (int i = 0; i < list.size(); i++) {
                    SdDevices devo = list.get(i);
                    saveDataIntoSdDeviceData(devo, brightness, DevicesTypeItemEnum.GUIDANCE_LAMP_BRIGHNESS.getCode());
                }
            }
        } else if (sdDevices.getEqType().longValue() == DevicesTypeEnum.LUN_KUO_BIAO_CONTROL.getCode().longValue()) {
            saveDataIntoSdDeviceData(sdDevices, brightness, DevicesTypeItemEnum.DELINEATOR_BRIGHNESS.getCode());
            if (!list.isEmpty()) {
                for (int i = 0; i < list.size(); i++) {
                    SdDevices devo = list.get(i);
                    saveDataIntoSdDeviceData(devo, brightness, DevicesTypeItemEnum.DELINEATOR_BRIGHNESS.getCode());
                }
            }
        }
        String frequency = codeMap.get("frequency").toString();
        if (sdDevices.getEqType().longValue() == DevicesTypeEnum.YOU_DAO_DENG_CONTROL.getCode().longValue()) {
            saveDataIntoSdDeviceData(sdDevices, frequency, DevicesTypeItemEnum.GUIDANCE_LAMP_FREQUENCY.getCode());
            if (!list.isEmpty()) {
                for (int i = 0; i < list.size(); i++) {
                    SdDevices devo = list.get(i);
                    saveDataIntoSdDeviceData(devo, brightness, DevicesTypeItemEnum.GUIDANCE_LAMP_FREQUENCY.getCode());
                }
            }
        } else if (sdDevices.getEqType().longValue() == DevicesTypeEnum.LUN_KUO_BIAO_CONTROL.getCode().longValue()) {
            saveDataIntoSdDeviceData(sdDevices, brightness, DevicesTypeItemEnum.DELINEATOR_FREQUENCY.getCode());
            if (!list.isEmpty()) {
                for (int i = 0; i < list.size(); i++) {
                    SdDevices devo = list.get(i);
                    saveDataIntoSdDeviceData(devo, brightness, DevicesTypeItemEnum.DELINEATOR_FREQUENCY.getCode());
                }
            }
        }
        String[] states = new String[3];
        states[0] = mode;
        states[1] = brightness;
        states[2] = frequency;
        sendNowDeviceStatusByWebsocket(sdDevices, states);
    }

    private static void saveDataIntoSdDeviceData(SdDevices sdDevices, String value, Integer itemId) {
        SdDeviceData sdDeviceData = new SdDeviceData();
        sdDeviceData.setDeviceId(sdDevices.getEqId());
        sdDeviceData.setItemId(Long.valueOf(itemId));
        List<SdDeviceData> deviceData = deviceDataMapper.selectSdDeviceDataList(sdDeviceData);
        if (deviceData.size() > 0) {
            SdDeviceData data = deviceData.get(0);
            data.setData(value);
            data.setUpdateTime(new Date());
            deviceDataMapper.updateSdDeviceData(data);
            sendData.pushDevicesDataNowTime(data);
        } else {
            sdDeviceData.setData(value);
            sdDeviceData.setCreateTime(new Date());
            deviceDataMapper.insertSdDeviceData(sdDeviceData);
            sendData.pushDevicesDataNowTime(sdDeviceData);
        }

    }

    public void sendCommand(SdDevices sdDevices, String ip, String portAddress) {
        if (ip == null || portAddress == null || "".equals(ip) || "".equals(portAddress)) {
            return;
        }
        Integer port = Integer.valueOf(portAddress);
        String command = "01030000000E";
        String crc = WarningLightStripHandle.getCRC(command);
        command = command + crc;
        Map map = WarningLightStripHandle.sendCommandToWarningLightStrip(ip, port, command);
        if (map == null || map.isEmpty() || map.get("isSuccess") == null || map.get("data") == null) {
            return;
        }
        if (map.get("isSuccess").toString().equals("0")) {
            sdDevices.setEqStatus(DevicesStatusEnum.DEVICE_OFF_LINE.getCode());
            sdDevices.setEqStatusTime(new Date());
            sdDevicesService.updateSdDevices(sdDevices);
            return;
        } else if (map.get("isSuccess").toString().equals("1")) {
            sdDevices.setEqStatus(DevicesStatusEnum.DEVICE_ON_LINE.getCode());
            sdDevices.setEqStatusTime(new Date());
            sdDevicesService.updateSdDevices(sdDevices);
        }
        //设备响应数据
        //01 03 1C 00 00 00 09 00 09 00 09 00 09 00 09 00 00 00 09 00 09 00 09 00 09 00 09 00 00 00 01 45 BE
        String data = map.get("data").toString().replaceAll(" ", "");
        log.info("获取到警示灯带设备{}的报文：{}", sdDevices.getEqId(), data);
        if (data.equals("")) {
            return;
        }
        data = data.substring(6);
        String addressOne = data.substring(0, 12);
        String addressOneOpenStatus = addressOne.substring(0, 4);

        data = data.substring(12);
        String addressTwo = data.substring(0, 12);
        String addressTwoOpenStatus = addressTwo.substring(0, 4);

        data = data.substring(12);
        String addressThree = data.substring(0, 12);
        String addressThreeOpenStatus = addressThree.substring(0, 4);

        data = data.substring(12);
        String addressFour = data.substring(0, 12);
        String addressFourOpenStatus = addressFour.substring(0, 4);

        String nowLightState = "0";
        //0、关灯；1、全红；2、全绿；3、全黄；4、红绿；5、红黄；6、绿红；7、绿黄；8、黄红；9、黄绿；
        if (addressOneOpenStatus.equals("0009") && addressTwoOpenStatus.equals("0000")
                && addressThreeOpenStatus.equals("0009") && addressFourOpenStatus.equals("0000")) {
            nowLightState = "1";
        } else if (addressOneOpenStatus.equals("0000") && addressTwoOpenStatus.equals("0009")
                && addressThreeOpenStatus.equals("0000") && addressFourOpenStatus.equals("0009")) {
            nowLightState = "2";
        } else if (addressOneOpenStatus.equals("0009") && addressTwoOpenStatus.equals("0009")
                && addressThreeOpenStatus.equals("0009") && addressFourOpenStatus.equals("0009")) {
            nowLightState = "3";
        } else if (addressOneOpenStatus.equals("0009") && addressTwoOpenStatus.equals("0000")
                && addressThreeOpenStatus.equals("0000") && addressFourOpenStatus.equals("0009")) {
            nowLightState = "4";
        } else if (addressOneOpenStatus.equals("0009") && addressTwoOpenStatus.equals("0000")
                && addressThreeOpenStatus.equals("0009") && addressFourOpenStatus.equals("0009")) {
            nowLightState = "5";
        } else if (addressOneOpenStatus.equals("0000") && addressTwoOpenStatus.equals("0009")
                && addressThreeOpenStatus.equals("0009") && addressFourOpenStatus.equals("0000")) {
            nowLightState = "6";
        } else if (addressOneOpenStatus.equals("0000") && addressTwoOpenStatus.equals("0009")
                && addressThreeOpenStatus.equals("0009") && addressFourOpenStatus.equals("0009")) {
            nowLightState = "7";
        } else if (addressOneOpenStatus.equals("0009") && addressTwoOpenStatus.equals("0009")
                && addressThreeOpenStatus.equals("0009") && addressFourOpenStatus.equals("0000")) {
            nowLightState = "8";
        } else if (addressOneOpenStatus.equals("0009") && addressTwoOpenStatus.equals("0009")
                && addressThreeOpenStatus.equals("0000") && addressFourOpenStatus.equals("0009")) {
            nowLightState = "9";
        }
        saveDataIntoSdDeviceData(sdDevices, nowLightState, DevicesTypeItemEnum.JING_SHI_DENG_DAI.getCode());
//        sendDataToWanJi(sdDevices, "lightOff", "0");
        return;
    }
}
