package com.ruoyi.quartz.task;

import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.tunnel.platform.business.instruction.EquipmentControlInstruction;
import com.tunnel.platform.domain.dataInfo.SdDataTrafficHour;
import com.tunnel.platform.domain.dataInfo.SdDevices;
import com.tunnel.platform.domain.dataInfo.SdEquipmentState;
import com.tunnel.platform.domain.dataInfo.SdStateStorage;
import com.tunnel.platform.domain.event.SdStrategy;
import com.tunnel.platform.domain.event.SdStrategyRl;
import com.tunnel.platform.service.dataInfo.*;
import com.tunnel.platform.service.event.ISdStrategyRlService;
import com.tunnel.platform.service.event.ISdStrategyService;
import com.tunnel.platform.service.event.ISdWarningInfoService;
import com.tunnel.platform.service.logRecord.ISdOperationLogService;
import com.tunnel.platform.utils.mpdbus4j.Modbus4jReadUtils;
import com.tunnel.platform.utils.mpdbus4j.ModbusTcpMaster;
import com.tunnel.platform.utils.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.ruoyi.common.utils.StringUtils;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 定时任务调度测试
 *
 */
@Component("ryTask")
public class RyTask {

    private static final Logger log = LoggerFactory.getLogger(RyTask.class);

    @Autowired
    private ISdStrategyService sdStrategyService;
    @Autowired
    private ISdStrategyRlService sdStrategyRlService;
    @Autowired
    private ISdEquipmentStateService sdEquipmentStateService;
    @Autowired
    private ISdOperationLogService sdOperationLogService;
    @Autowired
    private ISdDevicesService sdDevicesService;
    @Autowired
    private ISdWarningInfoService sdWarningInfoService;
    @Autowired
    private ISdDataTrafficHourService sdDataTrafficHourService;
    @Autowired
    private ISdDataTrafficDayService sdDataTrafficDayService;
    @Autowired
    private ISdDataTrafficMonthService sdDataTrafficMonthService;
    @Autowired
    private ISdStateStorageService sdStateStorageService;

    private static  ModbusMaster master = ModbusTcpMaster.getMaster();

    public void ryMultipleParams(String s, Boolean b, Long l, Double d, Integer i) {
        System.out.println(StringUtils.format("执行多参方法： 字符串类型{}，布尔类型{}，长整型{}，浮点型{}，整形{}", s, b, l, d, i));
    }

