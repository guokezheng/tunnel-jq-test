package com.tunnel.platform.business.vms.device;


import com.ruoyi.common.utils.StringUtils;

import java.util.List;
import java.util.Map;

public class DeviceManagerFactory implements IDeviceMonitor {

    /*
     * 目前支持的类型。
     */
    final static int DEVICE_TYPE_BOARD = 0;
    final static int DEVICE_TYPE_NONE = -1;

    final static String DEVICE_PREFIX_BOARD = "1";
    final static String DEVICE_PREFIX_NONE = "0";

    /**
     * 懒汉模式实例化。
     */
    private static DeviceManagerFactory instance;

    public static DeviceManagerFactory getInstance() {
        if (instance == null)
            instance = new DeviceManagerFactory();
        return instance;
    }

    private DeviceManagerFactory() {
    }

    /**
     * 根据设备标识获取设备状态信息。
     *
     * @param deviceId
     * @return
     */
    private IDeviceMonitor getDeviceMonitorByDeviceId(String deviceId) {
        int type = getDeviceTypeById(deviceId);
        IDeviceMonitor deviceMonitor = new NoneDevice();
        if (type == DEVICE_TYPE_BOARD) {
            deviceMonitor = new BroadDevice();
        }
        return deviceMonitor;
    }

    /**
     * 根据标准统一的ID来判断设备类型
     *
     * @param deviceId
     * @return
     * @Description: 根据ID获取设备类型。需要重构
     */
    private static int getDeviceTypeById(String deviceId) {
        if (StringUtils.isEmpty(deviceId))
            return DEVICE_TYPE_NONE;
        else if (deviceId.startsWith(DEVICE_PREFIX_BOARD)) {
            return DEVICE_TYPE_BOARD;
        } else {
            return DEVICE_TYPE_NONE;
        }
    }


    /**
     * 获取情报板当前显示信息
     */
    public Map<String, String> getDeviceDisplayByDeviceId(String deviceId) {
        return getDeviceMonitorByDeviceId(deviceId).getDeviceDisplayByDeviceId(deviceId);
    }

    /*
     * 获取情报板全部显示内容
     * */
    public Map<String, String> getDeviceDisplayListByDeviceId(String id) {
        return getDeviceMonitorByDeviceId(id).getDeviceDisplayListByDeviceId(id);
    }

    /*
     * 修改修改显示内容
     * */
    public Boolean controlDeviceByDeviceId(String deviceId, String  protocolType,String command) {
        return getDeviceMonitorByDeviceId(deviceId).controlDeviceByDeviceId(deviceId, protocolType,command);
    }

    /*
     * 获取设备故障信息
     * */
    public List<String> getDeviceFaultByDeviceId(String id) {
        return getDeviceMonitorByDeviceId(id).getDeviceFaultByDeviceId(id);
    }


    /*
     * 获取设备LED 坏点信息
     * */
    public Map<String, String> getDeviceBadPointsByDeviceId(String id) {
        return getDeviceMonitorByDeviceId(id).getDeviceBadPointsByDeviceId(id);
    }

    /*
     * 获取设备亮度
     * */
    public Map<String, String> getDeviceBrightnessByDeviceId(String id) {
        return getDeviceMonitorByDeviceId(id).getDeviceBrightnessByDeviceId(id);
    }

    /**
     * 调节设备亮度。
     */
    public Map<String, String> updateBrightnessByDeviceId(String id, String mode, String brightness) {
        return getDeviceMonitorByDeviceId(id).updateBrightnessByDeviceId(id, mode, brightness);
    }

    /**
     * 获取设备像素大小。
     */
    public Map<String, String> getDevicePixelByDeviceId(String id) {
        return getDeviceMonitorByDeviceId(id).getDevicePixelByDeviceId(id);
    }

}