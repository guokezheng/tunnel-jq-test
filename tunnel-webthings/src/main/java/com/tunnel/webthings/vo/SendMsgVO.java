package com.tunnel.webthings.vo;

import com.tunnel.webthings.domain.ConfluenceDevFaultWarn;
import com.tunnel.webthings.domain.SendMsgSuper;


/**
 * @author dzy
 * @date 2022/7/21 14:34
 */
public class SendMsgVO extends SendMsgSuper {

    /**
     * 设备实体类
     */
    private ConfluenceDevFaultWarn expands;

    public ConfluenceDevFaultWarn getExpands() {
        return expands;
    }

    public void setExpands(ConfluenceDevFaultWarn expands) {
        this.expands = expands;
    }

    @Override
    public String toString() {
        return "SendMsgVO{" +
                "expands=" + expands +
                '}';
    }

}
