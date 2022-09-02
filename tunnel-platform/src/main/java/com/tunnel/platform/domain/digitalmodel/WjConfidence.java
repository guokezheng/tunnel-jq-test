package com.tunnel.platform.domain.digitalmodel;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author dzy
 * @date 2022/9/2 10:07
 */
@Data
public class WjConfidence {

    @ApiModelProperty("目标全域ID")
    private Integer targetId;

    @ApiModelProperty("事件置信度")
    private Integer eventConfidence;

    @ApiModelProperty("事件id")
    private Long eventIds;
}
