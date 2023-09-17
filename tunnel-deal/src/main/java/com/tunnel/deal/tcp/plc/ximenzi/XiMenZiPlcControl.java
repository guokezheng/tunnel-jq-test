package com.tunnel.deal.tcp.plc.ximenzi;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.AjaxResult;
import com.tunnel.business.datacenter.domain.enumeration.DevicePointControlTypeEnum;
import com.tunnel.business.datacenter.domain.enumeration.DeviceStateTypeEnum;
import com.tunnel.business.datacenter.domain.enumeration.OperationLogEnum;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.protocol.SdDevicePointPlc;
import com.tunnel.business.service.dataInfo.ISdDeviceDataService;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.service.protocol.ISdDevicePointPlcService;
import com.tunnel.business.strategy.service.CommonControlService;
import com.tunnel.deal.generalcontrol.GeneralControlBean;
import com.tunnel.deal.tcp.client.general.TcpClientGeneralBean;
import com.tunnel.deal.tcp.modbus.ModbusCmd;
import com.tunnel.deal.tcp.modbus.ModbusCmdResolver;
import com.tunnel.deal.tcp.modbus.ModbusFunctionCode;
import com.tunnel.deal.tcp.plc.ximenzi.task.XiMenZiPlcTask;
import com.tunnel.deal.tcp.util.Obj2ListUtil;
import com.tunnel.deal.tcp.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

/**
 * describe: 西门子PLC控制类
 *
 * @author zs
 * @date 2023/8/17
 */
@Component
public class XiMenZiPlcControl  implements GeneralControlBean, TcpClientGeneralBean {

    @Autowired
    private ISdDevicesService sdDevicesService;

    @Autowired
    private ISdDeviceDataService deviceDataService;

    @Autowired
    private CommonControlService commonControlService;

    @Autowired
    private ISdDevicePointPlcService devicePointPlcService;

    @Autowired
    private ModbusCmd modbusCmd;


    /**
     * 设备控制--工作台单个设备控制
     *
     * @param map
     * @param sdDevices
     * @return
     */
    @Override
    public AjaxResult control(Map<String, Object> map, SdDevices sdDevices) {
        //设备ID
        String devId = Optional.ofNullable(map.get("devId")).orElse("").toString();
        //设备状态
        String state = Optional.ofNullable(map.get("state")).orElse("").toString();

        Integer controlState = 0;

        boolean isopen = commonControlService.queryAnalogControlConfig();
        if (isopen) {
            //设备模拟控制开启，直接变更设备状态为在线并展示对应运行状态
            controlState = commonControlService.analogControl(map,sdDevices);
            return AjaxResult.success(controlState);
        }

        //控制设备之前获取设备状态
        String beforeState = commonControlService.selectBeforeState(sdDevices);



        AjaxResult ajaxResult = control(sdDevices,state);
        Integer code = Integer.valueOf(String.valueOf(ajaxResult.get("code")));
        if( code == HttpStatus.SUCCESS){
            controlState = Integer.valueOf(OperationLogEnum.STATE_SUCCESS.getCode());
        }
        //操作日志
        commonControlService.addOperationLog(map,sdDevices,beforeState,controlState);
        return AjaxResult.success(controlState);
    }

    /**
     * 设备控制+模拟控制--其他模块调用的统一控制方法
     *
     * @param map
     * @return
     */
    @Override
    public Integer controlDevices(Map<String, Object> map) {
        //设备ID
        String devId = Optional.ofNullable(map.get("devId")).orElse("").toString();
        //设备状态
        String state = Optional.ofNullable(map.get("state")).orElse("").toString();
        SdDevices sdDevices = sdDevicesService.selectSdDevicesById(devId);

        Integer controlState = 0;

        boolean isopen = commonControlService.queryAnalogControlConfig();
        if (isopen) {
            //设备模拟控制开启，直接变更设备状态为在线并展示对应运行状态
            controlState = commonControlService.analogControl(map,sdDevices);
            return controlState;
        }

        //控制设备之前获取设备状态
        String beforeState = commonControlService.selectBeforeState(sdDevices);


        AjaxResult ajaxResult = control(sdDevices,state);
        Integer code = Integer.valueOf(String.valueOf(ajaxResult.get("code")));
        if( code == HttpStatus.SUCCESS){
            controlState = Integer.valueOf(OperationLogEnum.STATE_SUCCESS.getCode());
        }
        //操作日志
        commonControlService.addOperationLog(map,sdDevices,beforeState,controlState);
        return controlState;
    }


