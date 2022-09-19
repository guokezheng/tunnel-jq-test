package com.tunnel.webthings.vo;

import com.tunnel.business.domain.dataInfo.SdStateStorage;
import com.tunnel.webthings.domain.ReceiveTopic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ZHC
 * {@code @date} 2022/7/29 9:45
 * 状态存储业务类
 */
@Data
@ApiModel("状态存储业务类")
public class SdStateStorageVO extends ReceiveTopic {

    @ApiModelProperty("拓展")
    private SdStateStorage expands;



}
