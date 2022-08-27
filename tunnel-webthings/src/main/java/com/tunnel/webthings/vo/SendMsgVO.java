package com.tunnel.webthings.vo;

import com.tunnel.webthings.domain.ConfluenceDevFaultWarn;
import com.tunnel.webthings.domain.SendMsgSuper;
import lombok.Data;


/**
 * @author dzy
 * @date 2022/7/21 14:34
 */
@Data
public class SendMsgVO extends SendMsgSuper {

    /**
     * 设备实体类
     */
    private ConfluenceDevFaultWarn expands;
}
