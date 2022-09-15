package com.tunnel.platform.datacenter.domain.enumeration;

/**
 * describe: 字典类型枚举类
 *
 * @author zs
 * @date 2022/3/4
 */
public enum DictTypeEnum {

    /**
     * 设备方向
     */
    sd_direction("sd_direction","设备方向"),

    /**
     * 管控类别
     */
    sd_control_type("sd_control_type","管控类别"),

    /**
     * 管控级别
     */
    sd_control_level("sd_control_level","管控级别"),


    /**
     * 管控措施
     */
    sd_control_measure("sd_control_measure","管控措施"),


    /**
     * 道路管制措施
     */
    sd_road_control("sd_road_control","道路管制措施"),

    /**
     * 限制类型
     */
    sd_limit_type("sd_limit_type","限制类型"),

    /**
     * 车辆类型
     */
    sd_limit_car_type("sd_limit_car_type","车辆类型"),

    /**
     * 限制速度
     */
    sd_limit_speed("sd_limit_speed","限制速度"),

    sd_road_condition("sd_road_condition","路面情况"),

    sd_congestion_degree("sd_congestion_degree","拥挤度"),

    incident_type("incident_type","事件类型"),

    sd_incident_level("sd_incident_level","事件级别");

    DictTypeEnum(String code,String name){
        this.code = code;
        this.name = name;
    }

    /**
     * 代号
     */
    private String code;

    /**
     * 名称
     */
    private String name;

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
