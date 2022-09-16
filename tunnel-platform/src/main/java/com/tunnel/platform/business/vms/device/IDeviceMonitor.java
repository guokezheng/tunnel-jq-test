package com.tunnel.platform.business.vms.device;

import java.util.List;
import java.util.Map;

public interface IDeviceMonitor {

    /**
     * 获取设备当前显示内容
     *
     * @param deviceId
     * @return
     */
    public Map<String, String> getDeviceDisplayByDeviceId(String deviceId);

    /**
     * 获取情报板全部显示内容
     *
     * @param deviceId
     * @return
     */
    public Map<String, String> getDeviceDisplayListByDeviceId(String deviceId);

    /**
     * 修改设备显示内容
     *
     * @param deviceId 设备标识
     * @param command  设备的控制命令
     * @return
     */
    public Boolean controlDeviceByDeviceId(String deviceId, String protocolType, String command);

    /**
     * 获取情报板故障信息
     *
     * @param deviceId
     * @return
     */
    public List<String> getDeviceFaultByDeviceId(String deviceId);

    /**
     * 获取情报板屏幕坏点数量
     *
     * @param deviceId
     * @return
     */
    public Map<String, String> getDeviceBadPointsByDeviceId(String deviceId);


    /**
     * 获取情报板获取亮度调节方式和显示亮度
     *
     * @param deviceId
     * @return
     */
    public Map<String, String> getDeviceBrightnessByDeviceId(String deviceId);

    /**
     * 根据设备标识情报板获取亮度调节方式和显示亮度
     *
     * @param id,mode,brightness
     * @return
     */
    public Map<String, String> updateBrightnessByDeviceId(String id, String mode, String brightness);

    /**
     * 根据设备标识情报板获取像素大小
     *
     * @param deviceId
     * @return
     */
    public Map<String, String> getDevicePixelByDeviceId(String deviceId);

}