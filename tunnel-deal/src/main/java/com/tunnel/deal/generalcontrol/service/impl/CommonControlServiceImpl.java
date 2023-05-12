package com.tunnel.deal.generalcontrol.service.impl;

import cn.hutool.json.JSONObject;
import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.ip.IpUtils;
import com.ruoyi.system.service.ISysDictDataService;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeEnum;
import com.tunnel.business.domain.dataInfo.SdDeviceData;
import com.tunnel.business.domain.dataInfo.SdDeviceTypeItem;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.event.SdDeviceNowState;
import com.tunnel.business.domain.logRecord.SdOperationLog;
import com.tunnel.business.service.dataInfo.ISdDeviceDataService;
import com.tunnel.business.service.dataInfo.ISdDeviceTypeItemService;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.service.logRecord.ISdOperationLogService;
import com.tunnel.deal.generalcontrol.service.CommonControlService;
import com.zc.common.core.websocket.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * describe: 公共控制方法
 *
 * @author zs
 * @date 2023/5/11
 */
@Service
public class CommonControlServiceImpl implements CommonControlService {

    @Autowired
    private ISdDevicesService sdDevicesService;

    @Autowired
    private ISdDeviceDataService sdDeviceDataService;

    @Autowired
    private ISdDeviceTypeItemService sdDeviceTypeItemService;

    @Autowired
    private ISysDictDataService sysDictDataService;

    @Autowired
    private ISdOperationLogService sdOperationLogService;


    @Override
    public void sendNowDeviceStatusByWebsocket(SdDevices sdDevices, String[] state, SdOperationLog sdOperationLog, String type) {
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
        if (type.equals("ydd")) {
            sdDeviceNowState.setBrightness(state[1]);
            sdDeviceNowState.setFrequency(state[2]);
        } else if (type.equals("ssbz")) {
            sdDeviceNowState.setBrightness(state[1]);
            sdDeviceNowState.setFrequency(state[2]);
            sdDeviceNowState.setFireMark(state[3]);
        }
        sdDeviceNowState.setExecuteResult(sdOperationLog.getState());
        if(null != sdOperationLog.getEventId()) {
            sdDeviceNowState.setEventId(sdOperationLog.getEventId());
        }
        sdDeviceNowState.setExecuteTime(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS,sdOperationLog.getCreateTime()));
        dataList.add(sdDeviceNowState);
        jsonObject.put("deviceStatusChangeLog", dataList);
        WebSocketService.broadcast("deviceStatusChangeLog", jsonObject.toString());
    }


    /**
     * 设备模拟控制开启，直接变更设备状态为在线并展示对应运行状态
     * @param map
     * @param sdDevices
     * @return
     */
    @Override
    public Integer analogControl(Map<String, Object> map, SdDevices sdDevices) {
        //设备状态
        String state = Optional.ofNullable(map.get("state")).orElse("").toString();

        //设备模拟控制开启，直接变更设备状态为在线并展示对应运行状态
        sdDevices.setEqStatus("1");
        sdDevices.setEqStatusTime(new Date());
        sdDevicesService.updateSdDevices(sdDevices);
        SdDeviceTypeItem sdDeviceTypeItem = new SdDeviceTypeItem();
        sdDeviceTypeItem.setDeviceTypeId(sdDevices.getEqType());
        List<SdDeviceTypeItem> sdDeviceTypeItems = sdDeviceTypeItemService.selectSdDeviceTypeItemList(sdDeviceTypeItem);
        if (sdDeviceTypeItems.size() == 0) {
            throw new RuntimeException("当前设备没有设备类型数据项数据");
        }
        SdDeviceTypeItem typeItem = sdDeviceTypeItems.get(0);
        sdDeviceDataService.updateDeviceData(sdDevices, state, Integer.parseInt(typeItem.getId().toString()));
        Integer controlState = 1;
        return controlState;
    }



    /**
     * 根据字典中配置的设备模拟控制值进行模拟状态展示
     * @return
     */
    @Override
    public boolean queryAnalogControlConfig(){
        //开启模拟控制的字典值
        String openAnalogControl = "1";
        //根据字典中配置的设备模拟控制值进行模拟状态展示
        List<SysDictData> isopenList = sysDictDataService.getSysDictDataByDictType("sys_analog_control_isopen");
        if (isopenList.size() == 0) {
            throw new RuntimeException("设备模拟控制是否开启字典值不存在，请联系管理员添加后重试");
        }
        SysDictData sysDictData = isopenList.get(0);
        String isopen = sysDictData.getDictValue();
        return openAnalogControl.equals(isopen);
    }

    /**
     * 组装日志信息
     * @param map
     * @param sdDevices
     * @param controlState
     * @return
     */
    @Override
    public SdOperationLog getOperationLog(Map<String, Object> map,SdDevices sdDevices,Integer controlState){
        //设备状态
        String state = Optional.ofNullable(map.get("state")).orElse("").toString();

        //添加操作记录
        SdOperationLog sdOperationLog = new SdOperationLog();
        //部份设备未接入，无法正确获取设备控制结果，默认失败
        sdOperationLog.setState("0");

        String controlType = map.get("controlType").toString();

        //获取当前设备状态
        SdDeviceData sdDeviceData = new SdDeviceData();
        sdDeviceData.setDeviceId(sdDevices.getEqId());
        List<SdDeviceData> data = sdDeviceDataService.selectSdDeviceDataList(sdDeviceData);

        sdOperationLog.setEqTypeId(sdDevices.getEqType());
        sdOperationLog.setTunnelId(sdDevices.getEqTunnelId());
        sdOperationLog.setEqId(sdDevices.getEqId());
        sdOperationLog.setOperationState(state);
        sdOperationLog.setControlType(controlType);
        if (null != map.get("operIp")) {
            sdOperationLog.setOperIp(map.get("operIp").toString());
        }
        if (null != map.get("controlTime")) {
            sdOperationLog.setCreateTime(DateUtils.parseDate(map.get("controlTime")));
        }else{
            sdOperationLog.setCreateTime(new Date());
        }
        if (null != map.get("eventId")) {
            sdOperationLog.setEventId(map.get("eventId").toString());
        }

        if (data.size() > 0) {
            sdOperationLog.setBeforeState(data.get(0).getData());
        }

        sdOperationLog.setState(String.valueOf(controlState));
        return sdOperationLog;
    }

    /**
     * 控制设备之前获取设备状态
     * 车指、交通信号灯、风机、卷帘门的设备类型
     * @param sdDevices 设备信息
     * @return
     */
    @Override
    public String selectBeforeState(SdDevices sdDevices) {
//      return selectBeforeState(sdDevices,"");
        String beforeState = "";
        //获取当前设备状态
        SdDeviceData sdDeviceData = new SdDeviceData();
        sdDeviceData.setDeviceId(sdDevices.getEqId());
        //原代码：固定取车指的itemId,会导致其他设备类型取不到实时数据
//        sdDeviceData.setItemId(Long.valueOf(DevicesTypeItemEnum.PU_TONG_CHE_ZHI.getCode()));
        //车指、交通信号灯、风机、卷帘门的设备类型数据项只有一个，只有一条实时数据,可以不传itemId
        List<SdDeviceData> data = sdDeviceDataService.selectSdDeviceDataList(sdDeviceData);
        if (data.size() > 0 && data.get(0) != null) {
            beforeState = data.get(0).getData();
        }
        return beforeState;
    }

