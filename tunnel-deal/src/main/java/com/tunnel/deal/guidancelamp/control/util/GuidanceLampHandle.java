package com.tunnel.deal.guidancelamp.control.util;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeEnum;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeItemEnum;
import com.tunnel.business.domain.dataInfo.SdDeviceData;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.mapper.dataInfo.SdDeviceDataMapper;
import com.tunnel.business.mapper.dataInfo.SdDevicesMapper;
import com.tunnel.business.service.digitalmodel.RadarEventService;
import com.tunnel.deal.guidancelamp.control.NettyClient;
import com.tunnel.deal.guidancelamp.control.inductionlamp.InductionlampUtil;
import org.springframework.scheduling.annotation.Async;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 *
 * 设备控制处理类
 * */
public class GuidanceLampHandle {

    /**
     * 懒汉模式实例化
     */
    private static GuidanceLampHandle instance;

    private static SdDeviceDataMapper deviceDataMapper = SpringUtils.getBean(SdDeviceDataMapper.class);

    private static RadarEventService radarEventService = SpringUtils.getBean(RadarEventService.class);

    private static SdDevicesMapper sdDevicesMapper = SpringUtils.getBean(SdDevicesMapper.class);

    private GuidanceLampHandle() {
    }

    public static GuidanceLampHandle getInstance() {
        if (instance == null) {
            instance = new GuidanceLampHandle();
        }
        return instance;
    }

    public void updateDeviceData(String deviceId, Long itemId, String value) {
        SdDeviceData sdDeviceData = new SdDeviceData();
        sdDeviceData.setDeviceId(deviceId);
        sdDeviceData.setItemId(itemId);
        List<SdDeviceData> deviceData = deviceDataMapper.selectSdDeviceDataList(sdDeviceData);
        if (deviceData.size() > 0) {
            SdDeviceData data = deviceData.get(0);
            data.setData(value);
            data.setUpdateTime(new Date());
            deviceDataMapper.updateSdDeviceData(data);
        }
    }

