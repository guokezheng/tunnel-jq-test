package com.tunnel.business.datacenter.domain.enumeration;

/**
 * Enum工具类
 *
 * @author yangqichao
 */
public class EnumUtil {
    public static <T extends CodeEnum> String getType(Integer type, Class<T> t) {
        for (T item : t.getEnumConstants()) {
            if (item.getType() == type) {
                return item.getCommand();
            }
        }
        return "";
    }
}
