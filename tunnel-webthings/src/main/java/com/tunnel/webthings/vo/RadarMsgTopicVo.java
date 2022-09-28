package com.tunnel.webthings.vo;

import com.tunnel.webthings.domain.RadarMsgTopic;
import com.tunnel.webthings.domain.SendMsgSuper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Arrays;

/**
 * @author ZHC
 * {@code @date} 2022/7/26 10:41
 * 雷达信息主题业务类
 */
@ApiModel("雷达信息主题业务类")
public class RadarMsgTopicVo extends SendMsgSuper {

    @ApiModelProperty("拓展")
    private RadarMsgTopic[] expands;

    public RadarMsgTopic[] getExpands() {
        return expands;
    }

    public void setExpands(RadarMsgTopic[] expands) {
        this.expands = expands;
    }

    @Override
    public String toString() {
        return "RadarMsgTopicVo{" +
                "expands=" + Arrays.toString(expands) +
                '}';
    }
}
