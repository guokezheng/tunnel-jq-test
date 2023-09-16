package com.tunnel.business.datacenter.domain.enumeration;


/**
 * 隧道
 *
 * @author zhai
 */
public enum RoBotVoiceEnum {

    WEI_TING("4", "隧道内禁止异常停车请尽快驶离"),
    NI_XING("1", "隧道内禁止逆行请安全驾驶"),
    CHAO_SU("14", "隧道内禁止超速请安全驾驶"),
    MAN_XING("11", "隧道内禁止慢行请加速驶离"),
    BIAN_DAO("13", "隧道内禁止违法变道请安全驾驶"),
    YING_JI_CHE_DAO("15", "隧道内禁止违法占用应急车道请尽快驶离"),
    HUO_ZAI("20", "隧道内发生火灾请尽快驶离");
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
        for (RoBotVoiceEnum typeEnum : RoBotVoiceEnum.values()) {

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
        for (RoBotVoiceEnum value : RoBotVoiceEnum.values()) {
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

    RoBotVoiceEnum(String code, String name) {
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
