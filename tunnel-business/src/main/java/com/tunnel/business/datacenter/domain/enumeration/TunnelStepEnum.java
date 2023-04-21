package com.tunnel.business.datacenter.domain.enumeration;

/**
 * 三晶照明系统:隧道段号
 */
public enum TunnelStepEnum {

     COTE_STEP("0", "light1", "棚洞段",DevicesTypeEnum.JIA_QIANG_ZHAO_MING.getCode()),

    ENTR_STEP1("1", "light2", "入口段1",DevicesTypeEnum.JIA_QIANG_ZHAO_MING.getCode()),

    TRAN_STEP1("2", "light3", "过渡段1",DevicesTypeEnum.JIA_QIANG_ZHAO_MING.getCode()),

    BASE_STEP("4", "light5", "基本段",DevicesTypeEnum.JI_BEN_ZHAO_MING.getCode()),

    EXIT_STEP1("5", "light6", "出口段1",DevicesTypeEnum.JIA_QIANG_ZHAO_MING.getCode());

    private String value;
    private String name;
    private String desc;
    private Long eqType;

    TunnelStepEnum(String value, String name, String desc, Long eqType) {
        this.name = name;
        this.value = value;
        this.desc = desc;
        this.eqType = eqType;
    }



    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
    public String getDesc() {
        return desc;
    }

    public Long getEqType() {
        return eqType;
    }

    public void setEqType(Long eqType) {
        this.eqType = eqType;
    }

    /**
     * 根据name 返回value
     *
     * @param name
     * @return
     */
    public static String getValue(String name) {
        // 遍历枚举
        for (TunnelStepEnum item : TunnelStepEnum.values()) {
            String s = item.getName();
            if (s.equals(name)) {
                return item.getValue();
            }
        }
        // 其他情况
        return null;
    }

    public static Long getEqType(String name) {
        // 遍历枚举
        for (TunnelStepEnum item : TunnelStepEnum.values()) {
            String s = item.getName();
            if (s.equals(name)) {
                return item.getEqType();
            }
        }
        // 其他情况
        return null;
    }

}

