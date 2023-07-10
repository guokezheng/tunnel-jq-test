package com.tunnel.platform.service.deviceControl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.AjaxResult;
import com.tunnel.business.datacenter.domain.enumeration.DevicePointControlTypeEnum;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeItemEnum;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.protocol.SdDevicePoint;
import com.tunnel.business.service.dataInfo.ISdDeviceDataService;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.service.protocol.ISdDevicePointService;
import com.tunnel.business.strategy.factory.DeviceDataStrategyFactory;
import com.tunnel.business.strategy.service.CommonControlService;
import com.tunnel.business.strategy.service.DeviceDataStrategyService;
import com.tunnel.deal.generalcontrol.GeneralControlBean;
import com.tunnel.deal.tcp.client.general.TcpClientGeneralBean;
import com.tunnel.deal.tcp.modbus.ModbusCmd;
import com.tunnel.deal.tcp.modbus.rtu.ModbusRtuCmd;
import com.tunnel.deal.tcp.modbus.rtu.ModbusRtuCmdResolver;
import com.tunnel.deal.tcp.util.Obj2ListUtil;
import com.tunnel.deal.warninglightstrip.WarningLightStripHandle;
import com.tunnel.deal.warninglightstrip.WarningLightStripTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * describe: 警示灯带控制类
 *
 * @author zs
 * @date 2023/5/11
 */
@Component
public class WarningLightStripControl implements GeneralControlBean, TcpClientGeneralBean {

    private static final Logger log = LoggerFactory.getLogger(WarningLightStripControl.class);

    @Autowired
    private ISdDevicesService sdDevicesService;

    @Autowired
    private ISdDeviceDataService deviceDataService;

    @Autowired
    private ISdDevicePointService devicePointService;

    @Autowired
    private CommonControlService commonControlService;

    @Autowired
    private DeviceDataStrategyFactory deviceDataStrategyFactory;

    @Autowired
    private ModbusRtuCmd modbusRtuCmd;

    @Autowired
    private ModbusCmd modbusCmd;

    /**
     * 设备控制--工作台单个设备控制
     * @param map
     * @param sdDevices
     * @return
     */
    @Override
    public AjaxResult control(Map<String, Object> map, SdDevices sdDevices) {

        String devId = map.get("devId").toString();
        String state = map.get("state").toString();
        String eqType = String.valueOf(sdDevices.getEqType());

        boolean isopen = commonControlService.queryAnalogControlConfig();
        Long itemCode = (long) DevicesTypeItemEnum.JING_SHI_DENG_DAI.getCode();
        String beforeState = commonControlService.selectBeforeState(devId,itemCode);
        if (isopen) {
            //设备模拟控制开启
            DeviceDataStrategyService deviceDataStrategyService = deviceDataStrategyFactory.strategy(eqType);
            AjaxResult ajaxResult = deviceDataStrategyService.analogControl(map,sdDevices);
            return ajaxResult;
        }

        //控制设备
        int controlState = WarningLightStripHandle.getInstance().toControlDev(devId, Integer.parseInt(state), sdDevices);
        //控制设备的代码与定时获取状态的代码同步更新
//        int controlState = controlCmd(sdDevices,state);

        //控制方式-手动控制
//        String controlType = DeviceControlTypeEnum.AUTO_CONTROL.getCode();
        commonControlService.addOperationLog(map,sdDevices,beforeState,controlState);
        return AjaxResult.success(controlState);
    }



//    /**
//     * 添加操作日志
//     * @param map
//     * @param sdDevices
//     * @param beforeState
//     * @param controlState
//     * @param controlType
//     */
//    private void addOperationLog(Map<String, Object> map, SdDevices sdDevices,String beforeState,int controlState,String controlType){
//        String state = map.get("state").toString();
//        //添加操作记录
//        SdOperationLog sdOperationLog = new SdOperationLog();
//        sdOperationLog.setEqTypeId(sdDevices.getEqType());
//        sdOperationLog.setTunnelId(sdDevices.getEqTunnelId());
//        sdOperationLog.setEqId(sdDevices.getEqId());
//        sdOperationLog.setCreateTime(new Date());
//        sdOperationLog.setBeforeState(beforeState);
//        sdOperationLog.setOperationState(state);
//        sdOperationLog.setControlType(controlType);
//        sdOperationLog.setState(String.valueOf(controlState));
//        sdOperationLog.setOperIp(IpUtils.getIpAddr(ServletUtils.getRequest()));
//        sdOperationLogService.insertSdOperationLog(sdOperationLog);
//    }
//    /**
//     * 控制设备之前获取设备状态
//     *
//     * @param sdDevices 设备信息
//     * @return
//     */
//    private String selectBeforeState(SdDevices sdDevices){
//        String beforeState = "";
//        //获取当前设备状态
//        SdDeviceData sdDeviceData = new SdDeviceData();
//        sdDeviceData.setDeviceId(sdDevices.getEqId());
//        sdDeviceData.setItemId(Long.valueOf(DevicesTypeItemEnum.JING_SHI_DENG_DAI.getCode()));
//        List<SdDeviceData> data = sdDeviceDataService.selectSdDeviceDataList(sdDeviceData);
//        if (data.size() > 0 && data.get(0) != null) {
//            beforeState = data.get(0).getData();
//        }
//        return beforeState;
//    }

//    /**
//     * 模拟控制方法
//     *
//     * @param map
//     * @param sdDevices
//     * @return
//     */
//    public void analogControlDevice(Map<String, Object> map, SdDevices sdDevices) {
//        String state = map.get("state").toString();
//        String brightness = map.get("brightness").toString();
//
//        //设备模拟控制开启，直接变更设备状态为在线并展示对应运行状态
//        sdDevices.setEqStatus("1");
//        sdDevices.setEqStatusTime(new Date());
//        sdDevicesService.updateSdDevices(sdDevices);
//        if (sdDevices.getEqType().longValue() == DevicesTypeEnum.JING_SHI_DENG_DAI.getCode().longValue()) {
//            sdDeviceDataService.updateDeviceData(sdDevices, state, Long.valueOf(DevicesTypeItemEnum.JING_SHI_DENG_DAI.getCode()));
//            sdDeviceDataService.updateDeviceData(sdDevices, brightness, Long.valueOf(DevicesTypeItemEnum.JING_SHI_DENG_DAI_STATUS.getCode()));
//        }
//
//    }

