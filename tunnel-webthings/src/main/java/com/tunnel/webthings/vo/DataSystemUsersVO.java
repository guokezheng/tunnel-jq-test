package com.tunnel.webthings.vo;

import com.tunnel.webthings.domain.DataSystemUsers;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ZHC
 * {@code @date} 2022/8/24 14:29
 * 数据中台系统用户业务对象
 */
@Data
@ApiModel("数据中台系统用户业务对象")
public class DataSystemUsersVO extends DataSystemUsers {

    @ApiModelProperty("每页条数")
    private Integer limit;

    @ApiModelProperty("页数")
    private Integer page;

}
