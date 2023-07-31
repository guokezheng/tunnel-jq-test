package com.tunnel.business.datacenter.domain.enumeration;

/**
 * 设备类型
 *
 * @author tjw
 * @date
 */
public enum StatisticTypeEnum {


    RIBAO(0, "day"),
    YUEBAO(1, "month"),
    NIANBAO(2, "year");

    private Integer code;
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
        for (StatisticTypeEnum typeEnum : StatisticTypeEnum.values()) {

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
        for (StatisticTypeEnum value : StatisticTypeEnum.values()) {
            String s = value.getCode() + "";
            if (s.equals(String.valueOf(code))) {
                return value.getName();
            }
        }
        // 其他情况
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    StatisticTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }


}
