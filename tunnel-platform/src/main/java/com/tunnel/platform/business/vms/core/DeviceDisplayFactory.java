package com.tunnel.platform.business.vms.core;

import com.ruoyi.common.utils.StringUtils;
import com.tunnel.platform.business.vms.DevicesConfig;
import com.tunnel.platform.business.vms.display.*;
import com.tunnel.platform.business.vms.DevicesBean;

import java.util.List;

/**
 * 情报板信息显示工厂类
 *
 * @author Alvin
 * @date 2018年7月13日
 */
public abstract class DeviceDisplayFactory {

    /**
     * 获取当前亮度调节方式和显示亮度
     *
     * @param deviceId
     * @param responseContent
     * @return
     */
    public static String getCurrentBrightness(String deviceId, String responseContent) {
        return getDeviceDisplay(deviceId).getCurrentBrightness(responseContent);
    }

    /**
     * 获取当前故障信息
     *
     * @param deviceId
     * @param responseContent
     * @return
     */
    public static List<String> getDeviceFault(String deviceId, String responseContent) {
        return getDeviceDisplay(deviceId).getDeviceFault(responseContent);
    }

    /**
     * 获取当前显示内容
     *
     * @param deviceId
     * @param responseContent
     * @return
     */
    public static String getCurrentDisplay(String deviceId, String responseContent) {
        return getDeviceDisplay(deviceId).getCurrentDisplay(responseContent);
    }

    /**
     * 获取当前全部显示内容
     *
     * @param deviceId
     * @param responseContent
     * @return
     */
    public static String getPlayListDisplay(String deviceId, String responseContent) {
        return getDeviceDisplay(deviceId).getPlayListDisplay(responseContent);
    }

    /**
     * 根据设备标识，获取不同厂家协议解析
     *
     * @param deviceId
     * @return
     */
    private static IDeviceDisplay getDeviceDisplay(String deviceId) {
        // 1.如果设备标识为空，则返回null值
        if (StringUtils.isEmpty(deviceId))
            return new NoneDeviceDisplay();
        // 2.如果未获取设备基本信息，则返回未null值
        DevicesBean bean = new DevicesConfig().getDevicesById(deviceId);
        if (bean == null || bean.getVender_version() == null) {
            return new NoneDeviceDisplay();
        }
        // 3.判断
        String vender = bean.getVender_version();
        if (vender.startsWith(IDeviceProtocol.TONGZHOU) || vender.startsWith(IDeviceProtocol.DIANMING)) {//同洲 或电明
            return new DianMingAndTongZhouDeviceDisplay();
        } else if (vender.startsWith(IDeviceProtocol.XIANKE)) {//显科
            return new XianKeDeviceDisplay();
        } else if (vender.startsWith(IDeviceProtocol.SANSI) || vender.startsWith(IDeviceProtocol.GUANGDIAN)) {//三思 或光电
            return new GuangDianAndSanSiDeviceDisplay();
        } else if (vender.startsWith(IDeviceProtocol.DINGEN)) {//鼎恩
            return new DingEnDeviceDisplay();
        } else {
            return new CommonDeviceDisplay();
        }
    }

}
