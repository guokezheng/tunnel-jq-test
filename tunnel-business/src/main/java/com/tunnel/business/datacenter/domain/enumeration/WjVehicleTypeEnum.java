package com.tunnel.business.datacenter.domain.enumeration;


/**
 * 车辆类型
 *
 * @author zhai
 */
public enum WjVehicleTypeEnum {

    WEI_ZHI("0","0"),
    XIAO_KE_CHE("1","25"),
    DA_HUO_CHE("2","17"),
    DA_BA_CHE("3","16"),
    XING_REN("4","5"),
    ZI_XING_CHE("5","6"),
    DIAN_DONG_CHE("6","6"),
    ZHONG_BA_CHE("7","15"),
    WEI_HUA_CHE("8","40"),
    YI_SA_WU("9","4"),
    XIAO_HUO_CHE("10","14"),
    ZHONG_HUO_CHE("11","26"),
    XIAO_BA_CHE("15","25"),
    LU_ZHUI("16","4"),
    QI_XING_ZHE("17","6");
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
        for (WjVehicleTypeEnum typeEnum : WjVehicleTypeEnum.values()) {

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
        for (WjVehicleTypeEnum value : WjVehicleTypeEnum.values()) {
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

    WjVehicleTypeEnum(String code, String name) {
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
