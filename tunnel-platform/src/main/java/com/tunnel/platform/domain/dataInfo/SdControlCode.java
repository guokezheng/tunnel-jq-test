package com.tunnel.platform.domain.dataInfo;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 控制码对象 sd_control_code
 * 
 * @author zhangweitian
 * @date 2020-09-08
 */
public class SdControlCode extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long controlId;

    /** $column.columnComment */
    @Excel(name = "key")
    private String controlKey;

    /** $column.columnComment */
    @Excel(name = "value")
    private String controlValue;
    
    /** 备注 */
    @Excel(name = "备注")
    private String remark;

    public void setControlId(Long controlId) 
    {
        this.controlId = controlId;
    }

	public Long getControlId() 
    {
        return controlId;
    }
    public void setControlKey(String controlKey) 
    {
        this.controlKey = controlKey;
    }

    public String getControlKey() 
    {
        return controlKey;
    }
    public void setControlValue(String controlValue) 
    {
        this.controlValue = controlValue;
    }

    public String getControlValue() 
    {
        return controlValue;
    }
    
    public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("controlId", getControlId())
            .append("controlKey", getControlKey())
            .append("controlValue", getControlValue())
            .append("remark", getRemark())
            .toString();
    }
}