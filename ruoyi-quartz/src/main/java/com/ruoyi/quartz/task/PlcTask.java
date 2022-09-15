package com.ruoyi.quartz.task;

import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.code.DataType;
import com.tunnel.platform.datacenter.domain.enumeration.DevicesTypeEnum;
import com.tunnel.platform.domain.dataInfo.SdDevices;
import com.tunnel.platform.service.dataInfo.ISdDevicesService;
import com.tunnel.plc.modbus.ModbusTcpMaster;
import com.tunnel.plc.modbus.util.Modbus4jReadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.*;

/**
 * PLC定时任务调度
 */
@Component("plcTask")
public class PlcTask {


    @Autowired
    private ISdDevicesService sdDevicesService;

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
                        }
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
}
