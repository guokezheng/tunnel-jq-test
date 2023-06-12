package com.tunnel.business.strategy.service;

import com.ruoyi.common.core.domain.AjaxResult;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.logRecord.SdOperationLog;

import java.util.Map;

/**
 * describe: 公共控制方法
 *
 * @author zs
 * @date 2023/5/11
 */
public interface CommonControlService {

    void sendNowDeviceStatusByWebsocket(SdDevices sdDevices, String[] state, SdOperationLog sdOperationLog, String type);

    /**
     * 设备模拟控制开启，直接变更设备状态为在线并展示对应运行状态
     * @param map
     * @param sdDevices
     * @return
     */
    Integer analogControl(Map<String, Object> map, SdDevices sdDevices);



    /**
     * 根据字典中配置的设备模拟控制值进行模拟状态展示
     * @return
     */
     boolean queryAnalogControlConfig();


    /**
     * 组装日志信息
     * @param map
     * @param sdDevices
     * @param controlState
     * @return
     */
     SdOperationLog getOperationLog(Map<String, Object> map,SdDevices sdDevices,Integer controlState);


    /**
     * 控制设备之前获取设备状态
     * 车指、交通信号灯、风机、卷帘门的设备类型
     * @param sdDevices 设备信息
     * @return
     */
     String selectBeforeState(SdDevices sdDevices);

    /**
     * 控制设备之前获取设备状态
     * @param deviceId 设备Id
     * @param itemCode 设备类型数据项
     * @return
     */
    String selectBeforeState(String deviceId,Long itemCode);

    /**
     * 添加操作日志
     * @param sdDevices 设备信息
     * @param beforeState 控制前状态
     * @param map 控制参数
     * @param controlState 操作是否成功 0 不成功；1成功
     */
    void addOperationLog(Map<String, Object> map,SdDevices sdDevices,String beforeState,int controlState);

//    /**
//     * 添加操作日志
//     * @param map 控制参数
//     * @param sdDevices 设备信息
//     * @param beforeState 控制前状态
//     * @param controlState 操作是否成功 0 不成功；1成功
//     * @param controlType 控制方式
//     */
//    void addOperationLog(Map<String, Object> map, SdDevices sdDevices, String beforeState, int controlState, String controlType);


    /**
     * 设备模拟控制开启，直接变更设备状态为在线并展示对应运行状态
     * @param sdDevices 设备信息
     * @param map 控制参数
     * @return
     */
     AjaxResult excecuteAnalogControl(SdDevices sdDevices, Map<String, Object> map);
}
