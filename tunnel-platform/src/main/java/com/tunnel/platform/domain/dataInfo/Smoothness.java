package com.tunnel.platform.domain.dataInfo;

public enum Smoothness {
    /**
     * 未定义
     */
    UNKNOWN(0, "未定义"),

    /**
     * 畅通
     */
    SMOOTH(1, "畅通"),

    /**
     * 缓行
     */
    SLOW(2, "缓行"),

    /**
     * 轻度拥堵
     */
    LIGHT_JAM(3, "轻度拥堵"),

    /**
     * 中度拥堵
     */
    MID_JAM(4, "中度拥堵"),

    /**
     * 严重拥堵
     */
    SEVERE_JAM(5, "严重拥堵"),

    /**
     * 中断
     */
    CUTOFF(6, "中断");

    Smoothness(int i, String name) {
    }
}
