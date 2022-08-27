package com.tunnel.webthings.vo;

import com.tunnel.webthings.domain.RadarMsgTopic;
import com.tunnel.webthings.domain.ReceiveTopic;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author ZHC
 * {@code @date} 2022/7/22 10:49
 * 雷达信息数据业务类
 */
@Data
@ApiModel("雷达信息数据业务类")
public class SdRadarMsgTopicVO extends ReceiveTopic {

    private RadarMsgTopic expands;

}
