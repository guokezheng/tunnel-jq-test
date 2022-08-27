package com.tunnel.platform.domain.trafficOperationControl.controlConfig;

/**
 * describe: 交通突发事件类型
 *
 * @author zs
 * @date 2022/3/14
 */
public enum EmergencyIncidentTypeEnum {

    traffic_accident("traffic_accident","交通事故"),
    barrier_clear("barrier_clear","道路清障");

    /**
     * 代号
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    EmergencyIncidentTypeEnum(String code,String name){
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
