package com.zc.domain.informationCenterDto;

import lombok.Data;

/**
 * 查询分中心（拆账）
 */
@Data
public class SplitSubCenterDto {

    //路段编码
    private String splitSectionId;

    //路段名称
    private String splitSectionName;

    //分中心编码
    private String splitSubcenterId;

    //分中心名称
    private String splitSubcenterName;
}
