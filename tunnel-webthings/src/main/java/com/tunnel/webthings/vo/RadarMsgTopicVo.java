package com.tunnel.webthings.vo;

import com.tunnel.webthings.domain.RadarMsgTopic;
import com.tunnel.webthings.domain.SendMsgSuper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ZHC
 * {@code @date} 2022/7/26 10:41
 * 雷达信息主题业务类
 */
@Data
@ApiModel("雷达信息主题业务类")
public class RadarMsgTopicVo extends SendMsgSuper {

    @ApiModelProperty("拓展")
    private RadarMsgTopic[] expands;

}
