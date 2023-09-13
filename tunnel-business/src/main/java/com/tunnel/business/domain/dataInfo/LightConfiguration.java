package com.tunnel.business.domain.dataInfo;

import io.swagger.annotations.ApiModel;

@ApiModel("全部照明设备对象")
public class LightConfiguration {

    /** 主键 */
    private Long id;
    private String eqId;
    private String externalId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEqId() {
        return eqId;
    }

    public void setEqId(String eqId) {
        this.eqId = eqId;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }
}
