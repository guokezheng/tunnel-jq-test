package com.tunnel.business.datacenter.domain.enumeration;


/**
 * 事件导出模板信息
 *
 * @author zhai
 */
public enum EventExportEnum {

    CHU_LI_ZHONG("0", "exporttemplate/处理中.docx"),
    YI_CHU_LI("1", "exporttemplate/已处理.docx"),
    YI_GUA_QI("2", "exporttemplate/确认挂起误报.docx"),
    DAI_QUE_REN("3", "exporttemplate/待确认.docx"),
    YI_QUE_REN("4", "exporttemplate/确认挂起误报.docx"),
    WU_BAO("5", "exporttemplate/确认挂起误报.docx");
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
        for (EventExportEnum typeEnum : EventExportEnum.values()) {

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
        for (EventExportEnum value : EventExportEnum.values()) {
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

    EventExportEnum(String code, String name) {
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
