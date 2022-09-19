package com.tunnel.business.domain.event;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 触发器关联设备对象 sd_trigger_device
 * 
 * @author ruoyi
 * @date 2022-09-01
 */
public class SdTriggerDevice extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 触发器id */
    @Excel(name = "触发器id")
    private Long triggerId;

    /** 设备id */
    @Excel(name = "设备id")
    private String deviceId;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setTriggerId(Long triggerId) 
    {
        this.triggerId = triggerId;
    }

    public Long getTriggerId() 
    {
        return triggerId;
    }
    public void setDeviceId(String deviceId) 
    {
        this.deviceId = deviceId;
    }

    public String getDeviceId() 
    {
        return deviceId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("triggerId", getTriggerId())
            .append("deviceId", getDeviceId())
            .toString();
    }
}
