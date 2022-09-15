package com.ruoyi.quartz.task;

import com.serotonin.modbus4j.ModbusMaster;
import com.tunnel.platform.service.dataInfo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.ruoyi.common.utils.StringUtils;

import java.util.*;

/**
 * 定时任务调度测试
 *
 */
@Component("ryTask")
public class RyTask {

    private static final Logger log = LoggerFactory.getLogger(RyTask.class);

    private static final List<Long> deviceTypeList = new ArrayList<>(Arrays.asList(5L, 17L, 19L));

    private boolean isCheckState = false;

    @Autowired
    private ISdDevicesService sdDevicesService;

    private static  ModbusMaster master = null;

    public void ryMultipleParams(String s, Boolean b, Long l, Double d, Integer i) {
        System.out.println(StringUtils.format("执行多参方法： 字符串类型{}，布尔类型{}，长整型{}，浮点型{}，整形{}", s, b, l, d, i));
    }

//    public void ryParams(String params) {
//       if (params.equals("is_host_alive")) {
//            SdDevices sdDevices = new SdDevices();
//            long hostType = Long.parseLong(String.valueOf(DevicesTypeEnum.HUO_ZAI_BAO_JING_HOST.getCode()));
//            sdDevices.setEqType(hostType);
//            List<SdDevices> devicesList = sdDevicesService.selectSdDevicesList(sdDevices);
//            for (int i = 0;i < devicesList.size();i++) {
//                if (FireNettyServerHandler.clients.size() == 0){
//                    //消防主机已经离线，更改设备状态
//                    SdDevices devices = devicesList.get(i);
//                    devices.setEqStatus("2");
//                    devices.setEqStatusTime(new Date());
//                    sdDevicesService.updateSdDevices(devices);
//                }
//            }
//        } else if (params.equals("check_devices_is_online")) {
//            for (int i = 0;i < deviceTypeList.size();i++) {
//                Long deviceTypeId = deviceTypeList.get(i);
//                SdDevices sdDevices = new SdDevices();
//                sdDevices.setEqType(deviceTypeId);
//                List<SdDevices> devicesList = sdDevicesService.selectSdDevicesList(sdDevices);
//                for (int j = 0;j < devicesList.size();j++) {
//                    SdDevices devices = devicesList.get(j);
//                    String eqId = devices.getEqId();
//                    SdDeviceData sdDeviceData = new SdDeviceData();
//                    sdDeviceData.setDeviceId(eqId);
//                    SdDeviceData data = sdDeviceDataMapper.selectLastRecord(sdDeviceData);
//                    if (data != null) {
//                        Date createTime = data.getCreateTime();
//                        if(createTime != null){
//                            Date nowdate = new Date();
//                            long sec = (nowdate.getTime()-createTime.getTime()) / 1000L / 60L;
//                            if(sec > 5L){
//                                log.info("当前设备离线");
//                                if (devices.getEqStatus().equals("2")) {
//                                    return;
//                                }
//                                devices.setEqStatus("2");
//                                devices.setEqStatusTime(new Date());
//                                sdDevicesService.updateSdDevices(devices);
//                            } else {
//                                if (devices.getEqStatus() == null || !devices.getEqStatus().equals("1")) {
//                                    devices.setEqStatus("1");
//                                    devices.setEqStatusTime(new Date());
//                                    sdDevicesService.updateSdDevices(devices);
//                                }
//                            }
//                        }
//                        isCheckState = true;
//                    }
//                }
//            }
//        }
//    }

//    public void insertIntoDeviceData(SdDevices devices, int itemId, String value) {
//        SdDeviceData sdDeviceData = new SdDeviceData();
//        sdDeviceData.setDeviceId(devices.getEqId());
//        sdDeviceData.setItemId(Long.valueOf(itemId));
//        SdDeviceData deviceData = sdDeviceDataMapper.selectLastRecord(sdDeviceData);
//        if(deviceData != null){
//            Date createTime = deviceData.getCreateTime();
//            if(createTime != null) {
//                Date nowdate = new Date();
//                long sec = (nowdate.getTime()-createTime.getTime()) / 1000L / 60L;
//                if(sec < 5L){
//                    log.info("当前设备类型为{}的数据记录时间小于5分钟，不需要新增数据", itemId);
//                    return;
//                }
//            }
//        }
//        sdDeviceData.setData(value);
//        sdDeviceData.setCreateTime(new Date());
//        sdDeviceDataMapper.insertSdDeviceData(sdDeviceData);
//    }


    public void ryNoParams() {
        System.out.println("执行无参方法");
    }



}
