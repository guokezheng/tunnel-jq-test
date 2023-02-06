package com.tunnel.business.datacenter.domain.enumeration;

public enum PhoneSpkEnum {
    //回铃状态
    ALERT("0", "回铃"),
    //被接听
    ANSWERED("1", "被接听"),
    //挂断
    BYE("2", "挂断"),
    //忙碌状态
    BUSY("3", "忙碌"),
    //空闲状态
    IDLE("4", "空闲"),
    //离线状态
    OFFLINE("5", "离线");

    private String code;
    private String name;

    PhoneSpkEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }


    public static String getValue(String name) {
        // 遍历枚举
        for (PhoneSpkEnum value : PhoneSpkEnum.values()) {
            if (value.toString().equalsIgnoreCase(name)){
                return value.getCode();
            }
        }
        // 其他情况
        return null;
    }

    public static void main(String[] args) {
        String offline = PhoneSpkEnum.getValue("OFFLINE");

        System.out.println();
    }


}
