package com.tunnel.deal.phone;

import com.tunnel.business.domain.dataInfo.SdDevices;

import java.util.Map;

/**
 * @author zhai
 * @date 2023/6/13
 */
public interface LdPhoneSpeak {

    /**
     * 单个广播播放音频
     *
     * @param map
     * @return
     */
    int playVoice(Map<String, Object> map, SdDevices sdDevices);
}
