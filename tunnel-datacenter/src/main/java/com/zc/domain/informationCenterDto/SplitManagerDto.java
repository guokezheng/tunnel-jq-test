package com.zc.domain.informationCenterDto;

import lombok.Data;

/**
 * 运营管理单位（拆账）表
 */
@Data
public class SplitManagerDto {

    //运营管理单位编号
    private String splitManagerId;

    //运营管理单位名称
    private String splitManagerName;
}
