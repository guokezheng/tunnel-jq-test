package com.tunnel.business.domain.dataInfo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 微波车检单车实时数据对象 sd_microwave_real_data
 * 
 * @author ruoyi
 * @date 2022-12-07
 */
public class SdMicrowaveRealData extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 车道号 */
    @Excel(name = "车道号")
    private Long laneNo;

    /** 压占时间 (ms) */
    @Excel(name = "压占时间 (ms)")
    private String occupationTime;

    /** 车速 */
    @Excel(name = "车速")
    private String vehicleSpeed;

    /** 车种 */
    @Excel(name = "车种")
    private String vehicleType;

    /** 车间距 (0.1 秒) */
    @Excel(name = "车间距 (0.1 秒)")
    private String headway;

    /** 车长 (公尺) */
    @Excel(name = "车长 (公尺)")
    private String vehicleLength;

    /** 所属隧道ID */
    @Excel(name = "所属隧道ID")
    private String tunnelId;

    /** 设备ID */
    @Excel(name = "设备ID")
    private String deviceId;

    /** 方向 */
    @Excel(name = "方向")
    private String direction;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setLaneNo(Long laneNo) 
    {
        this.laneNo = laneNo;
    }

    public Long getLaneNo() 
    {
        return laneNo;
    }
    public void setOccupationTime(String occupationTime) 
    {
        this.occupationTime = occupationTime;
    }

    public String getOccupationTime() 
    {
        return occupationTime;
    }
    public void setVehicleSpeed(String vehicleSpeed) 
    {
        this.vehicleSpeed = vehicleSpeed;
    }

    public String getVehicleSpeed() 
    {
        return vehicleSpeed;
    }
    public void setVehicleType(String vehicleType) 
    {
        this.vehicleType = vehicleType;
    }

    public String getVehicleType() 
    {
        return vehicleType;
    }
    public void setHeadway(String headway)
    {
        this.headway = headway;
    }

    public String getHeadway()
    {
        return headway;
    }
    public void setVehicleLength(String vehicleLength) 
    {
        this.vehicleLength = vehicleLength;
    }

    public String getVehicleLength() 
    {
        return vehicleLength;
    }
    public void setTunnelId(String tunnelId) 
    {
        this.tunnelId = tunnelId;
    }

    public String getTunnelId() 
    {
        return tunnelId;
    }
    public void setDeviceId(String deviceId) 
    {
        this.deviceId = deviceId;
    }

    public String getDeviceId() 
    {
        return deviceId;
    }
    public void setDirection(String direction) 
    {
        this.direction = direction;
    }

    public String getDirection() 
    {
        return direction;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("laneNo", getLaneNo())
            .append("occupationTime", getOccupationTime())
            .append("vehicleSpeed", getVehicleSpeed())
            .append("vehicleType", getVehicleType())
            .append("headway", getHeadway())
            .append("vehicleLength", getVehicleLength())
            .append("createTime", getCreateTime())
            .append("tunnelId", getTunnelId())
            .append("deviceId", getDeviceId())
            .append("direction", getDirection())
            .toString();
    }
}
