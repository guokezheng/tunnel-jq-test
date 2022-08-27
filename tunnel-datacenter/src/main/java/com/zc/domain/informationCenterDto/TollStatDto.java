package com.zc.domain.informationCenterDto;

import lombok.Data;

/**
 * 收费站字典
 */
@Data
public class TollStatDto {

    //每页显示记录数
    private Integer limit;

    //国标路线id
    private String opmaCliqueId;

    //国标路线名称
    private String opmaCliqueName;

    //排序方式，可选值(asc、desc)
    private String order;

    //排序字段
    private String orderField;

    //当前页码，从1开始
    private Integer page;

    //国标路线id
    private String regionId;

    //国标路线名称
    private String regionName;

    //国标路线id
    private String roadId;

    //国标路线名称
    private String roadName;

    //收费站全称
    private String tollStationFullName;

    //收费站hex编码
    private String tollStationHex;

    //收费站编号
    private String tollStationId;

    //收费站名称
    private String tollStationName;

    //省标收费站编号
    private String tollStationProvinceId;
}
