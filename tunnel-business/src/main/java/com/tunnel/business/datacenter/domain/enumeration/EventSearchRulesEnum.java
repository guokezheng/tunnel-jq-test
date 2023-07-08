package com.tunnel.business.datacenter.domain.enumeration;


/**
 * 预案事件检索规则条件
 *
 * @author zhai
 */
public enum EventSearchRulesEnum {

    ONE("1", "指定设备资源"),
    TWO("2", "最近3公里"),
    THREE("3", "最近5个"),
    FOUR("4", "事发上游所有"),
    FIVE("5", "事发下游所有"),
    SEVEN("6", "事件下游最近1个");
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
        for (EventSearchRulesEnum typeEnum : EventSearchRulesEnum.values()) {

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
        for (EventSearchRulesEnum value : EventSearchRulesEnum.values()) {
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

    EventSearchRulesEnum(String code, String name) {
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
