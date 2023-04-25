package com.tunnel.business.domain.protocol;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 设备点位状态对象 sd_device_point_state
 *
 * @author ruoyi
 * @date 2023-04-11
 */
public class SdDevicePointState extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 设备点位ID */
    @Excel(name = "设备点位ID")
    private Long devicePointId;

    /** 设备状态ID */
    @Excel(name = "设备状态ID")
    private Long deviceStateId;

    /** 设备控制状态 */
    @Excel(name = "设备控制状态")
    private String controlState;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setDevicePointId(Long devicePointId)
    {
        this.devicePointId = devicePointId;
    }

    public Long getDevicePointId()
    {
        return devicePointId;
    }
    public void setDeviceStateId(Long deviceStateId)
    {
        this.deviceStateId = deviceStateId;
    }

    public Long getDeviceStateId()
    {
        return deviceStateId;
    }
    public void setControlState(String controlState)
    {
        this.controlState = controlState;
    }

    public String getControlState()
    {
        return controlState;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("devicePointId", getDevicePointId())
            .append("deviceStateId", getDeviceStateId())
            .append("controlState", getControlState())
            .toString();
    }
}
