package com.tunnel.business.datacenter.domain.enumeration;


/**
 * 车辆颜色
 *
 * @author zhai
 */
public enum HwVehicleColorEnum {

    BAI_SE("0", "2"),
    HUI_SE("1", "3"),
    HUANG_SE("2", "6"),
    FEN_SE("3", "12"),
    HONG_SE("4", "4"),
    LV_SE("5", "9"),
    LAN_SE("6", "5"),
    ZONG_SE("7", "8"),
    HEI_SE("8", "1"),
    WEI_ZHI("9", "99"),
    ZI_SE("10", "10"),
    CHENG_SE("11", "7"),
    YIN_SE("12", "99"),
    QING_SE("13", "11"),
    JIN_SE("14", "99"),
    TOU_MING("15", "13"),
    QI_TA("99", "2");
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
        for (HwVehicleColorEnum typeEnum : HwVehicleColorEnum.values()) {

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
        for (HwVehicleColorEnum value : HwVehicleColorEnum.values()) {
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

    HwVehicleColorEnum(String code, String name) {
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