    /**
     * 控制设备
     *
     * @param sdDevices
     * @param state    设备状态
     */
    public AjaxResult control(SdDevices sdDevices, String state) {

        String deviceId = sdDevices.getEqId();
        //点位类型：控制点位
        Long pointType = DevicePointControlTypeEnum.control_enable.getCode();
        //数据状态
        String runState = DeviceStateTypeEnum.data_status.getCode();

//        //设备信息
//        SdDevices sdDevices = sdDevicesService.selectSdDevicesById(deviceId);

        //查询设备点位
        SdDevicePointPlc devicePointPlc = new SdDevicePointPlc();
        devicePointPlc.setEqId(deviceId);
        devicePointPlc.setIsReserved(pointType);
        List<SdDevicePointPlc> devicePointList = devicePointPlcService.selectSdDevicePointPlcList(devicePointPlc);
        if(devicePointList == null || devicePointList.size() == 0){
            return AjaxResult.error("未配置设备点位");
        }
        devicePointPlc = devicePointList.get(0);


        String pointConfig = devicePointPlc.getPointConfig();
        JSONArray jsonArray = JSONArray.parseArray(pointConfig);
        JSONObject jsonConfig = new JSONObject();
        for(Object obj : jsonArray){
            JSONObject jsonObject = (JSONObject) obj;
            String stateConfig = jsonObject.getString("state");
            if(state.equals(stateConfig)){
                jsonConfig = jsonObject;
                break;
            }
        }

        String controlState = jsonConfig.getString("value");
        if(controlState == null){
            return AjaxResult.error("设备点位配置不完整");
        }
        //写入长度
        String writeLength =devicePointPlc.getDataLength();
        if(writeLength == null){
            return AjaxResult.error("设备点位配置不完整");
        }
        //控制点位
        String address = devicePointPlc.getAddress();
        //功能码
        String functionCode = devicePointPlc.getFunctionCode();

        //通过控制父设备控制子设备
        String fEqId = sdDevices.getfEqId();
        if(fEqId == null || "".equals(fEqId)){
            return AjaxResult.error("未配置设备关联的PLC");
        }

        //发送指令
        return modbusCmd.sendControlCommand(XiMenZiPlcTask.deviceMap,fEqId,functionCode,address,writeLength,controlState);
    }



    /**
     * 解析读取的数据
     *
     * @param ip       网关设备IP
     * @param deviceId 网关设备ID
     * @param msg      读取的数据
     */
    @Override
    public void handleReadData(String ip, String deviceId, String msg) {

        JSONObject jsonObject = ModbusCmdResolver.commandParse(msg);

        String functionCode = jsonObject.getString("functionCode");
        String readData = jsonObject.getString("readData");
        if(ModbusFunctionCode.CODE_TWO.equals(functionCode)){

            Integer num = Integer.parseInt(readData,16);
            String binaryNum = Integer.toBinaryString(num);
            binaryNum = StringUtil.fillStringWithZero(binaryNum,4);
            jsonObject.put("data",binaryNum);
        }
        if(ModbusFunctionCode.CODE_THREE.equals(functionCode) || ModbusFunctionCode.CODE_FOUR.equals(functionCode)){
            Integer length = readData.length();
//            //按照功能码分析
            //两个字节一个数字
            if(length % 4 == 0){
                List<String> list = ModbusCmdResolver.handleTwoBytesData(readData);
                jsonObject.put("data",list);
            }
        }
        if(ModbusFunctionCode.CODE_SIX.equals(functionCode)){
            //控制指令的返回指令，不需要解析
        }

        dataParse(ip,deviceId,jsonObject);
    }


