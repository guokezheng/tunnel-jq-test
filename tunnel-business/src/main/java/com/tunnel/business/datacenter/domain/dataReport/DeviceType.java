package com.tunnel.business.datacenter.domain.dataReport;

/**
 * describe: 字典类型枚举类
 *
 * @author tjw
 * @date 2023/3/7
 */
public enum DeviceType {
    /**
     * 对应数据库中的设备类型
     */
    COVI("19", "COVI"),
    FENGSHUFENGXIANG("17", "风速风向"),
    DONGNEILIANGDU("18", "洞内亮度"),
    DONGWAILIANGDU("5", "洞外亮度"),
    DONGFANGDU("48", "风机内外振动仪检测器"),
    DONGWATERGDU("42", "水浸传感器"),
    DONGHUMIDGDU("41", "温湿度传感器"),
    DONGCATGDU("20", "微波车辆检测器"),

    /**
     * 对应前台自定义类型
     */
    COVIITEM("1", "COVI数据报表"),
    FENGSHUFENGXIANGITEM("2", "风速风向数据报表"),
    DONGNEILIANGDUITEM("3", "洞内亮度数据报表"),
    DONGWAILIANGDUITEM("4", "洞外亮度数据报表"),
    DONGFANGDUITEM("5", "风机内外振动仪检测器"),
    DONGWATERGDUITEM("6", "水浸传感器"),
    DONGHUMIDGDUITEM("7", "温湿度传感器"),
    DONGCATGDUITEM("8", "微波车辆检测器");


    DeviceType(String code, String name){
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
