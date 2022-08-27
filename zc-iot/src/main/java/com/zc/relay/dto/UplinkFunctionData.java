package com.zc.relay.dto;

/**
 * 功能数据
 */
public class UplinkFunctionData {
    /**
     * 功能标识符
     */
    private String identifier;

    /**
     * 实时值
     */
    private Number realValue;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Number getRealValue() {
        return realValue;
    }

    public void setRealValue(Number realValue) {
        this.realValue = realValue;
    }

}