    /**
     * 设备控制
     *
     * @param map
     * @return
     */
    @Override
    public Integer controlDevices(Map<String, Object> map) {

        String devId = map.get("devId").toString();
        SdDevices sdDevices = sdDevicesService.selectSdDevicesById(devId);

        AjaxResult ajaxResult = control(map,sdDevices);
        Integer controlState = Integer.valueOf(String.valueOf(ajaxResult.get("data")));
        return controlState;
    }




    /**
     * 解析配置、拼接指令、下发指令
     * @param sdDevices
     * @param state
     * @return
     */
    public Integer controlCmd(SdDevices sdDevices,String state){
        int result = 0;
        String deviceId = sdDevices.getEqId();
        //点位类型：控制点位
        Long pointType = DevicePointControlTypeEnum.control_enable.getCode();
        //查询设备点位
        SdDevicePoint devicePoint = new SdDevicePoint();
        devicePoint.setEqId(deviceId);
        devicePoint.setIsReserved(pointType);
        List<SdDevicePoint> devicePointList = devicePointService.selectSdDevicePointList(devicePoint);
        if(devicePointList == null || devicePointList.size() == 0){
            log.error("警示灯带控制报错：未配置设备点位");
            return result;
        }
        devicePoint = devicePointList.get(0);
        //功能码
        String functionCode = devicePoint.getFunctionCode();
        String pointConfig = devicePoint.getPointConfig();
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
        String pointArray = jsonConfig.getString("pointArray");
        if(pointArray == null){
            log.error("警示灯带控制报错：设备点位配置不完整");
            return result;
        }
        result = sendCommand(deviceId,functionCode,pointArray);
        return result;
    }

    /**
     * 解析指令配置，拼接指令，下发控制指令
     * @param deviceId
     * @param functionCode
     * @param pointArray
     * @return
     */
    public Integer sendCommand(String deviceId,String functionCode,String pointArray){
        int result = 0;
        //        //待下发的指令
//        List<String> cmdList = new ArrayList<>();

        JSONArray pointJsonArray = JSONArray.parseArray(pointArray);
        for(Object obj : pointJsonArray){
            JSONObject jsonObject = (JSONObject) obj;
            String address = jsonObject.getString("address");
            String pointValue = jsonObject.getString("pointValue");
            String command = modbusRtuCmd.getSendCommand(functionCode,address,"","",pointValue);
            System.out.println("警示灯带指令下发："+command);
            AjaxResult ajaxResult = modbusRtuCmd.sendCommand(WarningLightStripTask.deviceMap,deviceId,command);
            int code = Integer.parseInt(String.valueOf(ajaxResult.get("code")));
            if(code == HttpStatus.SUCCESS){
                result = 1;
            }
            //休眠2毫秒
            modbusCmd.sleep(1000);

//            cmdList.add(command);
        }

        return result;
    }


