package com.tunnel.business.datacenter.domain.enumeration;

/**
 * 设备类型
 *
 * @author yangqichao
 * @date 2019/9/18 14:45
 */
public enum DevicesHongTypeEnum {

    PU_TONG_CHE_ZHI(1L, "普通车道指示器"),

    ZHUO_ZHUAN_CHE_ZHI(2L, "带左转车道指示器"),

    JIAO_TONG_XIN_HAO_DENG(3L, "交通信号灯"),

    ZUO_JIAO_TONG_XIN_HAO_DENG(4L, "带左转交通信号灯"),

    FENG_JI(10L, "射流风机"),

    JUAN_LIAN_MEN(12L, "卷帘门");

    private Long code;
    private String name;

    DevicesHongTypeEnum(Long code, String name) {
        this.code = code;
        this.name = name;
    }

    public void setCode(Long code) {
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
    public static Boolean contains(Long code) {

        if (null == code) {
            return false;
        }

        for (DevicesHongTypeEnum typeEnum : DevicesHongTypeEnum.values()) {

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
    public static String getValue(Long code) {
        // 遍历枚举
        for (DevicesHongTypeEnum value : DevicesHongTypeEnum.values()) {
            String s = value.getCode() + "";
            if (s.equals(String.valueOf(code))) {
                return value.getName();
            }
        }
        // 其他情况
        return null;
    }


    public Long getCode() {
        return code;
    }

    public String getName() {
        return name;
    }


}
