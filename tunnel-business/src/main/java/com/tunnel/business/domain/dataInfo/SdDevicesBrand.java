package com.tunnel.business.domain.dataInfo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 物联设备厂商对象 sd_devices_brand
 *
 * @author ruoyi
 * @date 2022-12-02
 */
public class SdDevicesBrand extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 设备厂商编号
     */
    private String supplierId;

    /**
     * 设备厂商名称
     */
    @Excel(name = "设备厂商名称")
    private String supplierName;

    /**
     * 简称
     */
    @Excel(name = "简称")
    private String shortName;

    /**
     * 是否删除（1-是，0-否）
     */
    private Integer isDel;

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getShortName() {
        return shortName;
    }

    public Integer getIsDel() {
        return isDel;
    }

    public void setIsDel(Integer isDel) {
        this.isDel = isDel;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("supplierId", getSupplierId())
                .append("supplierName", getSupplierName())
                .append("shortName", getShortName())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
