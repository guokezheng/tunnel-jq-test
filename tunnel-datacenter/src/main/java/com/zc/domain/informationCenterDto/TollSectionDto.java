package com.zc.domain.informationCenterDto;

import lombok.Data;

/**
 * 收费路段字典
 */
@Data
public class TollSectionDto {

    //国标路线编码
    private String roadId;

    //国标路线名称
    private String roadName;

    //收费路段编号主键
    private String tollSectionId;

    //收费路段名称
    private String tollSectionName;
}
