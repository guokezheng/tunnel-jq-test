package com.zc.relay.dto;

/**
 * 下行数据（设置参数）
 */
public class DownSetData<T> extends DownData {

    private T value;

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public void setAttrs(DownData downData){
        setIdentifier(downData.getIdentifier());
        setFunctionIdentify(downData.getFunctionIdentify());
        setDeviceCode(downData.getDeviceCode());
    }
}
