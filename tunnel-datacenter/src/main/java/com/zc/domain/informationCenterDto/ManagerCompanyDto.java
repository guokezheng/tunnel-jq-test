package com.zc.domain.informationCenterDto;



/**
 * 查询管养单位字典
 */
public class ManagerCompanyDto {

    //集团公司编码
    private String opmaCliqueId;

    //集团公司名称
    private String opmaCliqueName;

    //管养单位编码
    private String opmaManagerId;

    //管养单位名称
    private String opmaManagerName;

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

    public String getOpmaManagerId() {
        return opmaManagerId;
    }

    public void setOpmaManagerId(String opmaManagerId) {
        this.opmaManagerId = opmaManagerId;
    }

    public String getOpmaManagerName() {
        return opmaManagerName;
    }

    public void setOpmaManagerName(String opmaManagerName) {
        this.opmaManagerName = opmaManagerName;
    }

    @Override
    public String toString() {
        return "ManagerCompanyDto{" +
                "opmaCliqueId='" + opmaCliqueId + '\'' +
                ", opmaCliqueName='" + opmaCliqueName + '\'' +
                ", opmaManagerId='" + opmaManagerId + '\'' +
                ", opmaManagerName='" + opmaManagerName + '\'' +
                '}';
    }
}
