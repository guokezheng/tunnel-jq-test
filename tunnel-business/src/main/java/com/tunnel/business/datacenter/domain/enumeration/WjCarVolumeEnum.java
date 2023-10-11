package com.tunnel.business.datacenter.domain.enumeration;


/**
 * 万集车流量链接
 *
 * @author zhai
 */
public enum WjCarVolumeEnum {

    HU_SHAN("JQ-JiNan-WenZuBei-MJY", "http://10.7.179.141:19355"),
    PAN_DING_SHAN("JQ-ZiBo-TaiHe-PDS", "http://10.7.185.107:19355"),
    QING_FENG_LING("JQ-ZiBo-TaiHe-QFL", "http://10.7.183.123:19355"),
    TAI_HE_SHAN("JQ-WeiFang-MiaoZi-BJY", "http://10.7.198.99:19355"),
    TIAN_CI_SHAN("JQ-WeiFang-MiaoZi-WCL", "http://10.7.196.75:19355"),
    SHUANG_ZI_SHAN("JQ-WeiFang-YangTianShan-SZS", "http://10.7.202.101:19355"),
    YANG_TIAN_SHAN("JQ-WeiFang-YangTianShan-YTS", "http://10.7.200.94:19355"),
    JIN_JIA_LOU("JQ-WeiFang-JiuLongYu-JJL", "http://10.7.204.19:19355"),
    MA_AN_SHAN("JQ-WeiFang-JiuLongYu-MAS", "http://10.7.201.17:19355");
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
        for (WjCarVolumeEnum typeEnum : WjCarVolumeEnum.values()) {

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
        for (WjCarVolumeEnum value : WjCarVolumeEnum.values()) {
            String s = value.getCode() + "";
            if (s.equals(String.valueOf(code))) {
                return value.getName();
            }
        }
        // 其他情况
        return null;
    }

    /**
     * 传name返回code
     *
     * @param name
     * @return
     */
    public static String getCode(String name) {
        // 遍历枚举
        for (WjCarVolumeEnum value : WjCarVolumeEnum.values()) {
            String s = value.getName() + "";
            if (s.equals(String.valueOf(name))) {
                return value.getCode();
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

    WjCarVolumeEnum(String code, String name) {
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
