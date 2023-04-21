package com.tunnel.business.datacenter.domain.enumeration;


/**
 * 车辆类型
 *
 * @author zhai
 */
public enum HwVehicleTypeEnum {

    JIAO_CHE("1", "24"),
    HUO_CHE("2", "17"),
    MIAN_BAO_CHE("3", "13"),
    KE_CHE("4", "16"),
    XIAO_HUO_CHE("5", "14"),
    S_U_V("6", "4"),
    ZHONG_XING_KE_CHE("7", "25"),
    MO_TUO_CHE("8", "8"),
    QI_TA("9", "4"),
    YUE_YE_CHE("10", "12"),
    SHANG_WU_CHE("11", "19"),
    SAN_LUN_CHE("12", "7"),
    PI_KA_CHE("13", "18"),
    GUA_CHE("14", "27"),
    JIAO_BAN_CHE("15", "4"),
    GUAN_CHE("16", "28"),
    SUI_DIAO_CHE("17", "4"),
    XIN_NENG_YUAN_CHE("18", "4"),
    GONG_CHENG_CHE("20", "4"),
    XIAO_CHE("21", "4"),
    NI_TOU_CHE("22", "4"),
    GAO_WEI_CHE("23", "4"),
    CHU_ZU_CHE("24", "4"),
    YOU_GUAN_CHE("25", "28");
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
        for (HwVehicleTypeEnum typeEnum : HwVehicleTypeEnum.values()) {

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
        for (HwVehicleTypeEnum value : HwVehicleTypeEnum.values()) {
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

    HwVehicleTypeEnum(String code, String name) {
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
