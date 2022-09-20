package com.ruoyi.quartz.task;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeEnum;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeItemEnum;
import com.tunnel.business.domain.dataInfo.SdDeviceData;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.mapper.dataInfo.SdDeviceDataMapper;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.service.digitalmodel.RadarEventService;
import com.tunnel.deal.guidancelamp.control.inductionlamp.InductionlampUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//疏散标志定时任务调度
@Component("evacuationSignTask")
public class EvacuationSignTask {
    private static final Logger log = LoggerFactory.getLogger(EvacuationSignTask.class);

    @Autowired
    private ISdDevicesService sdDevicesService;

    private static SdDeviceDataMapper deviceDataMapper = SpringUtils.getBean(SdDeviceDataMapper.class);
    private static RadarEventService radarEventService = SpringUtils.getBean(RadarEventService.class);

    public void handle() {
        //定时获取疏散当前状态
        Long evacuationSignTypeId = Long.valueOf(DevicesTypeEnum.SHU_SAN_BIAO_ZHI.getCode());
        SdDevices sdDevices = new SdDevices();
        sdDevices.setEqType(evacuationSignTypeId);
        List<SdDevices> evacuationSignDevicesList = sdDevicesService.selectSdDevicesList(sdDevices);
        for (int i = 0;i < evacuationSignDevicesList.size();i++) {
            SdDevices devices = evacuationSignDevicesList.get(i);
            if (devices.getIp() == null && devices.getPort() == null && ("").equals(devices.getIp())) {
                continue;
            } else {
                //进行状态查询
                sendCommand(devices, devices.getIp(), Integer.parseInt(devices.getPort()));
            }
        }
    }

    private String handleDeviceStatus(SdDevices sdDevices, Map<String, Object> codeMap) {
        String state = "";
        if (codeMap.isEmpty()) {
            log.info("当前疏散标志控制器已经离线，存储状态到设备管理表中");
            sdDevices.setEqStatus("2");
            sdDevices.setEqStatusTime(new Date());
        } else {
            if (codeMap.get("openState") != null) {
                state = codeMap.get("openState").toString();
            } else if (codeMap.get("runMode") != null) {
                state = codeMap.get("runMode").toString();
            } else if (codeMap.get("fireMark") != null) {
                state = codeMap.get("fireMark").toString();
            }
            sdDevices.setEqStatus("1");
            sdDevices.setEqStatusTime(new Date());
            log.info("当前设备在线更新设备管理表中的状态");
        }
        sdDevicesService.updateSdDevices(sdDevices);
        return state;
    }

    private static void sendDataToWanJi(SdDevices sdDevices, String runStatus, String runMode) {
        Map<String, Object> map = new HashMap<>();
        map.put("deviceId", sdDevices.getEqId());
        map.put("deviceType", sdDevices.getEqType());
        JSONObject jsonObject = new JSONObject();
        if (runStatus.equals("lightOn")) {
            runStatus = "1";
        } else if (runStatus.equals("lightOff")) {
            runStatus = "0";
        }
        jsonObject.put("runStatus", Integer.valueOf(runStatus));
        jsonObject.put("runMode", runMode);
        map.put("deviceData", jsonObject);
        radarEventService.sendBaseDeviceStatus(map);
    }

    private static void handleCodeMap(SdDevices sdDevices, Map<String, Object> codeMap) {
        if (sdDevices.getEqType().longValue() == Long.valueOf(DevicesTypeEnum.SHU_SAN_BIAO_ZHI.getCode()).longValue()
                && codeMap.get("fireMark") != null) {
            saveDataIntoSdDeviceData(sdDevices, codeMap.get("fireMark").toString(), DevicesTypeItemEnum.EVACUATION_SIGN_FIREMARK.getCode());
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
        } else {
            sdDeviceData.setData(value);
            sdDeviceData.setCreateTime(new Date());
            deviceDataMapper.insertSdDeviceData(sdDeviceData);
        }
    }

    public void sendCommand(SdDevices sdDevices, String ip, Integer port) {
        try {
            Map codeMap = InductionlampUtil.getNowOpenState(ip, port);
            String state = handleDeviceStatus(sdDevices, codeMap);
            if (state != "" && state.equals("1")) {
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
                sendDataToWanJi(sdDevices, "lightOff", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }
}
