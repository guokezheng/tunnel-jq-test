package com.tunnel.deal.light;

import java.util.List;

public interface Light {

    /**
     * 调光
     * @param deviceId
     * @param bright
     */
    int setBrightness(String deviceId, Integer bright);

    /**
     * 开关 亮度
     * @param deviceId
     * @param openClose
     * @return 控制结果 1-成功，0-失败
     */
    int lineControl(String deviceId, Integer openClose, Integer brightness);



    void setBrightnessByList(List<String> deviceIds, Integer bright, String controlType, String operIp);
}
