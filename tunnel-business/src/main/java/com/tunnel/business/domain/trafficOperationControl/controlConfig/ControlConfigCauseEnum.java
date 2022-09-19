package com.tunnel.business.domain.trafficOperationControl.controlConfig;

/**
 * describe: 管控等级配置-管控原因
 *
 * @author zs
 * @date 2022/2/15
 */
public enum ControlConfigCauseEnum {

    /**
     * 能见度
     */
    visibility("1","能见度"),

    /**
     * 路面情况
     */
    road_condition("2","路面情况"),

    /**
     * 拥挤度
     */
    congestion_degree("3","拥挤度"),


    /**
     * 突发事件
     */
    emergency_incident("4","突发事件");

    /**
     * 代号
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    ControlConfigCauseEnum(String code,String name){
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
