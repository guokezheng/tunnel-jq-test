package com.ruoyi.quartz.task;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.utils.SecurityUtils;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 疏散标志定时任务调度
 */
@Component("evacuationSignTask")
public class EvacuationSignTask {

    @Value("${authorize.name}")
    private String deploymentType;

    private static final Logger log = LoggerFactory.getLogger(EvacuationSignTask.class);

    private static SdDeviceDataMapper deviceDataMapper = SpringUtils.getBean(SdDeviceDataMapper.class);
    private static RadarEventService radarEventService = SpringUtils.getBean(RadarEventService.class);
    private static SendDeviceStatusToKafkaService sendData = SpringUtils.getBean(SendDeviceStatusToKafkaService.class);
    private static ISdDevicesService sdDevicesService = SpringUtils.getBean(ISdDevicesService.class);

    @Value("${spring.profiles.active}")
    private String active;

    public void handle() {
        //定时获取疏散当前状态
        Long evacuationSignTypeId = Long.valueOf(DevicesTypeEnum.SHU_SAN_BIAO_ZHI_CONTROL.getCode());
        SdDevices sdDevices = new SdDevices();
        sdDevices.setEqType(evacuationSignTypeId);

        List<SdDevices> evacuationSignDevicesList = sdDevicesService.selectSdDevicesList(sdDevices);
        if(active.equals("wzbprod") ){
            evacuationSignDevicesList = evacuationSignDevicesList.stream().filter(devices -> devices.getEqTunnelId().equals("JQ-JiNan-WenZuBei-MJY")).collect(Collectors.toList());
        }else if(active.equals("ytsprod")){
            evacuationSignDevicesList = evacuationSignDevicesList.stream().filter(devices -> devices.getEqTunnelId().equals("JQ-WeiFang-YangTianShan-SZS")
                    ||devices.getEqTunnelId().equals("JQ-WeiFang-YangTianShan-YTS")).collect(Collectors.toList());
        }else if(active.equals("thprod")){
            evacuationSignDevicesList = evacuationSignDevicesList.stream().filter(devices -> devices.getEqTunnelId().equals("JQ-ZiBo-TaiHe-PDS")
                    ||devices.getEqTunnelId().equals("JQ-ZiBo-TaiHe-QFL")).collect(Collectors.toList());
        }else if(active.equals("mzprod")){
            evacuationSignDevicesList = evacuationSignDevicesList.stream().filter(devices -> devices.getEqTunnelId().equals("JQ-WeiFang-MiaoZi-BJY")
                    ||devices.getEqTunnelId().equals("JQ-WeiFang-MiaoZi-WCL")).collect(Collectors.toList());
        }else if(active.equals("jlyprod")){
            evacuationSignDevicesList = evacuationSignDevicesList.stream().filter(devices -> devices.getEqTunnelId().equals("JQ-WeiFang-JiuLongYu-JJL")
                    ||devices.getEqTunnelId().equals("JQ-WeiFang-JiuLongYu-MAS")||devices.getEqTunnelId().equals("JQ-WeiFang-JiuLongYu-HSD")).collect(Collectors.toList());
        }
        for (int i = 0;i < evacuationSignDevicesList.size();i++) {
            SdDevices devices = evacuationSignDevicesList.get(i);
            if (devices.getIp() == null && devices.getPort() == null
                    && ("").equals(devices.getIp()) && ("").equals(devices.getPort())) {
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
        SdDevices sonDevices = new SdDevices();
        sonDevices.setFEqId(sdDevices.getEqId());
        sonDevices.setEqStatus(sdDevices.getEqStatus());
        sdDevicesService.updateSdDevicesByFEqId(sonDevices);
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
        //todo 万集数据推送
        //radarEventService.sendBaseDeviceStatus(map);
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
        } else if (mode.equals("0")) {
            mode = "1";
        }
        String brightness = codeMap.get("brightness").toString();
        String frequency = codeMap.get("frequency").toString();
        if ((codeMap != null && !codeMap.isEmpty()) && codeMap.get("fireMark") != null
                && sdDevices.getEqType().longValue() == Long.valueOf(DevicesTypeEnum.SHU_SAN_BIAO_ZHI.getCode()).longValue()) {
            //控制器报警点存储
            saveDataIntoSdDeviceData(sdDevices, codeMap.get("fireMark").toString(), DevicesTypeItemEnum.EVACUATION_SIGN_FIREMARK.getCode());
            //根据控制器的设备ID查询所有子设备保存状态数据
            SdDevices dev = new SdDevices();
            dev.setFEqId(sdDevices.getEqId());
            List<SdDevices> list = sdDevicesService.selectSdDevicesList(dev);
            if (codeMap.get("fireMark").toString().equals("255") || codeMap.get("fireMark").toString().equals("0")) {
                //如果当前是开灯或关灯状态，所有的疏散标志子设备实时状态应该是一致的总共是两种状态，要不就都是开灯，要不就都是关灯
                if (codeMap.get("fireMark").toString().equals("0")) {
                    mode = "1";
                }
                for (int i = 0;i < list.size();i++) {
                    SdDevices devices = list.get(i);
                    saveDataIntoSdDeviceData(devices, codeMap.get("fireMark").toString(), DevicesTypeItemEnum.EVACUATION_SIGN_FIREMARK.getCode());
                    saveDataIntoSdDeviceData(devices, mode, DevicesTypeItemEnum.EVACUATION_SIGN_CONTROL_MODE.getCode());
//                    sendDataToWanJi(sdDevices, "lightOn", mode);
                    saveDataIntoSdDeviceData(devices, brightness, DevicesTypeItemEnum.EVACUATION_SIGN_BRIGHNESS.getCode());
                    saveDataIntoSdDeviceData(devices, frequency, DevicesTypeItemEnum.EVACUATION_SIGN_FREQUENCY.getCode());
                }
            } else {
                //此处表示标号处于0和255之前，疏散标志子设备包含三种状态，入口方向闪烁、闪烁、出口方向闪烁，对应标号处设备与控制器相同
                //其他标号处状态对应可控三种状态外的三种状态
                BigDecimal fireMark = new BigDecimal(codeMap.get("fireMark").toString());
                for (int i = 0;i < list.size();i++) {
                    SdDevices devices = list.get(i);
                    BigDecimal addressMark = new BigDecimal(devices.getQueryPointAddress());
                    if (fireMark.compareTo(addressMark) < 0) {
                        mode = "6";
                    } else if (fireMark.compareTo(addressMark) == 0) {
                        mode = "5";
                    } else if (fireMark.compareTo(addressMark) > 0) {
                        mode = "4";
                    }
                    saveDataIntoSdDeviceData(devices, codeMap.get("fireMark").toString(), DevicesTypeItemEnum.EVACUATION_SIGN_FIREMARK.getCode());
                    saveDataIntoSdDeviceData(devices, mode, DevicesTypeItemEnum.EVACUATION_SIGN_CONTROL_MODE.getCode());
//                    sendDataToWanJi(sdDevices, "lightOn", mode);
                    saveDataIntoSdDeviceData(devices, brightness, DevicesTypeItemEnum.EVACUATION_SIGN_BRIGHNESS.getCode());
                    saveDataIntoSdDeviceData(devices, frequency, DevicesTypeItemEnum.EVACUATION_SIGN_FREQUENCY.getCode());
                }
            }
            return;
        }
        saveDataIntoSdDeviceData(sdDevices, mode, DevicesTypeItemEnum.EVACUATION_SIGN_CONTROL_MODE.getCode());
        sendDataToWanJi(sdDevices, "lightOn", mode);
        saveDataIntoSdDeviceData(sdDevices, brightness, DevicesTypeItemEnum.EVACUATION_SIGN_BRIGHNESS.getCode());
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
            sendData.pushDevicesDataNowTime(data,sdDevices);
        } else {
            sdDeviceData.setData(value);
            sdDeviceData.setCreateTime(new Date());
            deviceDataMapper.insertSdDeviceData(sdDeviceData);
            sendData.pushDevicesDataNowTime(sdDeviceData,sdDevices);
        }
    }

