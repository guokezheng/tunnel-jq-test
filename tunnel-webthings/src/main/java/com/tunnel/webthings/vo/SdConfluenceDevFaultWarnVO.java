package com.tunnel.webthings.vo;

import com.tunnel.webthings.domain.ConfluenceDevFaultWarn;
import com.tunnel.webthings.domain.ReceiveTopic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author ZHC
 * @date 2022/7/20 10:59
 * 合流区预警设备故障告警业务类
 */
@ApiModel("合流区预警设备故障告警业务类")
public class SdConfluenceDevFaultWarnVO extends ReceiveTopic {

    @ApiModelProperty("扩展")
    private ConfluenceDevFaultWarn expands;

    public ConfluenceDevFaultWarn getExpands() {
        return expands;
    }

    public void setExpands(ConfluenceDevFaultWarn expands) {
        this.expands = expands;
    }

    @Override
    public String toString() {
        return "SdConfluenceDevFaultWarnVO{" +
                "expands=" + expands +
                '}';
    }
}
