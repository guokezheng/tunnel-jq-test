package com.tunnel.business.datacenter.domain.enumeration;


/**
 * 车牌颜色
 *
 * @author zhai
 */
public enum HwPlateColorEnum {

    BAI_DI_HEI_ZI("0", "3"),
    HUI_SE("1", "9"),
    HUANG_DI_HEI_ZI("2", "1"),
    FEN_SE("3", "9"),
    HONG_SE("4", "12"),
    LV_DI_BAI_HEI_ZI("5", "11"),
    LAN_DI_BAI_ZI("6", "0"),
    ZONG_SE("7", "9"),
    HEI_DI_BAI_ZI("8", "2"),
    WEI_ZHI("9", "9"),
    ZI_SE("10", "9"),
    CHENG_SE("11", "9"),
    YIN_SE("12", "9"),
    QING_SE("13", "9"),
    JIN_SE("14", "9"),
    TOU_MING("15", "9"),
    JIAN_BIAN_LV_DI_HEI_ZI("20", "4"),
    HUANG_LV_SHUANG_PIN_HEI_ZI("21", "5"),
    QI_TA("99", "9"),
    ;
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
        for (HwPlateColorEnum typeEnum : HwPlateColorEnum.values()) {

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
        for (HwPlateColorEnum value : HwPlateColorEnum.values()) {
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

    HwPlateColorEnum(String code, String name) {
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