    /**
     * 状态储存
     * @param sdDevices
     * @param value
     * @param itemId
     */
    private static void saveDataIntoSdDeviceDataTwo(SdDevices sdDevices, String value, Integer itemId) {
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

    public void sendCommand(SdDevices sdDevices, String ip, String portAddress) {
        if (ip == null || portAddress == null || "".equals(ip) || "".equals(portAddress)) {
            return;
        }
        Integer port = Integer.valueOf(portAddress);
        //查询子设备集合
        SdDevices dev = new SdDevices();
        dev.setFEqId(sdDevices.getEqId());
        //获取子设备
        List<SdDevices> list = sdDevicesService.selectSdDevicesList(dev);
        try {
            //当前状态查询
            Map codeMap = InductionlampUtil.getNowOpenState(ip, port);
            //状态入库
            String state = handleDeviceStatus(sdDevices, codeMap);
            //全部常亮
            if (state != "" && state.equals("2")) {
                //推送物联中台kafka内容
                saveDataIntoSdDeviceData(sdDevices, state, DevicesTypeItemEnum.EVACUATION_SIGN_CONTROL_MODE.getCode());
                //疏散标志是否开灯存储状态
                saveDataIntoSdDeviceDataTwo(sdDevices, state, DevicesTypeItemEnum.EVACUATION_SIGN_IS_OPEN.getCode());
                //疏散标志亮度存储状态
                saveDataIntoSdDeviceDataTwo(sdDevices, codeMap.get("brightness").toString(), DevicesTypeItemEnum.EVACUATION_SIGN_BRIGHNESS.getCode());
                //疏散标志事件标号
                saveDataIntoSdDeviceDataTwo(sdDevices, "0", DevicesTypeItemEnum.EVACUATION_SIGN_FREQUENCY.getCode());
                //疏散标志事件标号
                saveDataIntoSdDeviceDataTwo(sdDevices, "0", DevicesTypeItemEnum.EVACUATION_SIGN_FIREMARK.getCode());
                //更新子设备的状态
                if (!list.isEmpty()) {
                    for (int i = 0;i < list.size();i++) {
                        SdDevices devices = list.get(i);
                        devices.setEqStatus("1");
                        devices.setEqStatusTime(new Date());
                        sdDevicesService.updateSdDevices(devices);
                        //疏散标志控制模式
                        saveDataIntoSdDeviceData(devices, state, DevicesTypeItemEnum.EVACUATION_SIGN_CONTROL_MODE.getCode());
                        //疏散标志是否开灯存储状态
                        saveDataIntoSdDeviceDataTwo(devices, state, DevicesTypeItemEnum.EVACUATION_SIGN_IS_OPEN.getCode());
                        //疏散标志亮度存储状态
                        saveDataIntoSdDeviceDataTwo(devices, codeMap.get("brightness").toString(), DevicesTypeItemEnum.EVACUATION_SIGN_BRIGHNESS.getCode());
                        //疏散标志事件标号
                        saveDataIntoSdDeviceDataTwo(devices, "0", DevicesTypeItemEnum.EVACUATION_SIGN_FIREMARK.getCode());
                    }
                }
            } else if (state != "" && state.equals("1")) {//全部关闭
                //推送物联中台kafka内容
                saveDataIntoSdDeviceData(sdDevices, state, DevicesTypeItemEnum.EVACUATION_SIGN_CONTROL_MODE.getCode());
                //疏散标志是否开灯存储状态
                saveDataIntoSdDeviceDataTwo(sdDevices, state, DevicesTypeItemEnum.EVACUATION_SIGN_IS_OPEN.getCode());
                //疏散标志事件标号
                saveDataIntoSdDeviceDataTwo(sdDevices, "0", DevicesTypeItemEnum.EVACUATION_SIGN_FIREMARK.getCode());
                //更新子设备的状态
                if (!list.isEmpty()) {
                    for (int i = 0;i < list.size();i++) {
                        SdDevices devices = list.get(i);
                        devices.setEqStatus("1");
                        devices.setEqStatusTime(new Date());
                        sdDevicesService.updateSdDevices(devices);
                        //m模式疏散标志控制模式
                        saveDataIntoSdDeviceData(devices, state, DevicesTypeItemEnum.EVACUATION_SIGN_CONTROL_MODE.getCode());
                        //疏散标志是否开灯存储状态
                        saveDataIntoSdDeviceDataTwo(devices, state, DevicesTypeItemEnum.EVACUATION_SIGN_IS_OPEN.getCode());
                        //疏散标志事件标号
                        saveDataIntoSdDeviceDataTwo(devices, "0", DevicesTypeItemEnum.EVACUATION_SIGN_FIREMARK.getCode());
                    }
                }
            }else  if (state != "" && state.equals("5")) {

                list = list.stream()
                .sorted(Comparator.comparing(SdDevices::getPileNum))
                .collect(Collectors.toList());
                String fireMark = codeMap.get("fireMark").toString();
                //推送物联中台kafka内容
                saveDataIntoSdDeviceData(sdDevices, state, DevicesTypeItemEnum.EVACUATION_SIGN_CONTROL_MODE.getCode());
                //疏散标志是否开灯存储状态
                saveDataIntoSdDeviceDataTwo(sdDevices, state, DevicesTypeItemEnum.EVACUATION_SIGN_IS_OPEN.getCode());
                //疏散标志亮度存储状态
                saveDataIntoSdDeviceDataTwo(sdDevices, codeMap.get("brightness").toString(), DevicesTypeItemEnum.EVACUATION_SIGN_BRIGHNESS.getCode());
                //疏散标志事件标号
                saveDataIntoSdDeviceDataTwo(sdDevices, list.get(Integer.parseInt(fireMark)).getPile(), DevicesTypeItemEnum.EVACUATION_SIGN_FIREMARK.getCode());
                //疏散标志事件频率
                saveDataIntoSdDeviceDataTwo(sdDevices, codeMap.get("frequency").toString(), DevicesTypeItemEnum.EVACUATION_SIGN_FREQUENCY.getCode());
                //更新子设备的状态
                if (!list.isEmpty()) {
                    for (int i = 0;i < list.size();i++) {
                        SdDevices devices = list.get(i);
                        devices.setEqStatus("1");
                        devices.setEqStatusTime(new Date());
                        sdDevicesService.updateSdDevices(devices);
                        saveDataIntoSdDeviceData(devices, state, DevicesTypeItemEnum.EVACUATION_SIGN_CONTROL_MODE.getCode());
                        //疏散标志是否开灯存储状态
                        saveDataIntoSdDeviceDataTwo(devices, state, DevicesTypeItemEnum.EVACUATION_SIGN_IS_OPEN.getCode());
                        //疏散标志亮度存储状态
                        saveDataIntoSdDeviceDataTwo(devices, codeMap.get("brightness").toString(), DevicesTypeItemEnum.EVACUATION_SIGN_BRIGHNESS.getCode());
                        //疏散标志事件标号
                        saveDataIntoSdDeviceDataTwo(devices, list.get(Integer.parseInt(fireMark)).getPile(), DevicesTypeItemEnum.EVACUATION_SIGN_FIREMARK.getCode());
                        //疏散标志事件频率
                        saveDataIntoSdDeviceDataTwo(devices, codeMap.get("frequency").toString(), DevicesTypeItemEnum.EVACUATION_SIGN_FREQUENCY.getCode());
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }
}
