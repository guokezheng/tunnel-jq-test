package com.tunnel.deal.tcp.plc.ximenzi;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.tunnel.business.datacenter.domain.enumeration.DevicePointControlTypeEnum;
import com.tunnel.business.datacenter.domain.enumeration.DeviceStateTypeEnum;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeItemEnum;
import com.tunnel.business.datacenter.domain.enumeration.OperationLogEnum;
import com.tunnel.business.domain.dataInfo.SdDeviceDataRecord;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.protocol.SdDevicePointPlc;
import com.tunnel.business.mapper.dataInfo.SdDeviceDataRecordMapper;
import com.tunnel.business.service.dataInfo.ISdDeviceDataService;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.service.protocol.ISdDevicePointPlcService;
import com.tunnel.business.strategy.service.CommonControlService;
import com.tunnel.deal.generalcontrol.GeneralControlBean;
import com.tunnel.deal.tcp.client.general.TcpClientGeneralBean;
import com.tunnel.deal.tcp.modbus.ModbusCmd;
import com.tunnel.deal.tcp.modbus.ModbusCmdResolver;
import com.tunnel.deal.tcp.modbus.ModbusCmdValues;
import com.tunnel.deal.tcp.modbus.ModbusFunctionCode;
import com.tunnel.deal.tcp.plc.ximenzi.task.WZBXiMenZiPlcTask;
import com.tunnel.deal.tcp.util.NumberSystemConvert;
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
public class WZBXiMenZiPlcControl implements GeneralControlBean, TcpClientGeneralBean {

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

        ModbusCmd.commandLock = false;
        System.out.println("西门子--关闭指令锁：commandLock="+ModbusCmd.commandLock);

        //通过控制父设备控制子设备
        String fEqId = sdDevices.getfEqId();
        if(fEqId == null || "".equals(fEqId)){
            return AjaxResult.error("未配置设备关联的PLC");
        }
        SdDevices fDevice = sdDevicesService.selectSdDevicesById(fEqId);
        String ip = fDevice.getIp();
        String port = fDevice.getPort();
        Integer portNum = Integer.valueOf(port);

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


        //发送指令
        AjaxResult ajaxResult = modbusCmd.sendControlCommand(WZBXiMenZiPlcTask.deviceMap,fEqId,functionCode,address,writeLength,controlState);

        //每次指令保留300ms时间间隔，时间短PLC容易返回错误指令
        modbusCmd.sleep(100);
        ModbusCmd.commandLock = true;
        System.out.println("西门子--打开指令锁：commandLock="+ModbusCmd.commandLock);
        return ajaxResult;
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

        JSONObject jsonObject = ModbusCmdResolver.commandParse(ip,deviceId,msg);

        String functionCode = jsonObject.getString("functionCode");
        String readData = jsonObject.getString("readData");
        if(ModbusFunctionCode.CODE_ONE.equals(functionCode)){
//            Integer readDataNum = Integer.parseInt(readData,16);
//            String s = Integer.toBinaryString(readDataNum);
            List<String> list = ModbusCmdResolver.handleOneBitDataNew(readData);
            jsonObject.put("data",list);
        }
        if(ModbusFunctionCode.CODE_TWO.equals(functionCode)){

            Integer num = Integer.parseInt(readData,16);
            String binaryNum = Integer.toBinaryString(num);
            binaryNum = StringUtil.fillStringWithZero(binaryNum,4);
            jsonObject.put("data",binaryNum);
        }
        if(ModbusFunctionCode.CODE_THREE.equals(functionCode) || ModbusFunctionCode.CODE_FOUR.equals(functionCode)){
            Integer length = readData.length();
//            //按照功能码分析
            //两个字节一个字（一个寄存器地址的长度）
            if(length % 4 == 0){
                List<String> list = ModbusCmdResolver.handleTwoBytesDataHex(readData);
                jsonObject.put("data",list);
            }
        }
        if(ModbusFunctionCode.CODE_SIX.equals(functionCode)){
            //控制指令的返回指令，不需要解析
            if(msg.length() >= ModbusCmdValues.FUNCTION_SIX_RECV_CMD_LENGTH){
                //正常的返回指令
                String startAddress = msg.substring(16,20);
                String value = msg.substring(20,24);
            }else{
                //序列码和起始地址相同,控制指令返回失败
                String startAddress = msg.substring(0,4);
            }
        }