    /**
     * 解析读取的数据
     *
     * @param ip         网关设备IP
     * @param deviceId   网关设备ID
     * @param msg 读取的数据
     */
    @Override
    public void handleReadData(String ip, String deviceId, String msg) {
        //寄存器数量
        int registerNum = 14;
        JSONObject jsonObject = ModbusRtuCmdResolver.commandParse(msg);
        //msg = 01031c0000000900090009000900090000000900090009000900090000000145be
        Object data = jsonObject.get("data");
        if(data == null){
            return;
        }
        String status = "";
        //实时数据
        List<String> dataList = Obj2ListUtil.objToList(data,String.class);
        if(dataList != null && dataList.size() == registerNum){
            Integer redFirst = Integer.valueOf(dataList.get(0));
            Integer greenFirst = Integer.valueOf(dataList.get(3));
            Integer redSecond = Integer.valueOf(dataList.get(6));
            Integer greenSecond = Integer.valueOf(dataList.get(9));
            boolean red = (redFirst > 0 && greenFirst == 0) && (redSecond > 0 && greenSecond == 0);
            boolean green = (redFirst == 0 && greenFirst > 0) && (redSecond == 0 && greenSecond > 0);
            boolean yellow = (redFirst > 0 && greenFirst > 0) && (redSecond > 0 && greenSecond > 0);
            if(red){
                //红色
                status = "1";
            }
            if(green){
                //绿色
                status = "2";
            }
            if(yellow){
                //黄色
                status = "3";
            }
        }
        if(!"".equals(status)){
            //更新实时数据
            SdDevices sdDevices = new SdDevices();
            sdDevices.setEqId(deviceId);
            deviceDataService.updateDeviceData(sdDevices, status, (long) DevicesTypeItemEnum.JING_SHI_DENG_DAI.getCode());
        }
    }

//    /**
//     * 模拟控制方法
//     *
//     * @param map
//     * @param sdDevices
//     * @return
//     */
////    @Override
//    public Integer analogControl(Map<String, Object> map, SdDevices sdDevices) {
//
//        String state = map.get("state").toString();
//
//        //设备模拟控制开启，直接变更设备状态为在线并展示对应运行状态
//        sdDevices.setEqStatus("1");
//        sdDevices.setEqStatusTime(new Date());
//        sdDevicesService.updateSdDevices(sdDevices);
//        SdDeviceTypeItem sdDeviceTypeItem = new SdDeviceTypeItem();
//        sdDeviceTypeItem.setDeviceTypeId(sdDevices.getEqType());
//        List<SdDeviceTypeItem> sdDeviceTypeItems = sdDeviceTypeItemService.selectSdDeviceTypeItemList(sdDeviceTypeItem);
//        if (sdDeviceTypeItems.size() == 0) {
//            throw new RuntimeException("当前设备没有设备类型数据项数据");
//        }
//        SdDeviceTypeItem typeItem = sdDeviceTypeItems.get(0);
//        sdDeviceDataService.updateDeviceData(sdDevices, state, typeItem.getId());
//       Integer controlState = 1;
//       return controlState;
//    }


//    public void addOperationLog(Map<String, Object> map,SdDevices sdDevices,Integer controlState){
//
//        String devId = map.get("devId").toString();
//        String state = map.get("state").toString();
//        String controlType = map.get("controlType").toString();
//
//        //获取当前设备状态
//        SdDeviceData sdDeviceData = new SdDeviceData();
//        sdDeviceData.setDeviceId(devId);
//        List<SdDeviceData> data = sdDeviceDataService.selectSdDeviceDataList(sdDeviceData);
//
//        //添加操作记录
//        SdOperationLog sdOperationLog = new SdOperationLog();
//
//        //部份设备未接入，无法正确获取设备控制结果，默认失败
////        sdOperationLog.setState("0");
//
////        sdOperationLog.setEqTypeId(sdDevices.getEqType());
////        sdOperationLog.setTunnelId(sdDevices.getEqTunnelId());
////        sdOperationLog.setEqId(sdDevices.getEqId());
////        sdOperationLog.setOperationState(state);
////        sdOperationLog.setControlType(controlType);
////        if (null != map.get("operIp")) {
////            sdOperationLog.setOperIp(map.get("operIp").toString());
////        }
////        if (null != map.get("controlTime")) {
////            sdOperationLog.setCreateTime(DateUtils.parseDate(map.get("controlTime")));
////        }else{
////            sdOperationLog.setCreateTime(new Date());
////        }
////        if (null != map.get("eventId")) {
////            sdOperationLog.setEventId(map.get("eventId").toString());
////        }
//
////        //警示灯带设备类型数据项目前只有一个状态state
////        if (data.size() > 0) {
////            sdOperationLog.setBeforeState(data.get(0).getData());
////        }
//
////        sdOperationLog.setState(String.valueOf(controlState));
//        sdOperationLogService.insertSdOperationLog(sdOperationLog);
//    }

//    /**
//     * 警示灯带控制方法
//     * @param devId
//     * @param state
//     * @param sdDevices
//     * @return
//     */
//    private int controlWarningLightStripDevice(Map<String, Object> map, String devId, String state, SdDevices sdDevices) {
//        Integer controlState = 0;
//
//        boolean isopen = commonControlService.queryAnalogControlConfig();
//
//        if (!isopen) {
//            //连接设备进行控制
//            controlState = WarningLightStripHandle.getInstance().toControlDev(devId, Integer.parseInt(state), sdDevices);
//        } else {
//            //模拟控制
//            controlState = analogControl(map,sdDevices);
//        }
//        return controlState;
//    }
}
