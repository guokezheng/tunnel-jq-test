package com.tunnel.business.domain.digitalmodel;


/**
 * @author dzy
 * @date 2022/9/12 11:42
 */
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
    CAR_PEDESTRIAN(21,"16"),

    /**
     * 遗撒物
     */
    CAR_DROP_DUST(17,"3"),

    /**
     * 逆行
     */
    CAR_RETROGRADE(3,"1");

    private int code;
    private String number;

    WJEnum(int code, String number) {
        this.code = code;
        this.number = number;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

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
