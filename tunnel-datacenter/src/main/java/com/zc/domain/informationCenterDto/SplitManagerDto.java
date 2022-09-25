package com.zc.domain.informationCenterDto;

import lombok.Data;

/**
 * 运营管理单位（拆账）表
 */
public class SplitManagerDto {

    //运营管理单位编号
    private String splitManagerId;

    //运营管理单位名称
    private String splitManagerName;

    public String getSplitManagerId() {
        return splitManagerId;
    }

    public void setSplitManagerId(String splitManagerId) {
        this.splitManagerId = splitManagerId;
    }

    public String getSplitManagerName() {
        return splitManagerName;
    }

    public void setSplitManagerName(String splitManagerName) {
        this.splitManagerName = splitManagerName;
    }

    @Override
    public String toString() {
        return "SplitManagerDto{" +
                "splitManagerId='" + splitManagerId + '\'' +
                ", splitManagerName='" + splitManagerName + '\'' +
                '}';
    }
}
