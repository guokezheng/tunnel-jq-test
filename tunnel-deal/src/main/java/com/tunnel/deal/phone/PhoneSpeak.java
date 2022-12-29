package com.tunnel.deal.phone;

import java.util.Map;

public interface PhoneSpeak {

    /**
     * 单个广播播放音频
     *
     * @param map
     * @return
     */
    int playVoice(String systemUrl, Map<String, Object> map);

    /**
     * 多个广播播放音频
     *
     * @param map
     * @return
     */
    int tempSpkGroup(String systemUrl,Map<String, Object> map);


    int hungUp(String systemUrl,Map<String, Object> map);

}
