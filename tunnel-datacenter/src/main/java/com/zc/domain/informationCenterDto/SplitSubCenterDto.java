package com.zc.domain.informationCenterDto;



/**
 * 查询分中心（拆账）
 */
public class SplitSubCenterDto {

    //路段编码
    private String splitSectionId;

    //路段名称
    private String splitSectionName;

    //分中心编码
    private String splitSubcenterId;

    //分中心名称
    private String splitSubcenterName;

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

    public String getSplitSubcenterId() {
        return splitSubcenterId;
    }

    public void setSplitSubcenterId(String splitSubcenterId) {
        this.splitSubcenterId = splitSubcenterId;
    }

    public String getSplitSubcenterName() {
        return splitSubcenterName;
    }

    public void setSplitSubcenterName(String splitSubcenterName) {
        this.splitSubcenterName = splitSubcenterName;
    }

    @Override
    public String toString() {
        return "SplitSubCenterDto{" +
                "splitSectionId='" + splitSectionId + '\'' +
                ", splitSectionName='" + splitSectionName + '\'' +
                ", splitSubcenterId='" + splitSubcenterId + '\'' +
                ", splitSubcenterName='" + splitSubcenterName + '\'' +
                '}';
    }
}
