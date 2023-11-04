package com.tunnel.business.datacenter.domain.enumeration;

/**
 * 事件类型
 *
 * @author zhai
 * @date 2023/11/01
 */
public enum SdEventTypeEnum {

    NI_XING(1L, "逆行"),
    TING_CHE(4L, "停车"),
    MAN_XING(11L, "慢行"),
    BIAN_DAO(13L, "变道"),
    CHAO_SU(14L, "超速"),
    YING_JI_CHE_DAO(15L, "应急车道"),
    HUO_ZAI(20L, "火灾"),
    DIAN_HUA(21L, "电话");


    private Long code;
    private String name;

    SdEventTypeEnum(Long code, String name) {
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

        for (SdEventTypeEnum typeEnum : SdEventTypeEnum.values()) {

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
        for (SdEventTypeEnum value : SdEventTypeEnum.values()) {
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
