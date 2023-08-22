package com.tunnel.business.datacenter.domain.enumeration;


/**
 * kafkaTopic
 *
 * @author zhai
 */
public enum TopicEnum {

    //物联中台事件topic
    EVENT_TOPIC("wq_tunnelEvent", "eventTopic"),
    //物联中台设备状态topic
    DEV_STATUS_TOPIC("wq_devStatusTopic", "devStatusTopic"),
    //物联中台设备基础数据topic
    TUNNEL_DEVICE_BASE_TOPIC("tunnelDeviceBaseData", "tunnelDeviceBaseData"),
    //物联中台隧道基础数据topic
    TUNNEL_BASE_TOPIC("tunnelBaseData", "tunnelBaseData"),
    //物联中台雷达轨迹数据topic
    TUNNEL_RADAR_TOPIC("tunnelRadarData","tunnelRadarData"),
    //隧道车辆统计
    TUNNEL_STATISTICS_TOPIC("tunnelStatistics","tunnelStatistics"),
    //隧道车辆速度统计
    LANE_STATISTICS_TOPIC("laneStatistics","laneStatistics");

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
        for (TopicEnum typeEnum : TopicEnum.values()) {

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
        for (TopicEnum value : TopicEnum.values()) {
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

    TopicEnum(String code, String name) {
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
