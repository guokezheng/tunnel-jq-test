package com.tunnel.business.datacenter.domain.enumeration;


/**
 * 门架情报板颜色
 *
 * @author zhai
 */
public enum VmsColorEnum {

    HUANG_SE("255255000000", "1"),
    HONG_SE("255000000000", "2"),
    LV_SE("000255000000", "3");
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
        for (VmsColorEnum typeEnum : VmsColorEnum.values()) {

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
        for (VmsColorEnum value : VmsColorEnum.values()) {
            String s = value.getCode() + "";
            if (s.equals(String.valueOf(code))) {
                return value.getName();
            }
        }
        // 其他情况
        return null;
    }

    /**
     * 传name返回code
     *
     * @param name
     * @return
     */
    public static String getCode(String name) {
        // 遍历枚举
        for (VmsColorEnum value : VmsColorEnum.values()) {
            String s = value.getName() + "";
            if (s.equals(String.valueOf(name))) {
                return value.getCode();
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

    VmsColorEnum(String code, String name) {
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
