package com.zc.domain.informationCenterDto;



/**
 * 查询管辖路段（拆账）
 */
public class SplitSectionDto {

    //管理单位
    private String splitManagerId;

    //管理单位名称
    private String splitManagerName;

    //路段编码
    private String splitSectionId;

    //路段名称
    private String splitSectionName;

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

    public String getSplitSectionId() {
        return splitSectionId;
    }

    public void setSplitSectionId(String splitSectionId) {
        this.splitSectionId = splitSectionId;
    }

    public String getSplitSectionName() {
        return splitSectionName;
    }

    public void setSplitSectionName(String splitSectionName) {
        this.splitSectionName = splitSectionName;
    }

    @Override
    public String toString() {
        return "SplitSectionDto{" +
                "splitManagerId='" + splitManagerId + '\'' +
                ", splitManagerName='" + splitManagerName + '\'' +
                ", splitSectionId='" + splitSectionId + '\'' +
                ", splitSectionName='" + splitSectionName + '\'' +
                '}';
    }
}
