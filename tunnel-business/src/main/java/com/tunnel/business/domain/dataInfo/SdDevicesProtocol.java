package com.tunnel.business.domain.dataInfo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 设备协议对象 sd_devices_protocol
 *
 * @author ruoyi
 * @date 2022-12-07
 */
public class SdDevicesProtocol extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    private Integer id;

    /**
     * 厂商 ID
     */
    @Excel(name = "厂商 ID")
    private String brandId;

    /**
     * 设备类型 ID
     */
    @Excel(name = "设备类型 ID")
    private String eqType;

    /**
     * 协议名称
     */
    @Excel(name = "协议名称")
    private String protocolName;

    /**
     * 协议类型
     */
    @Excel(name = "协议类型")
    private String protocolType;

    /**
     * 类名
     */
    @Excel(name = "类名")
    private String className;

    /**
     * 备注
     */
    @Excel(name = "备注")
    private String note;

    /**
     * 是否删除（1-是，0-否）
     */
    private Integer isDel;

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getBrandId() {
        return brandId;
    }

    public String getEqType() {
        return eqType;
    }

    public void setEqType(String eqType) {
        this.eqType = eqType;
    }

    public void setProtocolName(String protocolName) {
        this.protocolName = protocolName;
    }

    public String getProtocolName() {
        return protocolName;
    }

    public void setProtocolType(String protocolType) {
        this.protocolType = protocolType;
    }

    public String getProtocolType() {
        return protocolType;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassName() {
        return className;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getNote() {
        return note;
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
                .append("id", getId())
                .append("brandId", getBrandId())
                .append("eqType", getEqType())
                .append("protocolName", getProtocolName())
                .append("protocolType", getProtocolType())
                .append("className", getClassName())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("note", getNote())
                .append("idDel", getIsDel())
                .toString();
    }
}
