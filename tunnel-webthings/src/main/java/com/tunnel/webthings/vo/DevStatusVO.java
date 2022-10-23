package com.tunnel.webthings.vo;

import com.tunnel.webthings.domain.RadarMsgTopic;
import com.tunnel.webthings.domain.SendMsgSuper;

import java.util.Arrays;

/**
 * @author dzy
 * @date 2022/7/22 14:27
 */
public class DevStatusVO extends SendMsgSuper {

    RadarMsgTopic[] expands;


    public RadarMsgTopic[] getExpands() {
        return expands;
    }

    public void setExpands(RadarMsgTopic[] expands) {
        this.expands = expands;
    }

    @Override
    public String toString() {
        return "DevStatusVO{" +
                "expands=" + Arrays.toString(expands) +
                '}';
    }
}
