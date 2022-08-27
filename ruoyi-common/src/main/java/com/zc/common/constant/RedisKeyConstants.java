package com.zc.common.constant;

/**
 *  Redis key 常量定义
 * @author Athena-xiepufeng
 */
public class RedisKeyConstants
{
    /**
     * 品类
     */
    public static final String IOT_CATEGORY = "iot:category";

    /**
     * 功能
     */
    public static final String IOT_FUNCTION = "iot:function";

    /**
     * 功能值
     */
    public static final String IOT_FUNCTION_VALUE = "iot:functionValue";

    /**
     * 产品
     */
    public static final String IOT_PRODUCT = "iot:product";

    /**
     * 设备
     */
    public static final String IOT_DEVICE = "iot:device";

    /**
     * 设备编号主键映射关系
     */
    public static final String IOT_DEVICE_CODE_ID = "iot:deviceCodeId";

    /**
     * 分组
     */
    public static final String IOT_GROUP = "iot:group";

    /**
     * 获取设备功能值 key
     */
    public static String  getDeviceFunctionValueKey(Long deviceId)
    {
        return "iot:deviceFunctionValue:" + deviceId;
    }

    /**
     * 获取功能标识符和功能id映射关系
     */
    public static String  getCategoryFunctionIdKey(Long categoryId)
    {
        return "iot:categoryFunctionId:" + categoryId;
    }

}
