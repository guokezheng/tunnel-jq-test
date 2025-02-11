package com.zc.relay.dto;

/**
 * 下行数据（设置参数：子设备）
 */
public class DownSetDataChildDevice<T> extends DownDataChildDevice {

    private T value;

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public void setAttrs(DownDataChildDevice downData){
        setIdentifier(downData.getIdentifier());
        setFunctionIdentify(downData.getFunctionIdentify());
        setDeviceCode(downData.getDeviceCode());
        setChildDeviceCode(downData.getChildDeviceCode());
    }
}
