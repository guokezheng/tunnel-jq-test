package com.zc.domain.informationCenterDto;

import lombok.Data;

/**
 * 查询集团公司
 */
public class OpmaCliqueDto {

    //集团公司编码
    private String opmaCliqueId;

    //集团公司名称
    private String opmaCliqueName;

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

    @Override
    public String toString() {
        return "OpmaCliqueDto{" +
                "opmaCliqueId='" + opmaCliqueId + '\'' +
                ", opmaCliqueName='" + opmaCliqueName + '\'' +
                '}';
    }
}
