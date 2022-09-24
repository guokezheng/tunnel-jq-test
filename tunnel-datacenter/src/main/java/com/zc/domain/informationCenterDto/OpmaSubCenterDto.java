package com.zc.domain.informationCenterDto;

/**
 * 信息分中心字典
 */
public class OpmaSubCenterDto {

    //路段编码
    private String opmaCliqueId;

    //路段名称
    private String opmaCliqueName;

    //分中心编码
    private String opmaXxsubcenterId;

    //分中心名称
    private String opmaXxsubcenterName;


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

    public String getOpmaXxsubcenterId() {
        return opmaXxsubcenterId;
    }

    public void setOpmaXxsubcenterId(String opmaXxsubcenterId) {
        this.opmaXxsubcenterId = opmaXxsubcenterId;
    }

    public String getOpmaXxsubcenterName() {
        return opmaXxsubcenterName;
    }

    public void setOpmaXxsubcenterName(String opmaXxsubcenterName) {
        this.opmaXxsubcenterName = opmaXxsubcenterName;
    }

    @Override
    public String toString() {
        return "OpmaSubCenterDto{" +
                "opmaCliqueId='" + opmaCliqueId + '\'' +
                ", opmaCliqueName='" + opmaCliqueName + '\'' +
                ", opmaXxsubcenterId='" + opmaXxsubcenterId + '\'' +
                ", opmaXxsubcenterName='" + opmaXxsubcenterName + '\'' +
                '}';
    }
}
