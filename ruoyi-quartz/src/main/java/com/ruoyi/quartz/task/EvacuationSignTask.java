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
import com.tunnel.deal.guidancelamp.control.inductionlamp.InductionlampUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 疏散标志定时任务调度
 */
@Component("evacuationSignTask")
public class EvacuationSignTask {
    private static final Logger log = LoggerFactory.getLogger(EvacuationSignTask.class);

    @Autowired
    private ISdDevicesService sdDevicesService;

    private static SdDeviceDataMapper deviceDataMapper = SpringUtils.getBean(SdDeviceDataMapper.class);
    private static RadarEventService radarEventService = SpringUtils.getBean(RadarEventService.class);
    private static SendDeviceStatusToKafkaService sendData = SpringUtils.getBean(SendDeviceStatusToKafkaService.class);

    public void handle() {
        //定时获取疏散当前状态
        Long evacuationSignTypeId = Long.valueOf(DevicesTypeEnum.SHU_SAN_BIAO_ZHI.getCode());
        SdDevices sdDevices = new SdDevices();
        sdDevices.setEqType(evacuationSignTypeId);
        List<SdDevices> evacuationSignDevicesList = sdDevicesService.selectSdDevicesList(sdDevices);
        for (int i = 0;i < evacuationSignDevicesList.size();i++) {
            SdDevices devices = evacuationSignDevicesList.get(i);
            if (devices.getIp() == null && devices.getPort() == null
                    && ("").equals(devices.getIp()) && ("").equals(devices.getPort())) {
                continue;
            } else {
                //进行状态查询
//                sendCommand(devices, devices.getIp(), devices.getPort());
            }
        }
    }

    private String handleDeviceStatus(SdDevices sdDevices, Map<String, Object> codeMap) {
        String state = "";
        if (codeMap == null || codeMap.isEmpty()) {
            log.info("当前疏散标志控制器已经离线，存储状态到设备管理表中");
            sdDevices.setEqStatus(DevicesStatusEnum.DEVICE_OFF_LINE.getCode());
            sdDevices.setEqStatusTime(new Date());
            sendData.pushDevicesStatusToOtherSystem(sdDevices, "1", "off");
            sendData.pushDevicesStatusToOtherSystem(sdDevices, "2", "off");
        } else {
            if (codeMap.get("openState") != null) {
                state = codeMap.get("openState").toString();
            } else if (codeMap.get("runMode") != null) {
                state = codeMap.get("runMode").toString();
            } else if (codeMap.get("fireMark") != null) {
                state = codeMap.get("fireMark").toString();
            }
            sdDevices.setEqStatus(DevicesStatusEnum.DEVICE_ON_LINE.getCode());
            sdDevices.setEqStatusTime(new Date());
            sendData.pushDevicesStatusToOtherSystem(sdDevices, "1", "on");
            sendData.pushDevicesStatusToOtherSystem(sdDevices, "2", "on");
            log.info("当前设备在线更新设备管理表中的状态");
        }
        //更新疏散标志控制器状态，疏散标志子设备状态也需要更改
        sdDevicesService.updateSdDevices(sdDevices);
        sdDevicesService.updateSdDevicesByFEqId(sdDevices);
        return state;
    }

    private static void sendNowDeviceStatusByWebsocket(SdDevices sdDevices, String[] state) {
        List<SdDeviceNowState> dataList = new ArrayList<>();
        cn.hutool.json.JSONObject jsonObject = new cn.hutool.json.JSONObject();
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
        jsonObject.put("runMode", runMode);
        map.put("deviceData", jsonObject);
        radarEventService.sendBaseDeviceStatus(map);
    }

