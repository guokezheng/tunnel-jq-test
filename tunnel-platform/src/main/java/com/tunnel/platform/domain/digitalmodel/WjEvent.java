package com.tunnel.platform.domain.digitalmodel;

import com.ruoyi.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author dzy
 * @date 2022/9/2 9:51
 */
@Data
public class WjEvent extends BaseEntity {

    @ApiModelProperty("事件编码")
    private Long eventId;

    @ApiModelProperty("事件类型")
    private Byte eventType;

    @ApiModelProperty("基站ID")
    private Byte stationId;

    @ApiModelProperty("车道号")
    private Byte laneNo;

    @ApiModelProperty("事件目标数量")
    private Byte targetNum;

    @ApiModelProperty("事件桩号")
    private String stakeNum;

    @ApiModelProperty("目标集合")
    private List<WjConfidence> targetList;

    @ApiModelProperty("事件位置经度")
    private Double eventLongitude;

    @ApiModelProperty("事件位置纬度")
    private Double eventLatitude;

    @ApiModelProperty("事件开始时间戳")
    private String eventTimeStampStart;

    @ApiModelProperty("事件结束时间戳")
    private String eventTimeStampEnd;
}
