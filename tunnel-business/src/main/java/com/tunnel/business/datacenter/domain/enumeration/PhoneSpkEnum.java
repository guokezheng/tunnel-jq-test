package com.tunnel.business.datacenter.domain.enumeration;

public enum PhoneSpkEnum {
    //回铃状态
    //ALERT("0", "回铃"),
    //被接听
    ANSWERED("4", "被接听"),
    //挂断
    BYE("1", "挂断"),
    //忙碌状态
    //BUSY("3", "忙碌"),
    //空闲状态
    IDLE("1", "空闲"),
    //离线状态
    OFFLINE("2", "离线"),



    //摘机挂机指令码(力电)
    INSTRUCT("01","摘机挂机指令"),
    INSTRUCT_OFF("02","摘机"),
    INSTRUCT_ON("01","挂机"),
    //运行状态指令码(力电)
    RUNSTART("02","运行状态指令"),
    RUNSTART_SUCCESS("00","正常"),
    RUNSTART_DOOR("01","门非法"),
    RUNSTART_FAULT("02","故障");

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
