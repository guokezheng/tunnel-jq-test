package com.zc.relay.dto;

/**
 * 操作响应数据
 */
public class UplinkOperationStateData {

    /**
     * 失败
     */
    public final static Integer FAIL = 0;

    /**
     * 成功
     */
    public final static Integer SUCCEED = 1;

    /**
     * 消息标识
     */
    private String identify;
    
    /**
     * 设备编号
     */
    private String deviceCode;

    /**
     * 功能标识
     */
    private String functionIdentify;

    /**
     * 操作状态
     */
    private Integer state;


    public String getIdentify() {
        return identify;
    }

    public void setIdentify(String identify) {
        this.identify = identify;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public String getFunctionIdentify() {
        return functionIdentify;
    }

    public void setFunctionIdentify(String functionIdentify) {
        this.functionIdentify = functionIdentify;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        if (!FAIL.equals(state) && !SUCCEED.equals(state)) return;
        this.state = state;
    }

    public void setStateFail() {
        this.state = FAIL;
    }

    public void setStateSucceed() {
        this.state = SUCCEED;
    }

}
