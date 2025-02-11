package com.zc.relay.handler.uplink;

import com.zc.relay.dto.UplinkDeviceData;
import com.zc.relay.dto.UplinkDeviceStateData;
import com.zc.relay.dto.UplinkOperationStateData;

/**
 * 上行指令（通讯端与物联平台端接口）
 * 约定了数据解析到物模型的接口
 */
public interface UplinkInstructHandle
{
    /**
     * 设备状态变化通知
     * @param uplinkDeviceStateData 设备状态数据
     */
    void uplinkDeviceState(UplinkDeviceStateData uplinkDeviceStateData);

    /**
     * 设备上报消息
     * @param uplinkDeviceData 上报数据设备实时数据
     */
    void uplinkDeviceMsg(UplinkDeviceData uplinkDeviceData);

    /**
     * 上报操作状态
     * @param uplinkOperationStateData 操作响应数据
     */
    void uplinkOperationState(UplinkOperationStateData uplinkOperationStateData);
}
