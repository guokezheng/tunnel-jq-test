package com.zc.domain.informationCenterDto;

import lombok.Data;

/**
 * 查询管辖路段（拆账）
 */
@Data
public class SplitSectionDto {

    //管理单位
    private String splitManagerId;

    //管理单位名称
    private String splitManagerName;

    //路段编码
    private String splitSectionId;

    //路段名称
    private String splitSectionName;
}
