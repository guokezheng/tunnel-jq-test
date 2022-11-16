package com.tunnel.business.datacenter.domain.enumeration;

/**
 * 防控类型
 *
 * @author zhai
 * @date 2022/11/16 10:05
 */
public enum PrevControlTypeEnum {

    TRAFFIC_NCIDENT("0","交通事件"),

    ACTIVE_SAFETY("1","主动安全"),

    EQUIPMENT_FAILURE("2","设备故障");

    private String code;
    private String name;

    PrevControlTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

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

        for (PrevControlTypeEnum typeEnum : PrevControlTypeEnum.values()) {

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
        for (PrevControlTypeEnum value : PrevControlTypeEnum.values()) {
            String s = value.getCode();
            if (s.equals(code)) {
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


}