//    /**
//     * @param sdDevices
//     * @param itemId
//     * @return
//     */
//    @Override
//    public String selectBeforeState(SdDevices sdDevices, String itemId) {
//        String beforeState = "";
//        //获取当前设备状态
//        SdDeviceData sdDeviceData = new SdDeviceData();
//        sdDeviceData.setDeviceId(sdDevices.getEqId());
//        //原代码：固定取车指的itemId,会导致其他设备类型取不到实时数据
////        sdDeviceData.setItemId(Long.valueOf(DevicesTypeItemEnum.PU_TONG_CHE_ZHI.getCode()));
//        //车指、交通信号灯、风机、卷帘门的设备类型数据项只有一个，只有一条实时数据,可以不传itemId
//        //基本照明、加强照明有多个数据项,必须传值
//        if(itemId != null && !"".equals(itemId)){
//            sdDeviceData.setItemId(Long.valueOf(itemId));
//        }
//        List<SdDeviceData> data = sdDeviceDataService.selectSdDeviceDataList(sdDeviceData);
//        if (data.size() > 0 && data.get(0) != null) {
//            beforeState = data.get(0).getData();
//        }
//        return beforeState;
//    }


    /**
     * 添加操作日志
     * @param sdDevices 设备信息
     * @param map 控制参数
     * @param beforeState 控制前状态
     * @param controlState 操作是否成功 0 不成功；1成功
     */
    @Override
    public void addOperationLog(SdDevices sdDevices,Map<String, Object> map,String beforeState,String controlState){
        //设备状态
        String state = Optional.ofNullable(map.get("state")).orElse("").toString();
        //亮度
        String brightness = Optional.ofNullable(map.get("brightness")).orElse("").toString();
        //添加操作记录
        SdOperationLog sdOperationLog = new SdOperationLog();
        sdOperationLog.setEqTypeId(sdDevices.getEqType());
        sdOperationLog.setTunnelId(sdDevices.getEqTunnelId());
        sdOperationLog.setEqId(sdDevices.getEqId());
        sdOperationLog.setCreateTime(new Date());

        sdOperationLog.setBeforeState(beforeState);
        //加强照明状态拼接
        String linState = "";
        if(DevicesTypeEnum.JIA_QIANG_ZHAO_MING.getCode().equals(sdDevices.getEqType()) || DevicesTypeEnum.JI_BEN_ZHAO_MING.getCode().equals(sdDevices.getEqType())){
            if(brightness != null){
                linState = "1".equals(state) ?"开启":"关闭";
                linState += "，亮度："+brightness + "%";
            }
            sdOperationLog.setOperationState(linState);
        }else {
            sdOperationLog.setOperationState(state);
        }
        sdOperationLog.setControlType("0");
        sdOperationLog.setState(String.valueOf(controlState));
        sdOperationLog.setOperIp(IpUtils.getIpAddr(ServletUtils.getRequest()));
        sdOperationLogService.insertSdOperationLog(sdOperationLog);
//        return AjaxResult.success(controlState);
    }

}
