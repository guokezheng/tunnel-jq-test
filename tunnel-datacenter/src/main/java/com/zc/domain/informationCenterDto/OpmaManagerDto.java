package com.zc.domain.informationCenterDto;

import lombok.Data;

/**
 * 查询管养公司编码信息
 */
@Data
public class OpmaManagerDto {

    //集团公司编码
    private String opmaCliqueId;

    //集团公司名称
    private String opmaCliqueName;

    //管养单位编码
    private String opmaManagerCropId;

    //管养单位名称
    private String opmaManagerCropName;
}
