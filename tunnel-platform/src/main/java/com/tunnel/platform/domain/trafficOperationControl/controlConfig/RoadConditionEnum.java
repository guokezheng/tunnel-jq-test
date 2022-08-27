package com.tunnel.platform.domain.trafficOperationControl.controlConfig;

/**
 * describe: 路面情况
 *
 * @author zs
 * @date 2022/2/25
 */
public enum RoadConditionEnum {

    /**
     * 积水
     */
     gathered_water("gathered_water","积水"),

    /**
     * 结冰
     */
    icy_road("icy_road","结冰"),
    /**
     * 积雪
     */
    snow_cover("snow_cover","积雪"),

    /**
     * 干燥
     */
    dry_road("dry_road","干燥"),

    /**
     * 湿润
     */
    wet_road("wet_road","湿润");

    RoadConditionEnum(String code,String name){
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


    public static String findCode(String name){
        for (RoadConditionEnum ele : values()) {
            if(ele.name().equals(name)) {
                return ele.getCode();
            }
        }
        return null;
    }

    public static String findName(String code){
        for(RoadConditionEnum ele : values()){
            if(ele.getCode().equals(code)){
                return ele.getName();
            }
        }
        return null;
    }

}
