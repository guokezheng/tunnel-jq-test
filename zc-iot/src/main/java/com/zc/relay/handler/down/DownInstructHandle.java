package com.zc.relay.handler.down;

import com.alibaba.fastjson.JSONObject;
import com.zc.relay.dto.DownData;
import com.zc.relay.dto.DownDataChildDevice;
import com.zc.relay.dto.DownSetData;
import com.zc.relay.dto.DownSetDataChildDevice;

/**
 * 下行指令接口
 */
public interface DownInstructHandle
{
    /**
     * 设置属性
     * @param downSetData 下行指令数据
     * @return
     */
    boolean setAttribute(DownSetData<Number> downSetData);

    /**
     * 设置子设备属性
     * @param downSetDataChildDevice 下行指令数据
     * @return
     */
    boolean setAttribute(DownSetDataChildDevice<Number> downSetDataChildDevice);

    /**
     * 获取子设备属性
     * @param downData 下行指令数据
     * @return
     */
    boolean getAttribute(DownData downData);

    /**
     * 获取子设备属性
     * @param downDataChildDevice 下行指令数据
     * @return
     */
    boolean getAttribute(DownDataChildDevice downDataChildDevice);


    /**
     * 服务调用
     * @param downSetData 下行指令数据
     * @return
     */
    boolean serveInvoke(DownSetData<JSONObject> downSetData);

    /**
     * 服务调用（子设备）
     * @param downSetDataChildDevice 下行指令数据
     * @return
     */
    boolean serveInvoke(DownSetDataChildDevice<JSONObject> downSetDataChildDevice);
}
