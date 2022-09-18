package com.tunnel.platform.business.vms.device;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 空设备
 *
 * @author yangqichao
 */
public class NoneDevice implements IDeviceMonitor {

    private static Map<String, String> map = new HashMap<String, String>();

    static {
        map.put("RESULT", "FALSE");
        map.put("MESSAGE", "无此设备！");
    }

    @Override
    public Map<String, String> getDeviceDisplayByDeviceId(String deviceId) {
        return map;
    }

    @Override
    public Map<String, String> getDeviceDisplayListByDeviceId(String deviceId) {
        return null;
    }

    @Override
    public Boolean controlDeviceByDeviceId(String deviceId, String protocolType, String command) {
        return null;
    }


    @Override
    public List<String> getDeviceFaultByDeviceId(String deviceId) {
        return null;
    }

    @Override
    public Map<String, String> getDeviceBadPointsByDeviceId(String deviceId) {
        return null;
    }

    @Override
    public Map<String, String> getDeviceBrightnessByDeviceId(String deviceId) {
        return null;
    }

    @Override
    public Map<String, String> updateBrightnessByDeviceId(String id, String mode, String brightness) {
        return null;
    }

    @Override
    public Map<String, String> getDevicePixelByDeviceId(String deviceId) {
        return null;
    }

}
