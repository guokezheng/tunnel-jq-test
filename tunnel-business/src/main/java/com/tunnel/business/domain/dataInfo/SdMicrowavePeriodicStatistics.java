package com.tunnel.business.domain.dataInfo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 车流量信息对象 sd_microwave_periodic_statistics
 * 
 * @author ruoyi
 * @date 2022-12-07
 */
public class SdMicrowavePeriodicStatistics extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long statisticsId;

    /** 所属隧道 */
    @Excel(name = "所属隧道")
    private String tunnelId;

    /** 设备编号 */
    @Excel(name = "设备编号")
    private String deviceId;

    /** 对应车道号  */
    @Excel(name = "对应车道号 ")
    private Long laneNo;

    /** 车道过车平均速度  */
    @Excel(name = "车道过车平均速度 ")
    private String avgSpeed;

    /** 小型车数量  */
    @Excel(name = "小型车数量 ")
    private String smallVehicleNum;

    /** 中型车数量  */
    @Excel(name = "中型车数量 ")
    private String midVehicleNum;

    /** 大型车数量 */
    @Excel(name = "大型车数量")
    private String heavyVehicleNum;

    /** 平均车长（0.1m） */
    @Excel(name = "平均车长", readConverterExp = "0=.1m")
    private String avgLength;

    /** 平均车间距（0.1m） */
    @Excel(name = "平均车间距", readConverterExp = "0=.1m")
    private String avgHeadway;

    /** 平均压占率（0.1%） */
    @Excel(name = "平均压占率", readConverterExp = "0=.1%")
    private String avgOccupancy;

    /** 上传标识：0-表示T1时间的统计结果，1-表示T2时间的统计  */
    @Excel(name = "上传标识：0-表示T1时间的统计结果，1-表示T2时间的统计 ")
    private String flag;

    /** 车流量（总数） */
    @Excel(name = "车流量", readConverterExp = "总=数")
    private String trafficFlowTotal;

    /** 方向 */
    @Excel(name = "方向")
    private String eqDirection;

    /** 小型车车速 */
    @Excel(name = "小型车车速")
    private String smallVehicleSpeed;

    /** 中型车车速 */
    @Excel(name = "中型车车速")
    private String midVehicleSpeed;

    /** 大型车车速 */
    @Excel(name = "大型车车速")
    private String heavyVehicleSpeed;

    public void setStatisticsId(Long statisticsId) 
    {
        this.statisticsId = statisticsId;
    }

    public Long getStatisticsId() 
    {
        return statisticsId;
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
    public void setLaneNo(Long laneNo)
    {
        this.laneNo = laneNo;
    }

    public Long getLaneNo()
    {
        return laneNo;
    }
    public void setAvgSpeed(String avgSpeed) 
    {
        this.avgSpeed = avgSpeed;
    }

    public String getAvgSpeed() 
    {
        return avgSpeed;
    }
    public void setSmallVehicleNum(String smallVehicleNum) 
    {
        this.smallVehicleNum = smallVehicleNum;
    }

    public String getSmallVehicleNum() 
    {
        return smallVehicleNum;
    }
    public void setMidVehicleNum(String midVehicleNum) 
    {
        this.midVehicleNum = midVehicleNum;
    }

    public String getMidVehicleNum() 
    {
        return midVehicleNum;
    }
    public void setHeavyVehicleNum(String heavyVehicleNum) 
    {
        this.heavyVehicleNum = heavyVehicleNum;
    }

    public String getHeavyVehicleNum() 
    {
        return heavyVehicleNum;
    }
    public void setAvgLength(String avgLength) 
    {
        this.avgLength = avgLength;
    }

    public String getAvgLength() 
    {
        return avgLength;
    }
    public void setAvgHeadway(String avgHeadway) 
    {
        this.avgHeadway = avgHeadway;
    }

    public String getAvgHeadway() 
    {
        return avgHeadway;
    }
    public void setAvgOccupancy(String avgOccupancy) 
    {
        this.avgOccupancy = avgOccupancy;
    }

    public String getAvgOccupancy() 
    {
        return avgOccupancy;
    }
    public void setFlag(String flag) 
    {
        this.flag = flag;
    }

    public String getFlag() 
    {
        return flag;
    }
    public void setTrafficFlowTotal(String trafficFlowTotal) 
    {
        this.trafficFlowTotal = trafficFlowTotal;
    }

    public String getTrafficFlowTotal() 
    {
        return trafficFlowTotal;
    }
    public void setEqDirection(String eqDirection) 
    {
        this.eqDirection = eqDirection;
    }

    public String getEqDirection() 
    {
        return eqDirection;
    }
    public void setSmallVehicleSpeed(String smallVehicleSpeed) 
    {
        this.smallVehicleSpeed = smallVehicleSpeed;
    }

    public String getSmallVehicleSpeed() 
    {
        return smallVehicleSpeed;
    }
    public void setMidVehicleSpeed(String midVehicleSpeed) 
    {
        this.midVehicleSpeed = midVehicleSpeed;
    }

    public String getMidVehicleSpeed() 
    {
        return midVehicleSpeed;
    }
    public void setHeavyVehicleSpeed(String heavyVehicleSpeed) 
    {
        this.heavyVehicleSpeed = heavyVehicleSpeed;
    }

    public String getHeavyVehicleSpeed() 
    {
        return heavyVehicleSpeed;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("statisticsId", getStatisticsId())
            .append("tunnelId", getTunnelId())
            .append("deviceId", getDeviceId())
            .append("laneNo", getLaneNo())
            .append("avgSpeed", getAvgSpeed())
            .append("smallVehicleNum", getSmallVehicleNum())
            .append("midVehicleNum", getMidVehicleNum())
            .append("heavyVehicleNum", getHeavyVehicleNum())
            .append("avgLength", getAvgLength())
            .append("avgHeadway", getAvgHeadway())
            .append("avgOccupancy", getAvgOccupancy())
            .append("flag", getFlag())
            .append("trafficFlowTotal", getTrafficFlowTotal())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("eqDirection", getEqDirection())
            .append("smallVehicleSpeed", getSmallVehicleSpeed())
            .append("midVehicleSpeed", getMidVehicleSpeed())
            .append("heavyVehicleSpeed", getHeavyVehicleSpeed())
            .toString();
    }
}
