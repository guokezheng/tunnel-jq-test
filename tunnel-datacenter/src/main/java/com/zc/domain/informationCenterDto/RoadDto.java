package com.zc.domain.informationCenterDto;

import lombok.Data;

/**
 * 国标公路字典
 */
@Data
public class RoadDto {

    //国标路线编码
    private String roadId;

    //国标路线名称
    private String roadName;

    //技术等级
    private String tecLevel;
}
