package com.tunnel.deal.light;

public interface Light {

    /**
     * 调光
     * @param deviceId
     * @param bright
     */
    int setBrightness(String deviceId, Integer bright);

    /**
     * 开关
     * @param deviceId
     * @param openClose
     * @return 控制结果 1-成功，0-失败
     */
    int lineControl(String deviceId, Integer openClose);


}
