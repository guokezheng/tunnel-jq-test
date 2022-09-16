package com.ruoyi.quartz.task;

import com.alibaba.fastjson.JSONObject;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.code.DataType;
import com.tunnel.platform.datacenter.domain.enumeration.DevicesTypeEnum;
import com.tunnel.platform.datacenter.domain.enumeration.DevicesTypeItemEnum;
import com.tunnel.platform.domain.dataInfo.SdDeviceData;
import com.tunnel.platform.domain.dataInfo.SdDeviceDataRecord;
import com.tunnel.platform.domain.dataInfo.SdDevices;
import com.tunnel.platform.mapper.dataInfo.SdDeviceDataMapper;
import com.tunnel.platform.mapper.dataInfo.SdDeviceDataRecordMapper;
import com.tunnel.platform.service.dataInfo.ISdDevicesService;
import com.tunnel.platform.service.digitalmodel.RadarEventService;
import com.tunnel.plc.modbus.ModbusTcpMaster;
import com.tunnel.plc.modbus.util.Modbus4jReadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.*;

/**
 * PLC定时任务调度
 */
@Component("plcTask")
public class PlcTask {

    private static final Logger log = LoggerFactory.getLogger(PlcTask.class);

    @Autowired
    private ISdDevicesService sdDevicesService;

    @Autowired
    private SdDeviceDataMapper sdDeviceDataMapper;

    @Autowired
    private SdDeviceDataRecordMapper sdDeviceDataRecordMapper;

    @Autowired
    private RadarEventService radarEventService;

    //
    public void createModbusTcpMaster() {
        ModbusTcpMaster.getInstance().init();
    }

