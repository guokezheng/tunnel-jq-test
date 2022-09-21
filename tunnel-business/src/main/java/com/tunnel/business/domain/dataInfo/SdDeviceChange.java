package com.tunnel.business.domain.dataInfo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 设备变更对象 sd_device_change
 * 
 * @author 刘方堃
 * @date 2021-12-09
 */
@ApiModel("设备变更对象类")
public class SdDeviceChange extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    @ApiModelProperty("$column.columnComment")
    private Long id;

    /** 设备编号 */
    @ApiModelProperty("设备编号")
    @Excel(name = "设备编号")
    private String deviceId;

    /** 设备名称 */
    @ApiModelProperty("设备名称")
    @Excel(name = "设备名称")
    private String deviceName;

    /** 更换时间 */
    @ApiModelProperty("更换时间")
    @Excel(name = "更换时间")
    private String changeTime;

    /** 桩号 */
    @Excel(name = "桩号")
    @ApiModelProperty("桩号")
    private String eqDirection;

    /** 方向 */
    @ApiModelProperty("方向")
    @Excel(name = "方向")
    private String stakeMark;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setDeviceId(String deviceId) 
    {
        this.deviceId = deviceId;
    }

    public String getDeviceId() 
    {
        return deviceId;
    }
    public void setDeviceName(String deviceName) 
    {
        this.deviceName = deviceName;
    }

    public String getDeviceName() 
    {
        return deviceName;
    }
    public void setChangeTime(String changeTime) 
    {
        this.changeTime = changeTime;
    }

    public String getChangeTime() 
    {
        return changeTime;
    }
    public void setEqDirection(String eqDirection) 
    {
        this.eqDirection = eqDirection;
    }

    public String getEqDirection() 
    {
        return eqDirection;
    }
    public void setStakeMark(String stakeMark) 
    {
        this.stakeMark = stakeMark;
    }

    public String getStakeMark() 
    {
        return stakeMark;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("deviceId", getDeviceId())
            .append("deviceName", getDeviceName())
            .append("changeTime", getChangeTime())
            .append("eqDirection", getEqDirection())
            .append("stakeMark", getStakeMark())
            .append("remark", getRemark())
            .toString();
    }
}
