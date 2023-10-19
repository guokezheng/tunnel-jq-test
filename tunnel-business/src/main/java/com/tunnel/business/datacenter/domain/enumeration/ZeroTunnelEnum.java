package com.tunnel.business.datacenter.domain.enumeration;


/**
 * 隧道
 *
 * @author zhai
 */
public enum ZeroTunnelEnum {

    HU_SHAN("JQ-JiNan-WenZuBei-MJY", "000000s001637000001","K0020+000"),
    HANG_SHAN_DONG("JQ-WeiFang-JiuLongYu-HSD", "000000s001637000010","K0135+000"),
    JIN_JIA_LOU("JQ-WeiFang-JiuLongYu-JJL", "000000s001637000009","K0105+000"),
    MA_AN_SHAN("JQ-WeiFang-JiuLongYu-MAS", "000000s001637000008","K0100+000"),
    TAI_HE_SHAN("JQ-WeiFang-MiaoZi-BJY", "000000s001637000005","K0090+000"),
    TIAN_CI_SHAN("JQ-WeiFang-MiaoZi-WCL", "000000s001637000004","K0085+000"),
    SHUANG_ZI_SHAN("JQ-WeiFang-YangTianShan-SZS", "000000s001637000007","K0095+000"),
    YANG_TIAN_SHAN("JQ-WeiFang-YangTianShan-YTS", "000000s001637000006","K0095+000"),
    PAN_DING_SHAN("JQ-ZiBo-TaiHe-PDS", "000000s001637000003","K0070+000"),
    QING_FENG_LING("JQ-ZiBo-TaiHe-QFL", "000000s001637000002","K0065+000");
    private String code;
    private String name;

    private String stake;

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
        for (ZeroTunnelEnum typeEnum : ZeroTunnelEnum.values()) {

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
        for (ZeroTunnelEnum value : ZeroTunnelEnum.values()) {
            String s = value.getCode() + "";
            if (s.equals(String.valueOf(code))) {
                return value.getName();
            }
        }
        // 其他情况
        return null;
    }

    /**
     * 传code返回stake
     *
     * @param code
     * @return
     */
    public static String getStake(String code) {
        // 遍历枚举
        for (ZeroTunnelEnum value : ZeroTunnelEnum.values()) {
            String s = value.getCode() + "";
            if (s.equals(String.valueOf(code))) {
                return value.getStake();
            }
        }
        // 其他情况
        return null;
    }

    public String getStake() {
        return stake;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    ZeroTunnelEnum(String code, String name, String stake) {
        this.code = code;
        this.name = name;
        this.stake = stake;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStake(String stake) {
        this.stake = stake;
    }
}
