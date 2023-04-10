package com.tunnel.deal.light.enums;

/**
 * describe: 三晶照明状态枚举类
 * 维护两个系统间的状态对应关系
 *
 * @author zs
 * @date 2023/4/3
 */
public enum SanjingLightStateEnum {

    /**
     * 开
     */
    OPEN(1,0),
    /**
     * 关
     */
    CLOSE(2,1);


    SanjingLightStateEnum(Integer code,Integer state){
        this.code = code;
        this.state = state;
    }


    /**
     * 平台状态
     */
    private Integer code;

    /**
     * 三晶系统状态
     */
    private Integer state;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }


    /**
     * 传code返回 state
     *
     * @param code
     * @return
     */
    public static Integer getValue(Integer code) {
        // 遍历枚举
        for (SanjingLightStateEnum value : SanjingLightStateEnum.values()) {
            Integer s = value.getCode();
            if (s.equals(code)) {
                return value.getState();
            }
        }
        // 其他情况
        return null;
    }
}
