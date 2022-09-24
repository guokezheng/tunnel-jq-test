package com.zc.domain.informationCenterDto;

import lombok.Data;

/**
 * 查询管养公司编码信息
 */
public class OpmaManagerDto {

    //集团公司编码
    private String opmaCliqueId;

    //集团公司名称
    private String opmaCliqueName;

    //管养单位编码
    private String opmaManagerCropId;

    //管养单位名称
    private String opmaManagerCropName;

    public String getOpmaCliqueId() {
        return opmaCliqueId;
    }

    public void setOpmaCliqueId(String opmaCliqueId) {
        this.opmaCliqueId = opmaCliqueId;
    }

    public String getOpmaCliqueName() {
        return opmaCliqueName;
    }

    public void setOpmaCliqueName(String opmaCliqueName) {
        this.opmaCliqueName = opmaCliqueName;
    }

    public String getOpmaManagerCropId() {
        return opmaManagerCropId;
    }

    public void setOpmaManagerCropId(String opmaManagerCropId) {
        this.opmaManagerCropId = opmaManagerCropId;
    }

    public String getOpmaManagerCropName() {
        return opmaManagerCropName;
    }

    public void setOpmaManagerCropName(String opmaManagerCropName) {
        this.opmaManagerCropName = opmaManagerCropName;
    }

    @Override
    public String toString() {
        return "OpmaManagerDto{" +
                "opmaCliqueId='" + opmaCliqueId + '\'' +
                ", opmaCliqueName='" + opmaCliqueName + '\'' +
                ", opmaManagerCropId='" + opmaManagerCropId + '\'' +
                ", opmaManagerCropName='" + opmaManagerCropName + '\'' +
                '}';
    }
}
