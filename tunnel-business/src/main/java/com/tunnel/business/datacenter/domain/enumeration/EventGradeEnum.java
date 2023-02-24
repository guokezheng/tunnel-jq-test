package com.tunnel.business.datacenter.domain.enumeration;


/**
 * 隧道
 *
 * @author zhai
 */
public enum EventGradeEnum {

    YI_BAN("1", "一般"),
    JIAO_DA("2", "较大"),
    ZHONG_DA("3", "重大"),
    TE_BIE_ZHONG_DA("4", "特别重大");
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
        for (EventGradeEnum typeEnum : EventGradeEnum.values()) {

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
        for (EventGradeEnum value : EventGradeEnum.values()) {
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

    EventGradeEnum(String code, String name) {
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
