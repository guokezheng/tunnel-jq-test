package com.tunnel.platform.domain.digitalmodel;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dzy
 * @date 2022/9/12 11:42
 */
@Getter
@AllArgsConstructor
public enum WJEnum {

    /**
     * 机动车违停
     */
    CAR_STOP(1,"4"),

    /**
     * 机动车超速
     */
    CAR_SPEEDING(6,"14"),

    /**
     * 机动车慢行
     */
    CAR_SLOW(8,"11"),

    /**
     * 机动车变道
     */
    CAR_CHANGE_LANES(16,"13"),

    /**
     * 行人进入隧道
     */
    CAR_PEDESTRIAN(21,"16");

    private int code;
    private String number;


    /**
     * 传code返回name
     * @param code
     * @return
     */
    public static String getValue(Byte code) {
        // 遍历枚举
        for (WJEnum value : WJEnum.values()) {
            String valueCode = value.getCode()+"";
            if (valueCode.equals(code+"")){
                return value.getNumber();
            }
        }
        // 其他情况
        return null;
    }
}