    public int toControlDev(String deviceId,Integer ctrState,SdDevices sdDevices,String brightness, String frequency, String fireMark) {
        String ip = sdDevices.getIp();
        Integer port = Integer.parseInt(sdDevices.getPort());
        if (sdDevices.getEqType().longValue() == DevicesTypeEnum.YOU_DAO_DENG_CONTROL.getCode().longValue()) {
            //发送诱导灯控制指令
            try {
                String code = "1GH+STATUS?\r\n";
                NettyClient client = new NettyClient(ip, port,code,1);
                client.start(null);
                Map codeMap = InductionlampUtil.getPilotLightMode(ctrState,Integer.parseInt(brightness),Integer.parseInt(frequency));
                client.pushCode(codeMap.get("code").toString());
                client.stop();
            } catch (Exception e) {
                System.err.println("设备编号为" + deviceId + "的设备变更状态失败");
                return 0;
            }
            //存储变更后控制器状态到数据库
            updateDeviceData(deviceId, Long.valueOf(DevicesTypeItemEnum.GUIDANCE_LAMP_CONTROL_MODE.getCode()), ctrState.toString());
            updateDeviceData(deviceId, Long.valueOf(DevicesTypeItemEnum.GUIDANCE_LAMP_BRIGHNESS.getCode()), brightness);
            updateDeviceData(deviceId, Long.valueOf(DevicesTypeItemEnum.GUIDANCE_LAMP_FREQUENCY.getCode()), frequency);
            //存储子级设备状态
            List<SdDevices> devicesListByFEqId = sdDevicesMapper.getDevicesListByFEqId(deviceId);
            for (int i = 0;i < devicesListByFEqId.size();i++) {
                String eqId = devicesListByFEqId.get(i).getEqId();
                updateDeviceData(eqId, Long.valueOf(DevicesTypeItemEnum.GUIDANCE_LAMP_CONTROL_MODE.getCode()), ctrState.toString());
                updateDeviceData(eqId, Long.valueOf(DevicesTypeItemEnum.GUIDANCE_LAMP_BRIGHNESS.getCode()), brightness);
                updateDeviceData(eqId, Long.valueOf(DevicesTypeItemEnum.GUIDANCE_LAMP_FREQUENCY.getCode()), frequency);
            }
        } else if (sdDevices.getEqType().longValue() == DevicesTypeEnum.SHU_SAN_BIAO_ZHI_CONTROL.getCode().longValue() && !fireMark.equals("")) {
            //发送疏散标志控制指令
            try {
                String code = "1GH+FIRE?\r\n";
                NettyClient client = new NettyClient(ip, port,code,1);
                client.start(null);
                Map codeMap = InductionlampUtil.getEvacuationSignLightMode(ctrState,Integer.parseInt(brightness),Integer.parseInt(frequency),fireMark);
                client.pushCode(codeMap.get("code").toString());
                client.stop();
            } catch (Exception e) {
                System.err.println("设备编号为" + deviceId + "的设备变更状态失败");
                return 0;
            }
            //存储变更后控制器状态到数据库
            updateDeviceData(deviceId, Long.valueOf(DevicesTypeItemEnum.EVACUATION_SIGN_CONTROL_MODE.getCode()), ctrState.toString());
            updateDeviceData(deviceId, Long.valueOf(DevicesTypeItemEnum.EVACUATION_SIGN_BRIGHNESS.getCode()), brightness);
            updateDeviceData(deviceId, Long.valueOf(DevicesTypeItemEnum.EVACUATION_SIGN_FREQUENCY.getCode()), frequency);
            updateDeviceData(deviceId, Long.valueOf(DevicesTypeItemEnum.EVACUATION_SIGN_FIREMARK.getCode()), fireMark);
            //存储子级设备状态
            List<SdDevices> devicesListByFEqId = sdDevicesMapper.getDevicesListByFEqId(deviceId);
//            for (int i = 0;i < devicesListByFEqId.size();i++) {
//                String eqId = devicesListByFEqId.get(i).getEqId();
//                updateDeviceData(eqId, Long.valueOf(DevicesTypeItemEnum.EVACUATION_SIGN_CONTROL_MODE.getCode()), ctrState.toString());
//                updateDeviceData(eqId, Long.valueOf(DevicesTypeItemEnum.EVACUATION_SIGN_BRIGHNESS.getCode()), brightness);
//                updateDeviceData(eqId, Long.valueOf(DevicesTypeItemEnum.EVACUATION_SIGN_FREQUENCY.getCode()), frequency);
//                updateDeviceData(eqId, Long.valueOf(DevicesTypeItemEnum.EVACUATION_SIGN_FIREMARK.getCode()), fireMark);
//            }
            String state = ctrState.toString();
            if (!devicesListByFEqId.isEmpty()) {
                //疏散标志关灯
                if (fireMark.equals("0") && !fireMark.equals("255")) {
                    state = "1";
                    for (int i = 0;i < devicesListByFEqId.size();i++) {
                        SdDevices devo = devicesListByFEqId.get(i);
                        updateDeviceDatas(devo, state, DevicesTypeItemEnum.EVACUATION_SIGN_CONTROL_MODE.getCode());
                        updateDeviceDatas(devo, brightness, DevicesTypeItemEnum.EVACUATION_SIGN_BRIGHNESS.getCode());
                        updateDeviceDatas(devo, frequency, DevicesTypeItemEnum.EVACUATION_SIGN_FREQUENCY.getCode());
                        updateDeviceDatas(devo, fireMark, DevicesTypeItemEnum.EVACUATION_SIGN_FIREMARK.getCode());
                    }
                    //疏散标志报警点更新
                } else if (!fireMark.equals("0") && !fireMark.equals("255")) {
                    BigDecimal fMark = new BigDecimal(fireMark);
                    for (int i = 0;i < devicesListByFEqId.size();i++) {
                        SdDevices devices = devicesListByFEqId.get(i);
                        BigDecimal addressMark = new BigDecimal(devices.getQueryPointAddress());
                        if (fMark.compareTo(addressMark) < 0) {
                            state = "6";
                        } else if (fMark.compareTo(addressMark) == 0) {
                            state = "5";
                        } else if (fMark.compareTo(addressMark) > 0) {
                            state = "4";
                        }
                        updateDeviceDatas(devices, fireMark, DevicesTypeItemEnum.EVACUATION_SIGN_FIREMARK.getCode());
                        updateDeviceDatas(devices, state, DevicesTypeItemEnum.EVACUATION_SIGN_CONTROL_MODE.getCode());
                        updateDeviceDatas(devices, brightness, DevicesTypeItemEnum.EVACUATION_SIGN_BRIGHNESS.getCode());
                        updateDeviceDatas(devices, frequency, DevicesTypeItemEnum.EVACUATION_SIGN_FREQUENCY.getCode());
                    }
                } else {
                    //疏散标志开灯无报警点
                    state = "2";
                    for (int i = 0;i < devicesListByFEqId.size();i++) {
                        SdDevices devo = devicesListByFEqId.get(i);
                        updateDeviceDatas(devo, state, DevicesTypeItemEnum.EVACUATION_SIGN_CONTROL_MODE.getCode());
                        updateDeviceDatas(devo, brightness, DevicesTypeItemEnum.EVACUATION_SIGN_BRIGHNESS.getCode());
                        updateDeviceDatas(devo, frequency, DevicesTypeItemEnum.EVACUATION_SIGN_FREQUENCY.getCode());
                        updateDeviceDatas(devo, fireMark, DevicesTypeItemEnum.EVACUATION_SIGN_FIREMARK.getCode());
                    }
                }
            }
        }
        //推送数据到万集
        if (ctrState != null && ctrState.toString().equals("1")) {
            sendDataToWanJi(sdDevices, "lightOff", "0");
        } else if (ctrState != null) {
            sendDataToWanJi(sdDevices, "lightOn", ctrState.toString());
        }
        return 1;
    }

