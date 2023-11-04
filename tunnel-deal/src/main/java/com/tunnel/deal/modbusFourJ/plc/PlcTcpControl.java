package com.tunnel.deal.modbusFourJ.plc;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.AjaxResult;
import com.serotonin.modbus4j.ModbusMaster;
import com.tunnel.business.datacenter.domain.enumeration.DevicePointControlTypeEnum;
import com.tunnel.business.datacenter.domain.enumeration.DeviceStateTypeEnum;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeItemEnum;
import com.tunnel.business.datacenter.domain.enumeration.OperationLogEnum;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.protocol.SdDevicePointPlc;
import com.tunnel.business.service.dataInfo.ISdDeviceDataService;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.service.digitalmodel.impl.RadarEventServiceImpl;
import com.tunnel.business.service.protocol.ISdDevicePointPlcService;
import com.tunnel.business.strategy.service.CommonControlService;
import com.tunnel.deal.generalcontrol.GeneralControlBean;
import com.tunnel.deal.modbusFourJ.util.Modbus4jWriteUtil;
import com.tunnel.deal.modbusFourJ.util.ModbusTcpMaster;
import com.tunnel.deal.tcp.modbus.ModbusFunctionCode;
import com.tunnel.deal.tcp.plc.ximenzi.XiMenZiPlcControl;
import com.tunnel.deal.tcp.util.NumberSystemConvert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * describe: PLC modbus tcp控制类
 *
 * @author zs
 * @date 2023/10/26
 */
@Component
public class PlcTcpControl implements GeneralControlBean {

    @Autowired
    private CommonControlService commonControlService;

    @Autowired
    private ISdDevicesService sdDevicesService;

    @Autowired
    private ISdDeviceDataService deviceDataService;

    @Autowired
    private ISdDevicePointPlcService devicePointPlcService;

    @Autowired
    private RadarEventServiceImpl radarEventServiceImpl;

    @Autowired
    @Qualifier("kafkaOneTemplate")
    private KafkaTemplate<String, String> kafkaOneTemplate;

    /**
     * 线程池
     */
    @Resource(name = "threadPoolTaskExecutor")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    private XiMenZiPlcControl xiMenZiPlcControl;

    @Autowired
    @Qualifier(value = "ModbusTcpMaster")
    ModbusTcpMaster masterTcp;


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

//        ModbusMaster master = masterTcp.getSlave("127.0.0.1", 502);
       ModbusMaster master = masterTcp.getSlave(ip, portNum);

//        short writeValue = Short.valueOf(controlState);
        int writeOffset = Integer.valueOf(address);
        boolean result = Modbus4jWriteUtil.writeData(master,1,writeOffset,controlState,functionCode);
        if(result){
           return AjaxResult.success("设备指令发送成功");
        }else{
            return AjaxResult.error("设备指令发送报错");
        }

    }

    public void dateParse(String deviceId,String functionCode,String address,List<String> result){
//        List<Object> dataList = Arrays.asList(result);
//
//        ArrayList<Integer> arrayList = new ArrayList<Integer>(result.length);
//        Collections.addAll(arrayList, result);
//
//
//        // 对于数组则可以使用 ImmutableList 的 copyOf() 方法创建
//        List<String> i2 = ImmutableList.copyOf(result);

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
                getFunctionThreeData(pointList,result,address);
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
    public void getFunctionThreeData(List<SdDevicePointPlc> pointList, List<String> dataList, String address){
        Map<Integer,String> valueMap = new HashMap<>();

        //十进制地址
        Integer startAddress = Integer.valueOf(address);
        for(int i = 0; i < dataList.size(); i++){
            String hexNum = dataList.get(i);
//            short num = dataList[i];
//            String hexNum = Integer.toHexString((-128 & 0xFF) | 0x100);
//            String hexNum = Integer.toHexString(num);
            valueMap.put(startAddress,hexNum);
            startAddress ++;
        }

        //按父设备ID、功能码筛选设备点位表中的地址，按照点位地址从小到大的顺序，将list数据跟设备点位对应
        // 将读取的数据保存到实时数据表中
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
//            if(eqId.contains("JQ-ZiBo-TaiHe-QFL-JF-010+1")){
//                System.out.println("测试");
//            }
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
                if(valueMap.get(pointAddress+1) == null){
                    System.out.println("没有数据，pointAddress="+pointAddress);
                    continue;
                }
                data = data + valueMap.get(pointAddress + 1);
                Float num = NumberSystemConvert.convertHexToFloat(data);
                //精确2位小数
                BigDecimal dataNum = new BigDecimal(num);
                //最终结果保留2位小数
                dataNum = dataNum.setScale(2,BigDecimal.ROUND_HALF_UP);
                result = String.valueOf(dataNum);
            }

            // Vi  数值千米换算成米  待优化 ，从数据库配置
            if(itemId == DevicesTypeItemEnum.VI.getCode()){
                Float num = NumberSystemConvert.convertHexToFloat(data);
                //精确2位小数
                BigDecimal dataNum = new BigDecimal(num);
                dataNum = dataNum.multiply(BigDecimal.valueOf(1000));
                dataNum = dataNum.setScale(2,BigDecimal.ROUND_HALF_UP);
                result = String.valueOf(dataNum);
            }

            //如果是整数模拟量,直接保存
            //更新实时数据
            SdDevices sdDevices = new SdDevices();
            sdDevices.setEqId(eqId);
            deviceDataService.updateDeviceData(sdDevices, result,itemId);

            //储存历史数据
            xiMenZiPlcControl.setDeviceDataRecord(eqId,result,Long.valueOf(itemId));
            //设置设备在线
            sdDevicesService.updateOnlineStatus(eqId,false);
//            //异步推送万集数据
//            threadPoolTaskExecutor.execute(()->{
//                try{
//                    xiMenZiPlcControl.pushWanJi(eqId);
//                }catch (Exception e){
//                    e.printStackTrace();
//
//                }
//            });
        }
    }
}