    /**
     * 数据解析
     * @param jsonObject 数据
     */
    public void dataParse(String ip,String deviceId,JSONObject jsonObject){

        //功能码
        String functionCode = jsonObject.getString("functionCode");
        //起始地址
        String address = jsonObject.getString("address");
        Object data = jsonObject.get("data");
        if(data == null){
            return;
        }

        List<String> idList = new ArrayList<>();
        idList.add(deviceId);

        //只读点位
        Long pointType = DevicePointControlTypeEnum.only_read.getCode();

        List<SdDevicePointPlc> pointList = devicePointPlcService.selectDevicePointByFEqId(idList,pointType,functionCode);


        switch (functionCode){
            case ModbusFunctionCode.CODE_TWO:
//                getFunctionTwoData(pointList, String.valueOf(data),address);
                break;
            case ModbusFunctionCode.CODE_THREE:
            case ModbusFunctionCode.CODE_FOUR:
                //实时数据
                List<String> dataList = Obj2ListUtil.objToList(data,String.class);
                getFunctionThreeData(pointList,dataList,address);
            default: break;
        }

    }

    /**
     * 功能码03、04返回数据解析
     * 点位配置解析：需要计算公式，将原始数据（模拟量）转为具有实际意义的数字
     * @param pointList
     * @param dataList
     * @param address
     */
    public void getFunctionThreeData(List<SdDevicePointPlc> pointList,List<String> dataList,String address){
        Map<Integer,String> valueMap = new HashMap<>();


        Integer startAddress = Integer.valueOf(address,16);
        for(int i = 0; i < dataList.size(); i++){
            valueMap.put(startAddress,dataList.get(i));
            startAddress ++;
        }

        //按父设备ID、功能码筛选设备点位表中的地址，按照点位地址从小到大的顺序，将list数据跟设备点位对应
        // 将读取的数据保存到实时数据表中
//        Integer addressCursor = Integer.valueOf(address);
        for(int j = 0; j < pointList.size();  j++){
            String result = "";
            SdDevicePointPlc devicePointPlc = pointList.get(j);
            //设备ID
            String eqId = devicePointPlc.getEqId();
            //设备类型数据项ID
            Long itemId = devicePointPlc.getItemId();
            //点位地址
            Integer pointAddress = Integer.valueOf(devicePointPlc.getAddress());
//            //点位长度
//            Integer dataLength = Integer.valueOf(devicePointPlc.getDataLength());
            //实时数据
            String data = valueMap.get(pointAddress);
            if(data == null){
                continue;
            }
            String pointConfig = devicePointPlc.getPointConfig();
            if(pointConfig != null && !"".equals(pointConfig)){
                //状态映射
                JSONArray jsonArray = JSONArray.parseArray(pointConfig);
                String state = "";
                for(Object obj : jsonArray){
                    JSONObject jsonObject = (JSONObject) obj;
                    String pointValue = jsonObject.getString("value");
                    if(data.equals(pointValue)){
                        state = jsonObject.getString("state");
                        System.out.println("实时数据状态：stateJson="+jsonObject+",eqId="+eqId);
                        break;
                    }
                }
                result = state;
            }else if(data.indexOf(".") > 0){
                //小数模拟量
                //精确2位小数
                BigDecimal dataNum = new BigDecimal(data);
                //最终结果保留2位小数
                dataNum = dataNum.setScale(2,BigDecimal.ROUND_HALF_UP);
                result = String.valueOf(dataNum);
            }
            //如果是整数模拟量,直接保存
            //更新实时数据
            SdDevices sdDevices = new SdDevices();
            sdDevices.setEqId(eqId);
            deviceDataService.updateDeviceData(sdDevices, result,itemId);
            }
    }
}
