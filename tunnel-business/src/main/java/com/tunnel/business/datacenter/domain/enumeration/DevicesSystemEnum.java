package com.tunnel.business.datacenter.domain.enumeration;


/**
 * 设备系统
 *
 * @author zhai
 */
public enum DevicesSystemEnum {

    DEVICE_MONITORING("0", "监控系统"),
    DEVICE_VENTILATION("1", "通风系统"),
    DEVICE_LIGHTING("2", "照明系统"),
    DEVICE_ENERGY("3", "能源系统"),
    DEVICE_FIREFIGHTING("4", "消防系统");
    private String code;
    private String name;

    /**
     * 判断是否包含
     *
     * @param code
     * @return
     */
    public static Boolean contains(String code) {
        if (null == code) {
            return false;
        }
        for (DevicesSystemEnum typeEnum : DevicesSystemEnum.values()) {

            if (code.equals(typeEnum.code)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 传code返回name
     *
     * @param code
     * @return
     */
    public static String getValue(String code) {
        // 遍历枚举
        for (DevicesSystemEnum value : DevicesSystemEnum.values()) {
            String s = value.getCode() + "";
            if (s.equals(String.valueOf(code))) {
                return value.getName();
            }
        }
        // 其他情况
        return null;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    DevicesSystemEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }
}
