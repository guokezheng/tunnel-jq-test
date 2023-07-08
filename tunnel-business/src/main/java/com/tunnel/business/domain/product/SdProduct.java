package com.tunnel.business.domain.product;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 产品对象 sd_product
 * 
 * @author ruoyi
 * @date 2023-02-27
 */
public class SdProduct extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 产品id */
    private Long id;

    /** 产品名称 */
    @Excel(name = "产品名称")
    private String productName;

    /** 设备大类id */
    @Excel(name = "设备大类id")
    private Long fEqType;

    /** 厂商id */
    @Excel(name = "厂商id")
    private Long brandId;

    /** 协议id */
    @Excel(name = "协议id")
    private Long protocolId;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setProductName(String productName) 
    {
        this.productName = productName;
    }

    public String getProductName() 
    {
        return productName;
    }
    public void setfEqType(Long fEqType) 
    {
        this.fEqType = fEqType;
    }

    public Long getfEqType() 
    {
        return fEqType;
    }
    public void setBrandId(Long brandId) 
    {
        this.brandId = brandId;
    }

    public Long getBrandId() 
    {
        return brandId;
    }
    public void setProtocolId(Long protocolId) 
    {
        this.protocolId = protocolId;
    }

    public Long getProtocolId() 
    {
        return protocolId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("productName", getProductName())
            .append("fEqType", getfEqType())
            .append("brandId", getBrandId())
            .append("protocolId", getProtocolId())
            .toString();
    }
}
