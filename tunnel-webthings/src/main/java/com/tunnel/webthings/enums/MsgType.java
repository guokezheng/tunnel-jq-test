package com.tunnel.webthings.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * @author dzy
 * @date 2022/7/19 14:34
 */
@Getter
@AllArgsConstructor
public enum MsgType {
    /**
     * 车道上下行
     */
    direct1("1","上行"),
    direct2("2","下行"),

    /**
     * 消息编码
     */
    msgId01("01","状态数据"),
    msgId02("02","事件数据"),
    msgId03("03","指令数据"),
    msgUp("up","上行物联数据"),
    msgDown("down","下行kafka数据"),
    /**
     * 消息类型
     */
    msg11("11","修改设备信息接口"),
    msg12("12","设备删除接口"),
    msg13("13","增加设备自定义标签接口"),
    msg14("14","删除设备自定义标签接口"),
    msg15("15","获取设备列表接口"),
    msg16("16","获取设备详情信息接口"),
    msg21("21","设备/平台上报基础信息接口"),
    msg22("22","设备/平台上报设备状态接口"),
    msg23("23","设备/平台上报数据接口"),
    msg24("24","设备/平台上报设备告警状态接口"),
    msg31("31","设备/平台控制指令(多设备同类型)"),
    msg32("32","设备/平台控制指令(单设备多子设备)"),
    msg41("41","获取最新版本接口"),
    msg42("42","程序升级结果上报接口"),
    msg51("51","获取设备/平台最新状态接口"),
    msg52("52","获取多个设备最新状态接口"),
    msg53("53","程序升级通知接口");

    private final String code;
    private final String value;

    public static String getValue(String code) {
        // 遍历枚举
        for (MsgType value : MsgType.values()) {
            if (value.getCode().equals(code)){
                return value.getValue();
            }
        }
        // 其他情况
        return null;
    }
}