    public void ryParams(String params) {
       /* if (!master.isInitialized()){
             master = ModbusTcpMaster.getMaster("10.3.19.150",502);
        }*/
        if (params.equals("cheZhiType_1")){
            int offset=0;
            try{
                boolean[] registers = Modbus4jReadUtils.readInputStatus(master,1, offset, 100);
                String[] types={"1","17"};//1：车指 17：风向
                List<SdDevices> sdDevicesArr = sdDevicesService.selectEqListByEqTypes(types);
                for (int i = 0; i <sdDevicesArr.size(); i++) {
                    SdDevices devices = sdDevicesArr.get(i);
                    String state="";
                    if (devices.getEqType()==1L){
                        Integer eqFeedbackAddress1 =Integer.parseInt(devices.getEqFeedbackAddress1()) ;
                        boolean fHong = registers[eqFeedbackAddress1];
                        boolean fLv = registers[eqFeedbackAddress1+1];
                        boolean zHong = registers[eqFeedbackAddress1+2];
                        boolean zLv = registers[eqFeedbackAddress1+3];
                        state = getCheZhiState(fHong, fLv, zHong, zLv);
                    }else if (devices.getEqType()==17L){
                        Integer eqFeedbackAddress2 =Integer.parseInt(devices.getEqFeedbackAddress2()) ;
                        boolean fx = registers[eqFeedbackAddress2];
                        if (fx){
                            state="反";
                        }else {
                            state="正";
                        }
                        //查询存在的风速
                        SdStateStorage storage = sdStateStorageService.selectSdStateStorage(devices.getEqId());
                        if (storage!=null){
                            String[] split = storage.getState().split(",");
                            //风速、风向
                            state= split[0]+","+state;
                        }
                    }
                    updateSdStateStorage(devices.getEqId(),state);
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }else if (params.equals("hj_CO_VI_WS_DN_DW")){
            String[] types={"19","17","5"};//19:CO_VI 17:FS 5:DN_DW
            List<SdDevices> sdDevicesArr = sdDevicesService.selectEqListByEqTypes(types);
            int offset=0;
            try{
                short[] revShorts = Modbus4jReadUtils.readHoldingRegister( master,1, 0, 10);
                List<String> co_vi_ws_dn_dw = getCo_Vi_WS_DN_DW(offset, revShorts);
                for (int i = 0; i <sdDevicesArr.size(); i++) {
                    SdDevices devices = sdDevicesArr.get(i);
                    String state="";
                    if (devices.getEqType()==19L || devices.getEqType()==5L){
                        Integer eqFeedbackAddress1 =Integer.parseInt(devices.getEqFeedbackAddress1()) ;
                        Integer eqFeedbackAddress2 =Integer.parseInt(devices.getEqFeedbackAddress2()) ;
                        state = co_vi_ws_dn_dw.get(eqFeedbackAddress1)+","+co_vi_ws_dn_dw.get(eqFeedbackAddress2);
                    }else if (devices.getEqType()==17L){
                        Integer eqFeedbackAddress1 =Integer.parseInt(devices.getEqFeedbackAddress1()) ;
                        //查询存在的风向
                        SdStateStorage storage = sdStateStorageService.selectSdStateStorage(devices.getEqId());
                        if (storage!=null){
                            String[] split = storage.getState().split(",");
                            //风速、风向
                            state= co_vi_ws_dn_dw.get(eqFeedbackAddress1)+","+split[1];
                        }
                    }
                    updateSdStateStorage(devices.getEqId(),state);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    public void updateSdStateStorage(String deviceId,String state) {
        SdStateStorage sdStateStorage=new SdStateStorage();
        sdStateStorage.setDeviceId(deviceId);
        sdStateStorage.setState(state);
        sdStateStorageService.updateSdStateStorage(sdStateStorage);
    }
    public void ryNoParams() {
        System.out.println("执行无参方法");
    }

    public void strategyParams(String params) {
        System.out.println("执行无参方法");
        EquipmentControlInstruction instruc = new EquipmentControlInstruction();
        SdStrategy strategy = sdStrategyService.selectSdStrategyByJobRelationId(params);
        String strategyType = strategy.getStrategyType();
        String tunnelId = strategy.getTunnelId();
        SdStrategyRl rl = new SdStrategyRl();
        List<SdStrategyRl> ssgyRlList = sdStrategyRlService.selectSdStrategyRlList(rl);
        for (int i = 0; i < ssgyRlList.size(); i++) {
            //设备类型
            Long deviceTypeId = Long.parseLong(ssgyRlList.get(i).getEqTypeId());
            //设备状态码
            String codeDeviceState = ssgyRlList.get(i).getState();
            String eqs = ssgyRlList.get(i).getEquipments();
            String[] eqss = eqs.split(",");
            //设备list
            List<String> eqId = new ArrayList<String>();
            for (int j = 0; j < eqss.length; j++) {
                if ("全选".equals(eqss[j])){
                    continue;
                }
                eqId.add(eqss[j]);
            }
            SdEquipmentState state = new SdEquipmentState();
            state.setStateTypeId(deviceTypeId);
            state.setDeviceState(codeDeviceState);
            List<SdEquipmentState> stateList = sdEquipmentStateService.selectSdEquipmentStateList(state);
            //状态名称
            String stateName = stateList.get(0).getStateName();
            System.out.println("定时执行策略，调用发送指令接口【方法前】=====>>>guid：" + params);
            //调用发送指令
            instruc.controlInstruction(deviceTypeId, tunnelId, eqId, strategyType, stateName, codeDeviceState);
            System.out.println("定时执行策略，调用发送指令接口【结束】=====>>>guid：" + params);
        }
    }

    public static List<String> getCo_Vi_WS_DN_DW(int offset,short[] revShorts) throws IOException {
        List<String> strArr = new ArrayList<>();
        String hexString = "";
        for (int i = 0; i < revShorts.length; i++) {
            System.out.println("读取寄存器数据:" +(offset++)+">>"+ revShorts[i]);
            hexString = StringUtil.hexStringByShort(revShorts[i],2);
            String hex1 = StringUtil.hexStringByShort(revShorts[i+1],2);
            if (hex1.contains("-")){
                BigInteger bi = new BigInteger(hex1, 16);
                String string = Long.toHexString(0x100000000L + bi.longValue());
                hexString+=string.substring(string.length()-5,string.length());
            }else {
                hexString+=StringUtil.hexStringByShort(revShorts[i+1],2);
            }
            Float aFloat = StringUtil.HexConvertToString(hexString);
            String format = String.format("%.3f", aFloat);
            i++;
            strArr.add(format);
        }
        return strArr;
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