        dataParse(ip,deviceId,jsonObject);

//        SdDevices sdDevices = sdDevicesService.selectSdDevicesById(deviceId);
//        String port = sdDevices.getPort();
//        //关闭通道
//        Channel channel = TcpNettySocketClient.channels.get(ChannelKey.getChannelKey(ip,Integer.valueOf(port)));
//        channel.close();
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
            case ModbusFunctionCode.CODE_ONE:
                //实时数据
                List<String> dataResult = Obj2ListUtil.objToList(data,String.class);
                getFunctionOneData(pointList,dataResult,address);
                break;
            case ModbusFunctionCode.CODE_TWO:
//                getFunctionTwoData(pointList, String.valueOf(data),address);
                break;
            case ModbusFunctionCode.CODE_THREE:
            case ModbusFunctionCode.CODE_FOUR:
                //实时数据
                List<String> dataList = Obj2ListUtil.objToList(data,String.class);
                getFunctionThreeData(pointList,dataList,address);
                break;
            default: break;
        }

    }

    /**
     * 功能码01的返回数据解析
     * @param pointList
     * @param dataList
     * @param address
     */
    public void getFunctionOneData(List<SdDevicePointPlc> pointList,List<String> dataList,String address){
        Map<Integer,String> valueMap = new HashMap<>();

        //十六进制地址转换
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
            Integer dataLength = Integer.valueOf(devicePointPlc.getDataLength());
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
            }
            //更新实时数据
            SdDevices sdDevices = new SdDevices();
            sdDevices.setEqId(eqId);
            deviceDataService.updateDeviceData(sdDevices, result,itemId);

            //储存历史数据
            setDeviceDataRecord(eqId,result,Long.valueOf(itemId));
            //设置设备在线
            sdDevicesService.updateOnlineStatus(eqId,false);
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

        //十六进制地址转换
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
            Integer dataLength = Integer.valueOf(devicePointPlc.getDataLength());
            //实时数据
            String data = valueMap.get(pointAddress);
            if(data == null){
                continue;
            }
            String pointConfig = devicePointPlc.getPointConfig();
            if(pointConfig != null && !"".equals(pointConfig)){
                //将十六进制数据转换为十进制，再做匹配
                Integer value = Integer.parseInt(data,16);
                data = String.valueOf(value);
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
            }else if(dataLength == 2){
                //模拟量，4个字节，2个寄存器地址，双字
                data = data + valueMap.get(pointAddress + 1);
                data = NumberSystemConvert.reverseHex(data);
                Float num = NumberSystemConvert.convertHexToFloat(data);
                //精确2位小数
                BigDecimal dataNum = new BigDecimal(num);
                //最终结果保留2位小数
                dataNum = dataNum.setScale(2,BigDecimal.ROUND_HALF_UP);
                result = String.valueOf(dataNum);
            }

            //    if(dataLength == 2){
            //                JSONObject jsonObject = JSONObject.parseObject(pointConfig);
            //                //模拟量，4个字节，2个寄存器地址，双字
            //                data = data + valueMap.get(pointAddress + 1);
            //                Float num = NumberSystemConvert.convertHexToFloat(data);
            //                //精确2位小数
            //                BigDecimal dataNum = new BigDecimal(num);
            //
            //                //{divisor:'16',rangeMin:'4',rangeMax:'20',realRangeMin:'0',realRangeMax:'6500'}
            //                BigDecimal divisor = jsonObject.getBigDecimal("divisor");
            //                BigDecimal rangeMin = jsonObject.getBigDecimal("rangeMin");
            //                BigDecimal rangeMax = jsonObject.getBigDecimal("rangeMax");
            //                BigDecimal realRangeMin = jsonObject.getBigDecimal("realRangeMin");
            //                BigDecimal realRangeMax = jsonObject.getBigDecimal("realRangeMax");
            //                //{(测量范围上限-测量范围下限)/16}x{电流(A)-4}+测量范围下限-[(6500-0)/16}x{电流(mA)-4} +0
            //                BigDecimal resultNum = realRangeMax.subtract(realRangeMin).divide(divisor,3,BigDecimal.ROUND_HALF_UP).multiply(dataNum.subtract(rangeMin)).add(realRangeMin);
            //
            //                //最终结果保留2位小数
            //                resultNum = resultNum.setScale(2,BigDecimal.ROUND_HALF_UP);
            //                result = String.valueOf(resultNum);
            //            }

            //如果是整数模拟量,直接保存
            //更新实时数据
            SdDevices sdDevices = new SdDevices();
            sdDevices.setEqId(eqId);
            deviceDataService.updateDeviceData(sdDevices, result,itemId);

            //储存历史数据
            setDeviceDataRecord(eqId,result,Long.valueOf(itemId));
            //设置设备在线
            sdDevicesService.updateOnlineStatus(eqId,false);
            }
    }

    /**
     * 储存设备数据历史记录表
     *
     * @param deviceId
     * @param data
     * @param itemId
     */
    public void setDeviceDataRecord(String deviceId,String data,Long itemId){
        Long co = Long.valueOf(DevicesTypeItemEnum.CO.getCode());
        Long vi = Long.valueOf(DevicesTypeItemEnum.VI.getCode());
        Long fengsu = Long.valueOf(DevicesTypeItemEnum.FENG_SU.getCode());
        Long fengxiang = Long.valueOf(DevicesTypeItemEnum.FENG_XIANG.getCode());
        Long dongnei = Long.valueOf(DevicesTypeItemEnum.LIANG_DU_INSIDE.getCode());
        Long dongwai = Long.valueOf(DevicesTypeItemEnum.LIANG_DU_OUTSIDE.getCode());
        Long yuanchuan = Long.valueOf(DevicesTypeItemEnum.YUAN_CHUAN_YA_LI_ZHI.getCode());
        Long wendu = Long.valueOf(DevicesTypeItemEnum.WEN_DU_CHUANGAN.getCode());
        Long shidu  = Long.valueOf(DevicesTypeItemEnum.SHI_DU_CHUANGAN.getCode());
        Long shuijin  = Long.valueOf(DevicesTypeItemEnum.SHUI_JIN_LEVEL.getCode());

        Long fjZdsd  = Long.valueOf(DevicesTypeItemEnum.ZHEN_DONG_SU_DU.getCode());
        Long fjZdFd  = Long.valueOf(DevicesTypeItemEnum.ZHEN_DONG_FU_DU.getCode());
        Long fjCjz  = Long.valueOf(DevicesTypeItemEnum.CHEN_JIANG_ZHI.getCode());
        Long fjQxz  = Long.valueOf(DevicesTypeItemEnum.QING_XIE_ZHI.getCode());
        Long fjzdgj  = Long.valueOf(DevicesTypeItemEnum.ZHEN_DONG_GAO_JING.getCode());
        Long fjqxgj  = Long.valueOf(DevicesTypeItemEnum.CHEN_JIANG_QING_XIE_GAO_JING.getCode());

        if(itemId == co || itemId == vi || itemId == fengsu || itemId == fengxiang
                || itemId == dongnei || itemId == dongwai || itemId == yuanchuan|| itemId == wendu
                || itemId == shidu|| itemId == shuijin || itemId == fjZdsd || itemId == fjZdFd || itemId == fjCjz
                || itemId == fjQxz || itemId == fjzdgj || itemId == fjqxgj){
            SdDeviceDataRecord record = new SdDeviceDataRecord();
            record.setDeviceId(deviceId);
            record.setData(data);
            record.setItemId(itemId);
            record.setCreateTime(DateUtils.getNowDate());
            //获取bean
            SdDeviceDataRecordMapper bean = SpringUtils.getBean(SdDeviceDataRecordMapper.class);
            //将数据存入历史记录表
//            bean.insertSdDeviceDataRecord(record);
        }
    }
}
