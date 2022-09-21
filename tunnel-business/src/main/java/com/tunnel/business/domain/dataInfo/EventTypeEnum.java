package com.tunnel.business.domain.dataInfo;
/**
 * 设备状态
 */
public enum EventTypeEnum {
    /**
     * 未定义
     */
    UNKNOWN(0, "未定义"),

    /**
     * 事故
     */
    ACCIDENT(1, "事故"),

    /**
     * 逆行或倒车
     */
    REVERSE(2, "逆行或倒车"),

    /**
     * 超速
     */
    OVERSPEED(3, "超速"),

    /**
     * 拥堵
     */
    TRAFFICJAM(4, "拥堵"),

    /**
     * 缓行
     */
    SLOW(5, "缓行"),

    /**
     * 停车
     */
    PARKING(6, "停车"),

    /**
     * 变道
     */
    LANE_CHANGE(7, "变道"),

    /**
     * 跟车太近
     */
    TOO_CLOSE(8, "跟车太近"),

    /**
     * 正常天气白天
     */
    NORMAL_WEATHER(90, "正常天气白天"),

    /**
     * 正常天气夜晚
     */
    NORMAL_WEATHER_NIGHT(91, "正常天气夜晚"),

    /**
     * 雨雾天气白天
     */
    RAIN_WEATHER(92, "雨雾天气白天"),

    /**
     * 雨雾天气夜晚
     */
    RAIN_WEATHER_NIGHT(93, "雨雾天气夜晚"),
    ;

    EventTypeEnum(int i, String name) {

    }
}
