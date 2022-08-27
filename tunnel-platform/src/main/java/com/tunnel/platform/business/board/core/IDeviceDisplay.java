package com.tunnel.platform.business.board.core;

import java.util.List;

public interface IDeviceDisplay {

    /**
     * 获取当前亮度调节方式和显示亮度
     *
     * @param responseContent
     * @return
     */
    public String getCurrentBrightness(String responseContent);

    /**
     * 获取当前设备故障信息
     *
     * @param responseContent
     * @return
     */
    public List<String> getDeviceFault(String responseContent);

    /**
     * 获取情报板当前显示的内容
     *
     * @param responseContent
     * @return
     */
    public String getCurrentDisplay(String responseContent);

    /**
     * 获取情报板全部显示的列表信息
     *
     * @param responseContent
     * @return
     */
    public String getPlayListDisplay(String responseContent);

}
