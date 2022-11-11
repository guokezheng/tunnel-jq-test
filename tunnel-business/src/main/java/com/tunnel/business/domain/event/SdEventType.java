package com.tunnel.business.domain.event;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 事件类型对象 sd_event_type
 * 
 * @author gongfanfei
 * @date 2020-08-24
 */
public class SdEventType extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 事件类型ID */
    private Long id;

    /** 事件类型 */
    @Excel(name = "事件类型")
    private String eventType;

    /** 简称 */
    private String simplifyName;

    /** 父Id */
    private Long fId;


    public String getSimplifyName() {
        return simplifyName;
    }

    public void setSimplifyName(String simplifyName) {
        this.simplifyName = simplifyName;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setEventType(String eventType) 
    {
        this.eventType = eventType;
    }

    public String getEventType() 
    {
        return eventType;
    }

    public Long getfId() {
        return fId;
    }

    public void setfId(Long fId) {
        this.fId = fId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("eventType", getEventType())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}