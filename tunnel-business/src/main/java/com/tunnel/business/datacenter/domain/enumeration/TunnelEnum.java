package com.tunnel.business.datacenter.domain.enumeration;


/**
 * 隧道
 *
 * @author zhai
 */
public enum TunnelEnum {

    HU_SHAN("JQ-JiNan-WenZuBei-MJY", "胡山隧道"),
    HANG_SHAN_DONG("JQ-WeiFang-JiuLongYu-HSD", "杭山东隧道"),
    JIN_JIA_LOU("JQ-WeiFang-JiuLongYu-JJL", "金佳楼隧道"),
    MA_AN_SHAN("JQ-WeiFang-JiuLongYu-MAS", "马鞍山隧道"),
    TAI_HE_SHAN("JQ-WeiFang-MiaoZi-BJY", "泰和山隧道"),
    TIAN_CI_SHAN("JQ-WeiFang-MiaoZi-WCL", "天赐山隧道"),
    SHUANG_ZI_SHAN("JQ-WeiFang-YangTianShan-SZS", "双子山隧道"),
    YANG_TIAN_SHAN("JQ-WeiFang-YangTianShan-YTS", "仰天山隧道"),
    PAN_DING_SHAN("JQ-ZiBo-TaiHe-PDS", "盘顶山隧道"),
    QING_FENG_LING("JQ-ZiBo-TaiHe-QFL", "青风岭隧道"),

    FENG_HUANG_SHAN("WLJD-JiNan-YanJiuYuan-FHS", "凤凰山隧道");
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
        for (TunnelEnum typeEnum : TunnelEnum.values()) {

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
        for (TunnelEnum value : TunnelEnum.values()) {
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

    TunnelEnum(String code, String name) {
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
