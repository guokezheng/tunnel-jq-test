package com.zc.domain.informationCenterDto;

import lombok.Data;

/**
 * 节点信息
 */
@Data
public class NodeDto {

    //距离范围(单位：米)
    private String distance;

    //收费站或门架id
    private String id;

    //纬度
    private String lat;

    //经度
    private String lng;

    //所属运管单位编码
    private String managerId;

    //机构类型
    private String nodeType;

    //所属高速公路路线编码（运营业务）
    private String opmaRoadId;

    //所属高速公路路段编码（运营业务）
    private String opmaSectionId;

    //所在地市编码
    private String regionId;
}
