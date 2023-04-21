package com.tunnel.business.datacenter.domain.enumeration;


/**
 * 隧道
 *
 * @author zhai
 */
public enum DeviceControlTypeEnum {

    AUTO_CONTROL("0", "手动控制"),
    TRIGGER_CONTROL("1", "定时控制"),
    AUTO_EXEC("2", "自动触发"),
    TIMES_CONTROL("3", "分时控制"),
    LIGHT_CONTROL("8", "光强控制"),
    RESERVE_CONTROL("4", "预案执行");
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
        for (DeviceControlTypeEnum typeEnum : DeviceControlTypeEnum.values()) {

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
        for (DeviceControlTypeEnum value : DeviceControlTypeEnum.values()) {
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

    DeviceControlTypeEnum(String code, String name) {
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
