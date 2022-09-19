package com.tunnel.business.datacenter.domain.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 设备类型
 *
 * @author yangqichao
 */
@Getter
@AllArgsConstructor
public enum DevicesStatusEnum {

    DEVICE_ON_LINE("1", "在线"), DEVICE_OFF_LINE("2", "离线"), DEVICE_ERROR("3", "故障");
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
        for (DevicesStatusEnum typeEnum : DevicesStatusEnum.values()) {

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
        for (DevicesStatusEnum value : DevicesStatusEnum.values()) {
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
}
