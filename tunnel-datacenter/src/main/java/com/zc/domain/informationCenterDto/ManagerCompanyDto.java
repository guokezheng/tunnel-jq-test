package com.zc.domain.informationCenterDto;

import lombok.Data;

/**
 * 查询管养单位字典
 */
@Data
public class ManagerCompanyDto {

    //集团公司编码
    private String opmaCliqueId;

    //集团公司名称
    private String opmaCliqueName;

    //管养单位编码
    private String opmaManagerId;

    //管养单位名称
    private String opmaManagerName;
}