    public void modbusTcpDataHandle() {
        Map<String, ModbusMaster> masterMap = ModbusTcpMaster.masterMap;
        for (Map.Entry<String, ModbusMaster> entry : masterMap.entrySet()) {
            String plcId = entry.getKey();
            ModbusMaster master = entry.getValue();
            SdDevices sdDevices = new SdDevices();
            sdDevices.setFEqId(plcId);
            List<SdDevices> sdDevicesList = sdDevicesService.selectSdDevicesList(sdDevices);
            for (SdDevices sdDevice : sdDevicesList) {
                if (sdDevice.getEqType() == DevicesTypeEnum.PU_TONG_CHE_ZHI.getCode()) {
                    try {
                        boolean[] registers = Modbus4jReadUtils.readInputStatus(master, 1, 0, 100);
                        String[] split = sdDevice.getEqFeedbackAddress1().split(",");
                        if (split.length > 2) {
                            boolean bHong = registers[Integer.parseInt(split[0]) - 1];
                            boolean bLv = registers[Integer.parseInt(split[1]) - 1];
                            boolean zHong = registers[Integer.parseInt(split[2]) - 1];
                            boolean zLv = registers[Integer.parseInt(split[3]) - 1];
                            String state = getCheZhiState(bHong, bLv, zHong, zLv);
                            System.out.println("------------------------------------");
                            System.out.printf("桩号:" + sdDevice.getPile() + "，状态：" + state);
                            System.out.println("------------------------------------");
                            int itemId = DevicesTypeItemEnum.PU_TONG_CHE_ZHI.getCode();
                            insertIntoDeviceData(sdDevice, itemId, state);
                            //推送设备状态
                            Map<String, Object> map = new HashMap<>();
                            map.put("deviceId", sdDevice.getEqId());
                            map.put("deviceType", sdDevice.getEqType());
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("runStatus", state);
                            map.put("deviceData", jsonObject);
                            radarEventService.sendBaseDeviceStatus(map);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (sdDevice.getEqType() == DevicesTypeEnum.FENG_SU_FENG_XIANG.getCode()) {
                    try {
                        String[] split = sdDevice.getEqFeedbackAddress1().split(",");
                        if (split.length > 1) {
                            Number FS = Modbus4jReadUtils.readHoldingRegisterByDataType(master, 1, Integer.parseInt(split[0]) - 1, DataType.FOUR_BYTE_FLOAT);
                            boolean[] FX = Modbus4jReadUtils.readInputStatus(master, 1, Integer.parseInt(split[1]) - 1, 1);
                            System.out.println("------------------------------------");
                            System.out.printf("桩号:" + sdDevice.getPile() + "，模拟量：" + FS);
                            System.out.printf("桩号:" + sdDevice.getPile() + "，状态：" + FX);
                            System.out.println("------------------------------------");
                            int itemId = DevicesTypeItemEnum.FENG_SU.getCode();
                            insertIntoDeviceData(sdDevice, itemId, FS.toString());
                            itemId = DevicesTypeItemEnum.FENG_XIANG.getCode();
                            String windDirection = "正向";
                            if (FX[0]) {
                                windDirection = "反向";
                            }
                            insertIntoDeviceData(sdDevice, itemId, windDirection);
                            Map<String, Object> map = new HashMap<>();
                            map.put("deviceId", sdDevice.getEqId());
                            map.put("deviceType", sdDevice.getEqType());
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("windSpeed", FS.toString());
                            jsonObject.put("windDirection", windDirection);
                            map.put("deviceData", jsonObject);
                            radarEventService.sendBaseDeviceStatus(map);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (sdDevice.getEqType() == DevicesTypeEnum.LIANG_DU_JIAN_CE_INSIDE.getCode()) {
                    try {
                        Integer addr = Integer.valueOf(sdDevice.getEqFeedbackAddress1());
                        Number number = Modbus4jReadUtils.readHoldingRegisterByDataType(master, 1, addr - 1, DataType.FOUR_BYTE_FLOAT);
                        System.out.println("------------------------------------");
                        System.out.printf("桩号:" + sdDevice.getPile() + "，模拟量：" + number);
                        System.out.println("------------------------------------");
                        int itemId = DevicesTypeItemEnum.LIANG_DU_INSIDE.getCode();
                        insertIntoDeviceData(sdDevice, itemId, number.toString());
                        Map<String, Object> map = new HashMap<>();
                        map.put("deviceId", sdDevice.getEqId());
                        map.put("deviceType", sdDevice.getEqType());
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("runDate", number.toString());
                        jsonObject.put("unit", "lux");
                        map.put("deviceData", jsonObject);
                        radarEventService.sendBaseDeviceStatus(map);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (sdDevice.getEqType() == DevicesTypeEnum.LIANG_DU_JIAN_CE.getCode()) {
                    try {
                        Integer addr = Integer.valueOf(sdDevice.getEqFeedbackAddress1());
                        Number number = Modbus4jReadUtils.readHoldingRegisterByDataType(master, 1, addr - 1, DataType.FOUR_BYTE_FLOAT);
                        System.out.println("------------------------------------");
                        System.out.printf("桩号:" + sdDevice.getPile() + "，模拟量：" + number);
                        System.out.println("------------------------------------");
                        int itemId = DevicesTypeItemEnum.LIANG_DU_OUTSIDE.getCode();
                        insertIntoDeviceData(sdDevice, itemId, number.toString());
                        Map<String, Object> map = new HashMap<>();
                        map.put("deviceId", sdDevice.getEqId());
                        map.put("deviceType", sdDevice.getEqType());
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("runDate", number.toString());
                        jsonObject.put("unit", "cd/㎡");
                        map.put("deviceData", jsonObject);
                        radarEventService.sendBaseDeviceStatus(map);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (sdDevice.getEqType() == DevicesTypeEnum.CO_VI.getCode()) {
                    try {
                        String[] split = sdDevice.getEqFeedbackAddress1().split(",");
                        if (split.length > 1) {
                            Number CO = Modbus4jReadUtils.readHoldingRegisterByDataType(master, 1, Integer.parseInt(split[0]) - 1, DataType.FOUR_BYTE_FLOAT);
                            Number VI = Modbus4jReadUtils.readHoldingRegisterByDataType(master, 1, Integer.parseInt(split[1]) - 1, DataType.FOUR_BYTE_FLOAT);
                            System.out.println("------------------------------------");
                            System.out.printf("桩号:" + sdDevice.getPile() + "，模拟量：" + CO);
                            System.out.printf("桩号:" + sdDevice.getPile() + "，模拟量：" + VI);
                            System.out.println("------------------------------------");
                            int itemId = DevicesTypeItemEnum.CO.getCode();
                            insertIntoDeviceData(sdDevice, itemId, CO.toString());
                            itemId = DevicesTypeItemEnum.VI.getCode();
                            insertIntoDeviceData(sdDevice, itemId, VI.toString());
                            Map<String, Object> map = new HashMap<>();
                            map.put("deviceId", sdDevice.getEqId());
                            map.put("deviceType", sdDevice.getEqType());
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("CO", CO.toString());
                            jsonObject.put("VI", VI.toString());
                            map.put("deviceData", jsonObject);
                            radarEventService.sendBaseDeviceStatus(map);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public String getCheZhiState(boolean fHong,boolean fLv,boolean zHong,boolean zLv){
        String state="";
        if (zLv && fHong){
            state="1";
        }else if (zHong && fLv){
            state="2";
        }else if (zHong && fHong){
            state="3";
        }else if (zLv && (fHong==false && fLv==false)){
            state="5";
        }else if (zHong && (fHong==false && fLv==false) ){
            state="6";
        }else if (fLv && (zHong==false && zLv==false) ){
            state="7";
        }else if (fHong && (zHong==false && zLv==false) ){
            state="7";
        }else {
            state="4";
        }
        return state;
    }

    public void insertIntoDeviceData(SdDevices devices, int itemId, String value) {
        //实时数据存储
        SdDeviceData sdDeviceData = new SdDeviceData();
        sdDeviceData.setDeviceId(devices.getEqId());
        sdDeviceData.setItemId(Long.valueOf(itemId));
        SdDeviceData deviceData = sdDeviceDataMapper.selectLastRecord(sdDeviceData);
        sdDeviceData.setData(value);
        sdDeviceData.setUpdateTime(new Date());
        if(deviceData != null) {
            sdDeviceDataMapper.updateSdDeviceData(sdDeviceData);
        } else {
            sdDeviceDataMapper.insertSdDeviceData(sdDeviceData);
        }
        //每5分钟记录一次数据存储
        SdDeviceDataRecord sdDeviceDataRecord = new SdDeviceDataRecord();
        sdDeviceDataRecord.setDeviceId(devices.getEqId());
        sdDeviceDataRecord.setItemId(Long.valueOf(itemId));
        List<SdDeviceDataRecord> sdDeviceDataRecords = sdDeviceDataRecordMapper.selectSdDeviceDataRecordList(sdDeviceDataRecord);
        if (sdDeviceDataRecords.size() > 0) {
            SdDeviceDataRecord deviceDataRecord = sdDeviceDataRecords.get(sdDeviceDataRecords.size() - 1);
            Date createTime = deviceDataRecord.getCreateTime();
            if(createTime != null) {
                Date nowdate = new Date();
                long sec = (nowdate.getTime()-createTime.getTime()) / 1000L / 60L;
                if(sec < 5L){
                    log.info("当前设备类型为{}的数据记录时间小于5分钟，不需要新增数据", itemId);
                    return;
                }
            }
        }
        sdDeviceDataRecord.setData(value);
        sdDeviceDataRecord.setCreateTime(new Date());
        sdDeviceDataRecordMapper.insertSdDeviceDataRecord(sdDeviceDataRecord);
    }
}