    // 深圳显科诱导灯控制设备逻辑
    public int toControlXianKeDev(String deviceId,Integer ctrState,SdDevices sdDevices,String brightness, String frequency) {
        String ip = sdDevices.getIp();
        Integer port = Integer.parseInt(sdDevices.getPort());
        //发送诱导灯控制指令
        try {
            String code = "000000000006010300010001";
            NettyClient client = new NettyClient(ip, port,code,3);
            client.start(null);
            //控制亮度
            Map codeMap = InductionlampUtil.getXianKePilotLightMode(ctrState,Integer.parseInt(brightness));
            client.pushHexCode(codeMap.get("code").toString());
            //控制频率（1S闪几次）
            codeMap = InductionlampUtil.getXianKeFrequency(ctrState,Integer.parseInt(frequency));
            client.pushHexCode(codeMap.get("code").toString());
            //控制占空比（亮的时间在一整个周期的比例）
            /*codeMap = InductionlampUtil.getXianKeDutyCycle(ctrState,Integer.parseInt(frequency));
            client.pushHexCode(codeMap.get("code").toString());*/
            client.stop();
        } catch (Exception e) {
            System.err.println("设备编号为" + deviceId + "的设备变更状态失败");
            return 0;
        }
        //存储变更后控制器状态到数据库
        updateDeviceData(deviceId, Long.valueOf(DevicesTypeItemEnum.GUIDANCE_LAMP_CONTROL_MODE.getCode()), ctrState.toString());
        updateDeviceData(deviceId, Long.valueOf(DevicesTypeItemEnum.GUIDANCE_LAMP_BRIGHNESS.getCode()), brightness);
        updateDeviceData(deviceId, Long.valueOf(DevicesTypeItemEnum.GUIDANCE_LAMP_FREQUENCY.getCode()), frequency);
        //存储子级设备状态
        List<SdDevices> devicesListByFEqId = sdDevicesMapper.getDevicesListByFEqId(deviceId);
        for (int i = 0;i < devicesListByFEqId.size();i++) {
            String eqId = devicesListByFEqId.get(i).getEqId();
            updateDeviceData(eqId, Long.valueOf(DevicesTypeItemEnum.GUIDANCE_LAMP_CONTROL_MODE.getCode()), ctrState.toString());
            updateDeviceData(eqId, Long.valueOf(DevicesTypeItemEnum.GUIDANCE_LAMP_BRIGHNESS.getCode()), brightness);
            updateDeviceData(eqId, Long.valueOf(DevicesTypeItemEnum.GUIDANCE_LAMP_FREQUENCY.getCode()), frequency);
        }
        return 1;
    }

    @Async("induceExecutor")
    public void syncYdd(SdDevices sdDevices){
        String ip = sdDevices.getIp();

        Integer port = Integer.parseInt(sdDevices.getPort());
        try {
            String code = "000000000006010300010001";
            NettyClient client = new NettyClient(ip, port,code,3);
            client.start(null);
            //控制同步时间指令
            client.pushHexCode("000000000006010600020001");
            System.out.println(ip + "：执行了");
            client.stop();
        } catch (Exception e) {
            System.err.println("设备编号为" + sdDevices.getEqId() + "的设备变更状态失败");
        }

    }

    private void updateDeviceDatas(SdDevices sdDevices, String value, Integer itemId) {
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

    private void sendDataToWanJi(SdDevices sdDevices, String runStatus, String runMode) {
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

}
