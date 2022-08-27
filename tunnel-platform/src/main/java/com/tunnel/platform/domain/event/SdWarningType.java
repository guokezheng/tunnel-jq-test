package com.tunnel.platform.domain.event;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

/**
 * 预警类型对象 sd_warning_type
 * 
 * @author gongfanfei
 * @date 2020-12-21
 */
public class SdWarningType extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 预警类型 */
    @Excel(name = "预警类型")
    private String typeName;

    /** 判断符 0：大于 1 小于 2 等于 */
    @Excel(name = "判断符 0：大于 1 小于 2 等于")
    private String judge;

    /** 阈值 */
    @Excel(name = "阈值")
    private BigDecimal threshold;

    /** 阈值描述 */
    @Excel(name = "阈值描述")
    private String description;

    /** 相关预案 */
    private String reservePlanIds;
    
    /** 预案名称 **/
    private String reservePlanInfo;

    /** 来源类型 0：传感器 1：视频事件  2其他 */
    private String sourceType;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setTypeName(String typeName) 
    {
        this.typeName = typeName;
    }

    public String getTypeName() 
    {
        return typeName;
    }
    public void setJudge(String judge) 
    {
        this.judge = judge;
    }

    public String getJudge() 
    {
        return judge;
    }
    public void setThreshold(BigDecimal threshold) 
    {
        this.threshold = threshold;
    }

    public BigDecimal getThreshold() 
    {
        return threshold;
    }
    public void setDescription(String description) 
    {
        this.description = description;
    }

    public String getDescription() 
    {
        return description;
    }
    public void setReservePlanIds(String reservePlanIds) 
    {
        this.reservePlanIds = reservePlanIds;
    }

    public String getReservePlanIds() 
    {
        return reservePlanIds;
    }
    
    public String getReservePlanInfo() {
		return reservePlanInfo;
	}

	public void setReservePlanInfo(String reservePlanInfo) {
		this.reservePlanInfo = reservePlanInfo;
	}

	public void setSourceType(String sourceType) 
    {
        this.sourceType = sourceType;
    }

    public String getSourceType() 
    {
        return sourceType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("typeName", getTypeName())
            .append("judge", getJudge())
            .append("threshold", getThreshold())
            .append("description", getDescription())
            .append("reservePlanIds", getReservePlanIds())
            .append("reservePlanInfo", getReservePlanInfo())
            .append("sourceType", getSourceType())
            .toString();
    }
}