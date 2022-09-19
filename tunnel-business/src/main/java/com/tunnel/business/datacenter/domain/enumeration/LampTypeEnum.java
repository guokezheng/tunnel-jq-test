package com.tunnel.business.datacenter.domain.enumeration;

public enum LampTypeEnum {
    JI_BEN_ZHAO_MING_1(1, "基本照明1"),
    JI_BEN_ZHAO_MING_2(2, "基本照明2"),
    JIA_QIANG_ZHAO_MING_1(3, "加强照明1"),
    JIA_QIANG_ZHAO_MING_2(4, "加强照明2"),
    JIA_QIANG_ZHAO_MING_3(5, "加强照明3"),
    YIN_DAO_ZHAO_MING(6, "引道照明"),
    PENG_DONG_ZHAO_MING(7, "棚洞照明");

    private int code;
    private String name;

    LampTypeEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
