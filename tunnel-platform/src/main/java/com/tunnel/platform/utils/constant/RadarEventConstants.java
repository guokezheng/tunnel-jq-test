package com.tunnel.platform.utils.constant;

/**
 * describe: 万集对接常量类
 *
 * @author zs
 * @date 2022/9/5
 */
public class RadarEventConstants {

    /**
     * 万集雷达信息 redis cache key
     */
    public static final String WJ_LIDAR_INFO_KEY = "wj_lidar_info:";

    /**
     * 万集摄像机信息 redis cache key
     */
    public static final String WJ_CAMERA_INFO_KEY = "wj_camera_info:";

    /**
     * 万集设备状态数据redis cache key
     */
    public static final String DEVICE_DATA = "device_data:";

    /**
     * 万集感知数据topic matchResultData
     * 消费者
     */
    public static final String MATCHRESULTDATA = "matchResultData";

    /**
     * 万集雷达设备运行数据topic wjDeviceRunningInfo
     * 消费者
     */
    public static final String WJDEVICERUNNINGINFO = "wjDeviceRunningInfo";

    /**
     *万集 设备运行状态数据topic baseDeviceStatus
     * 生产者
     */
    public static final String BASEDEVICESTATUS ="baseDeviceStatus";

    public static final String TEST ="test";


}
