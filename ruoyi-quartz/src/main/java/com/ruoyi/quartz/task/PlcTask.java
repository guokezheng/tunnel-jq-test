package com.ruoyi.quartz.task;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.redis.RedisCache;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.code.DataType;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeEnum;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeItemEnum;
import com.tunnel.business.domain.dataInfo.SdDeviceData;
import com.tunnel.business.domain.dataInfo.SdDeviceDataRecord;
import com.tunnel.business.domain.dataInfo.SdDeviceTypeItem;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.event.SdDeviceNowState;
import com.tunnel.business.mapper.dataInfo.SdDeviceDataMapper;
import com.tunnel.business.mapper.dataInfo.SdDeviceDataRecordMapper;
import com.tunnel.business.mapper.dataInfo.SdDeviceTypeItemMapper;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.service.digitalmodel.RadarEventService;
import com.tunnel.deal.plc.modbus.ModbusTcpMaster;
import com.tunnel.deal.plc.modbus.util.Modbus4jReadUtils;
import com.zc.common.core.websocket.WebSocketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
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

    @Autowired
    private SdDeviceTypeItemMapper sdDeviceTypeItemMapper;

    @Resource
    private RedisCache redisCache;

    //
    public void createModbusTcpMaster() {
//        ModbusTcpMaster.getInstance().init();
        System.out.println("本地环境不开启创建PLC客户端");
    }

    public void sendNowDeviceStatusByWebsocket(SdDevices sdDevices, String[] state, String type) {
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
        if (type.equals("fsfx")) {
            sdDeviceNowState.setFs(state[1]);
            sdDeviceNowState.setFx(state[2]);
        } else if (type.equals("covi")) {
            sdDeviceNowState.setCo(state[1]);
            sdDeviceNowState.setVi(state[2]);
        }
        dataList.add(sdDeviceNowState);
        jsonObject.put("deviceStatus", dataList);
        WebSocketService.broadcast("deviceStatus", jsonObject.toString());
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
                            insertIntoDeviceData(sdDevice, DevicesTypeItemEnum.PU_TONG_CHE_ZHI.getCode(), state);
                            //通过websocket推送实时状态
                            String[] states = new String[3];
                            states[0] = state;
                            sendNowDeviceStatusByWebsocket(sdDevice, states, "cz");
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
                            insertIntoDeviceData(sdDevice, DevicesTypeItemEnum.FENG_SU.getCode(), FS.toString());
                            String windDirection = "正向";
                            if (FX[0]) {
                                windDirection = "反向";
                            }
                            insertIntoDeviceData(sdDevice, DevicesTypeItemEnum.FENG_XIANG.getCode(), windDirection);
                            //通过websocket推送实时状态
                            SdDeviceTypeItem sdDeviceTypeItem = sdDeviceTypeItemMapper.selectSdDeviceTypeItemById(Long.valueOf(DevicesTypeItemEnum.FENG_SU.getCode()));
                            String state = FS.toString() + sdDeviceTypeItem.getUnit() + "/" + windDirection;
                            String[] states = new String[3];
                            states[0] = state;
                            states[1] = FS.toString() + sdDeviceTypeItem.getUnit();
                            states[2] = windDirection;
                            sendNowDeviceStatusByWebsocket(sdDevice, states, "fsfx");
                            //推送数据到万集
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
                        insertIntoDeviceData(sdDevice, DevicesTypeItemEnum.LIANG_DU_INSIDE.getCode(), number.toString());
                        //通过websocket推送实时状态
                        String state = number.toString() + "lux";
                        String[] states = new String[3];
                        states[0] = state;
                        sendNowDeviceStatusByWebsocket(sdDevice, states, "brightnessInside");
                        //推送数据到万集
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
                        insertIntoDeviceData(sdDevice, DevicesTypeItemEnum.LIANG_DU_OUTSIDE.getCode(), number.toString());
                        //通过websocket推送实时状态
                        String state = number.toString() + "cd/㎡";
                        String[] states = new String[3];
                        states[0] = state;
                        sendNowDeviceStatusByWebsocket(sdDevice, states, "brightnessOutside");
                        //推送数据到万集
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
                            insertIntoDeviceData(sdDevice, DevicesTypeItemEnum.CO.getCode(), CO.toString());
                            insertIntoDeviceData(sdDevice, DevicesTypeItemEnum.VI.getCode(), VI.toString());
                            //通过websocket推送实时状态
                            String[] states = new String[3];
                            SdDeviceTypeItem sdDeviceTypeItem = sdDeviceTypeItemMapper.selectSdDeviceTypeItemById(Long.valueOf(DevicesTypeItemEnum.CO.getCode()));
                            String state = "CO:" + CO.toString() + sdDeviceTypeItem.getUnit() + "/";
                            states[1] = CO.toString() + sdDeviceTypeItem.getUnit();
                            sdDeviceTypeItem = sdDeviceTypeItemMapper.selectSdDeviceTypeItemById(Long.valueOf(DevicesTypeItemEnum.VI.getCode()));
                            state = state + "VI:" + VI.toString() + sdDeviceTypeItem.getUnit();
                            states[2] = VI.toString() + sdDeviceTypeItem.getUnit();
                            states[0] = state;
                            sendNowDeviceStatusByWebsocket(sdDevice, states, "covi");
                            //推送数据到万集
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

    public String getCheZhiState(boolean fHong, boolean fLv, boolean zHong, boolean zLv) {
        String state = "";
        if (zLv && fHong) {
            state = "1";
        } else if (zHong && fLv) {
            state = "2";
        } else if (zHong && fHong) {
            state = "3";
        } else if (zLv && (fHong == false && fLv == false)) {
            state = "5";
        } else if (zHong && (fHong == false && fLv == false)) {
            state = "6";
        } else if (fLv && (zHong == false && zLv == false)) {
            state = "7";
        } else if (fHong && (zHong == false && zLv == false)) {
            state = "7";
        } else {
            state = "4";
        }
        return state;
    }

    public void insertIntoDeviceData(SdDevices devices, int itemId, String value) {
        //实时数据存储
        SdDeviceData sdDeviceData = new SdDeviceData();
        sdDeviceData.setDeviceId(devices.getEqId());
        sdDeviceData.setItemId(Long.valueOf(itemId));
        SdDeviceData deviceData = sdDeviceDataMapper.selectLastRecord(sdDeviceData);
        if (deviceData != null) {
            deviceData.setData(value);
            deviceData.setUpdateTime(new Date());
            sdDeviceDataMapper.updateSdDeviceData(deviceData);
        } else {
            sdDeviceData.setData(value);
            sdDeviceData.setUpdateTime(new Date());
            sdDeviceDataMapper.insertSdDeviceData(sdDeviceData);
        }
        //redis存储设备实时数据
        redisCache.setCacheMapValue("deviceData",sdDeviceData.getDeviceId()+"-"+sdDeviceData.getItemId(),sdDeviceData);
        //每5分钟记录一次数据存储，车指不需要存储
        if (DevicesTypeItemEnum.PU_TONG_CHE_ZHI.getCode() == itemId) {
            return;
        }
        SdDeviceDataRecord sdDeviceDataRecord = new SdDeviceDataRecord();
        sdDeviceDataRecord.setDeviceId(devices.getEqId());
        sdDeviceDataRecord.setItemId(Long.valueOf(itemId));
        List<SdDeviceDataRecord> sdDeviceDataRecords = sdDeviceDataRecordMapper.selectSdDeviceDataRecordList(sdDeviceDataRecord);
        if (sdDeviceDataRecords.size() > 0) {
            SdDeviceDataRecord deviceDataRecord = sdDeviceDataRecords.get(sdDeviceDataRecords.size() - 1);
            Date createTime = deviceDataRecord.getCreateTime();
            if (createTime != null) {
                Date nowdate = new Date();
                long sec = (nowdate.getTime() - createTime.getTime()) / 1000L / 60L;
                if (sec < 5L) {
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