    private static void handleCodeMap(SdDevices sdDevices, Map<String, Object> codeMap) {
        if ((codeMap != null && !codeMap.isEmpty()) && codeMap.get("fireMark") != null
                && sdDevices.getEqType().longValue() == Long.valueOf(DevicesTypeEnum.SHU_SAN_BIAO_ZHI.getCode()).longValue()) {
            saveDataIntoSdDeviceData(sdDevices, codeMap.get("fireMark").toString(), DevicesTypeItemEnum.EVACUATION_SIGN_FIREMARK.getCode());
            if (codeMap.get("fireMark").toString().equals("255") || codeMap.get("fireMark").toString().equals("0")) {
                //如果当前是开灯或关灯状态，所有的疏散标志子设备实时状态应该是一致的总共是两种状态，要不就都是开灯，要不就都是关灯
            } else {
                //此处表示标号处于0和255之前，疏散标志子设备包含三种状态，入口方向闪烁、闪烁、出口方向闪烁
            }
            return;
        }
        if (codeMap == null || codeMap.isEmpty() || codeMap.get("mode") == null) {
            return;
        }
        String mode = codeMap.get("mode").toString();
        if (mode.equals("1")) {
            mode = "2";
        } else if (mode.equals("4")) {
            mode = "3";
        }
        saveDataIntoSdDeviceData(sdDevices, mode, DevicesTypeItemEnum.EVACUATION_SIGN_CONTROL_MODE.getCode());
        sendDataToWanJi(sdDevices, "lightOn", mode);
        String brightness = codeMap.get("brightness").toString();
        saveDataIntoSdDeviceData(sdDevices, brightness, DevicesTypeItemEnum.EVACUATION_SIGN_BRIGHNESS.getCode());
        String frequency = codeMap.get("frequency").toString();
        saveDataIntoSdDeviceData(sdDevices, frequency, DevicesTypeItemEnum.EVACUATION_SIGN_FREQUENCY.getCode());
        log.info("存储设备实时数据到sd_device_data完成");
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
        try {
            Map codeMap = InductionlampUtil.getNowOpenState(ip, port);
            String state = handleDeviceStatus(sdDevices, codeMap);
            if (state != "" && state.equals("1")) {
                saveDataIntoSdDeviceData(sdDevices, state, DevicesTypeItemEnum.EVACUATION_SIGN_IS_OPEN.getCode());
                codeMap = InductionlampUtil.getNowRunMode(ip, port);
                state = handleDeviceStatus(sdDevices, codeMap);
                String code = "";
                if (state != "" && state.equals("1")) {
                    //当前属于闪烁模式，查询当前闪灯模式、亮度、频率
                    code = "1GH+BMODE?,TPWM?,BFREQ?\r\n";
                    codeMap = InductionlampUtil.getNowModeAndParameter(code, ip, port);
                    handleDeviceStatus(sdDevices, codeMap);
                    //codeMap中包含的数据解析
                    handleCodeMap(sdDevices, codeMap);
                } else if (state != "" && state.equals("2")) {
                    //当前属于流水模式，查询当前闪灯模式、亮度、频率
                    code = "1GH+WMODE?,TPWM?,WFREQ?\r\n";
                    codeMap = InductionlampUtil.getNowModeAndParameter(code, ip, port);
                    handleDeviceStatus(sdDevices, codeMap);
                    //codeMap中包含的数据解析
                    handleCodeMap(sdDevices, codeMap);
                }
                //当前不管是什么模式，都需要查询报警标点
                codeMap = InductionlampUtil.getNowevacuationSignStatus(ip, port);
                handleDeviceStatus(sdDevices, codeMap);
                //此处仅处理获取到的标号信息
                //FIRE=255表示所有疏散标志全开灯，表示当前隧道状态为正常状态
                handleCodeMap(sdDevices, codeMap);
            } else if (state != "" && state.equals("0")) {
                saveDataIntoSdDeviceData(sdDevices, state, DevicesTypeItemEnum.EVACUATION_SIGN_IS_OPEN.getCode());
                saveDataIntoSdDeviceData(sdDevices, "1", DevicesTypeItemEnum.EVACUATION_SIGN_CONTROL_MODE.getCode());
                sendDataToWanJi(sdDevices, "lightOff", "0");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }
}
