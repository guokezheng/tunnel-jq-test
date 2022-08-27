package com.zc.iot.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 产品对象 athena_iot_product
 * 
 * @author Athena-xiepufeng
 * @date 2021-10-28
 */
public class IotProduct extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /**
     * 直连设备
     */
    public static final Long DIRECT_DEVICE = 1L;

    /**
     * 网关子设备
     */
    public static final Long SUBSET_DEVICE = 2L;

    /**
     * 网关设备
     */
    public static final Long GATEWAY_DEVICE = 3L;

    /** $column.columnComment */
    private Long id;

    /** 产品名称 */
    @Excel(name = "产品名称")
    private String productName;

    /** 所属品类 */
    @Excel(name = "所属品类")
    private Long categoryKey;

    /** 节点类型 */
    @Excel(name = "节点类型")
    private Long nodeType;

    /** 连网方式 */
    @Excel(name = "连网方式")
    private Long netType;

    /** 产品描述 */
    @Excel(name = "产品描述")
    private String description;

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
    public void setCategoryKey(Long categoryKey) 
    {
        this.categoryKey = categoryKey;
    }

    public Long getCategoryKey() 
    {
        return categoryKey;
    }
    public void setNodeType(Long nodeType) 
    {
        this.nodeType = nodeType;
    }

    public Long getNodeType() 
    {
        return nodeType;
    }
    public void setNetType(Long netType) 
    {
        this.netType = netType;
    }

    public Long getNetType() 
    {
        return netType;
    }
    public void setDescription(String description) 
    {
        this.description = description;
    }

    public String getDescription() 
    {
        return description;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("productName", getProductName())
            .append("categoryKey", getCategoryKey())
            .append("nodeType", getNodeType())
            .append("netType", getNetType())
            .append("description", getDescription())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
