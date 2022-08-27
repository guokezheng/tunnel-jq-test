package com.tunnel.platform.domain.trafficOperationControl.controlConfig;

/**
 * describe: 管控措施枚举类
 *
 * @author zs
 * @date 2022/3/8
 */
public enum ControlMeasureEnum {

    /**
     * 限行车速
     */
    limitspeed("limitspeed","限行车速"),

//    /**
//     * 限行车道且限行车辆
//     */
//    limitline_limitcar("limitline&limitcar","限行车道且限行车辆"),

    /**
     * 限行车辆
     */
    limitcar("limitcar","限行车辆"),

    /**
     * 临时关闭
     */
    close("close","临时关闭"),

    /**
     * 间隔放行
     */
    interval("interval","间隔放行"),

    /**
     * 开放应急车道
     */
    emergencyLine("emergencyLine","开放应急车道");

    /**
     * 代号
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    ControlMeasureEnum(String code,String name){
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
