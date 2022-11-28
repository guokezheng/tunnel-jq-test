package com.tunnel.business.domain.dataInfo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 设备品牌
 */
@ApiModel("设备品牌对象类")
public class SdDevicesBrand {
    @ApiModelProperty("设备厂商ID")
    private String supplierId;

    @ApiModelProperty("设备厂商名称")
    private String supplierName;

    @ApiModelProperty("简称")
    private String shortName;

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }


    @Override
    public String toString() {
        return "SdDevicesBrand{" +
                "supplierId='" + supplierId + '\'' +
                ", supplierName='" + supplierName + '\'' +
                ", shortName='" + shortName + '\'' +
                '}';
    }
}
