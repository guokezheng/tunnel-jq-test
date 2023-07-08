package com.tunnel.business.datacenter.domain.enumeration;


/**
 * 隧道
 *
 * @author zhai
 */
public enum TunnelEnum {

    HANG_SHAN_DONG("JQ-WeiFang-JiuLongYu-HSD", "杭山东隧道"),
    JIN_JIA_LOU("JQ-WeiFang-JiuLongYu-JJL", "金佳楼隧道"),
    MA_AN_SHAN("JQ-WeiFang-JiuLongYu-MAS", "马鞍山隧道"),
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
