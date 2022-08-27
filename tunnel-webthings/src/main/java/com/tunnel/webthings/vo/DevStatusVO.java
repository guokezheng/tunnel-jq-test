package com.tunnel.webthings.vo;

import com.tunnel.webthings.domain.RadarMsgTopic;
import com.tunnel.webthings.domain.SendMsgSuper;
import lombok.Data;

/**
 * @author dzy
 * @date 2022/7/22 14:27
 */
@Data
public class DevStatusVO extends SendMsgSuper {

    RadarMsgTopic[] expands;

}
