package com.tunnel.business.domain.dataInfo;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excels;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 车流量信息对象 sd_traffic_statistics
 * 
 * @author yanghousheng
 * @date 2020-11-10
 */
public class SdTrafficStatistics extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long statisticsId;
    /** 所属隧道 id */
    @Excel(name = "所属隧道 id")
    private String tunnelId;
    
    /** tunnel对象 */
    @Excels({
        @Excel(name = "tunnel", targetAttr = "tunnelName"),
    })
    private SdTunnels tunnelName;
    
    public SdTunnels getTunnelName() {
		return tunnelName;
	}

	public void setTunnelName(SdTunnels tunnelName) {
		this.tunnelName = tunnelName;
	}

	/** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String deviceId;
    
    /** 方向 **/
    private String eqDirection;
    
    public String getEqDirection() {
		return eqDirection;
	}

	public void setEqDirection(String eqDirection) {
		this.eqDirection = eqDirection;
	}
	
	/** 左右洞 **/
	private String holes;
	
	public String getHoles() {
		return holes;
	}

	public void setHoles(String holes) {
		this.holes = holes;
	}

	/** 对应车道号  */
    @Excel(name = "对应车道号 ")
    private Long byLane;

    /** 车道过车平均速度  */
    @Excel(name = "车道过车平均速度 ")
    private Long bySpeed;

    /** 到达速度 */
    @Excel(name = "到达速度")
    private Long wArrivalFlow;

    /** 小型车数量  */
    @Excel(name = "小型车数量 ")
    private Long dwLightVehicle;

    /** 中型车数量  */
    @Excel(name = "中型车数量 ")
    private Long dwMidVehicle;

    /** 重型车数量 */
    @Excel(name = "重型车数量")
    private Long dwHeavyVehicle;

    /** 车头时距，以秒计算  */
    @Excel(name = "车头时距，以秒计算 ")
    private Long dwTimeHeadway;

    /** 车头间距，以米来计算  */
    @Excel(name = "车头间距，以米来计算 ")
    private Long dwSpaceHeadway;

    /** 空间占有率，百分比计算  */
    @Excel(name = "空间占有率，百分比计算 ")
    private Long fSpaceOccupyRation;

    /** 时间占有率，百分比计算  */
    @Excel(name = "时间占有率，百分比计算 ")
    private Long fTimeOccupyRation;

    /** 平均停车次数 */
    @Excel(name = "平均停车次数")
    private Long byStoppingTimes;

    /** 堵塞状态下排队长度（比如50米）  */
    @Excel(name = "堵塞状态下排队长度", readConverterExp = "比=如50米")
    private Long byQueueLen;

    /** 上传标识：0-表示T1时间的统计结果，1-表示T2时间的统计  */
    @Excel(name = "上传标识：0-表示T1时间的统计结果，1-表示T2时间的统计 ")
    private Long byFlag;

    /** 区域车辆数  */
    @Excel(name = "区域车辆数 ")
    private Long byVehicelNum;

    /** 平均延误  */
    @Excel(name = "平均延误 ")
    private Long wDelay;

    /** 非机动车数量  */
    @Excel(name = "非机动车数量 ")
    private Long dwNonMotor;
    
    
    private String deviceName;
    
    private String date;
    
    public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public void setStatisticsId(Long statisticsId) 
    {
        this.statisticsId = statisticsId;
    }

    public Long getStatisticsId() 
    {
        return statisticsId;
    }

	public String getTunnelId() {
		return tunnelId;
	}

	public void setTunnelId(String tunnelId) {
		this.tunnelId = tunnelId;
	}

	public void setDeviceId(String deviceId) 
    {
        this.deviceId = deviceId;
    }

    public String getDeviceId() 
    {
        return deviceId;
    }
    public void setByLane(Long byLane) 
    {
        this.byLane = byLane;
    }

    public Long getByLane() 
    {
        return byLane;
    }
    public void setBySpeed(Long bySpeed) 
    {
        this.bySpeed = bySpeed;
    }

    public Long getBySpeed() 
    {
        return bySpeed;
    }
    public void setwArrivalFlow(Long wArrivalFlow) 
    {
        this.wArrivalFlow = wArrivalFlow;
    }

    public Long getwArrivalFlow() 
    {
        return wArrivalFlow;
    }
    public void setDwLightVehicle(Long dwLightVehicle) 
    {
        this.dwLightVehicle = dwLightVehicle;
    }

    public Long getDwLightVehicle() 
    {
        return dwLightVehicle;
    }
    public void setDwMidVehicle(Long dwMidVehicle) 
    {
        this.dwMidVehicle = dwMidVehicle;
    }

    public Long getDwMidVehicle() 
    {
        return dwMidVehicle;
    }
    public void setDwHeavyVehicle(Long dwHeavyVehicle) 
    {
        this.dwHeavyVehicle = dwHeavyVehicle;
    }

    public Long getDwHeavyVehicle() 
    {
        return dwHeavyVehicle;
    }
    public void setDwTimeHeadway(Long dwTimeHeadway) 
    {
        this.dwTimeHeadway = dwTimeHeadway;
    }

    public Long getDwTimeHeadway() 
    {
        return dwTimeHeadway;
    }
    public void setDwSpaceHeadway(Long dwSpaceHeadway) 
    {
        this.dwSpaceHeadway = dwSpaceHeadway;
    }

    public Long getDwSpaceHeadway() 
    {
        return dwSpaceHeadway;
    }
    public void setfSpaceOccupyRation(Long fSpaceOccupyRation) 
    {
        this.fSpaceOccupyRation = fSpaceOccupyRation;
    }

    public Long getfSpaceOccupyRation() 
    {
        return fSpaceOccupyRation;
    }
    public void setfTimeOccupyRation(Long fTimeOccupyRation) 
    {
        this.fTimeOccupyRation = fTimeOccupyRation;
    }

    public Long getfTimeOccupyRation() 
    {
        return fTimeOccupyRation;
    }
    public void setByStoppingTimes(Long byStoppingTimes) 
    {
        this.byStoppingTimes = byStoppingTimes;
    }

    public Long getByStoppingTimes() 
    {
        return byStoppingTimes;
    }
    public void setByQueueLen(Long byQueueLen) 
    {
        this.byQueueLen = byQueueLen;
    }

    public Long getByQueueLen() 
    {
        return byQueueLen;
    }
    public void setByFlag(Long byFlag) 
    {
        this.byFlag = byFlag;
    }

    public Long getByFlag() 
    {
        return byFlag;
    }
    public void setByVehicelNum(Long byVehicelNum) 
    {
        this.byVehicelNum = byVehicelNum;
    }

    public Long getByVehicelNum() 
    {
        return byVehicelNum;
    }
    public void setwDelay(Long wDelay) 
    {
        this.wDelay = wDelay;
    }

    public Long getwDelay() 
    {
        return wDelay;
    }
    public void setDwNonMotor(Long dwNonMotor) 
    {
        this.dwNonMotor = dwNonMotor;
    }

    public Long getDwNonMotor() 
    {
        return dwNonMotor;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("statisticsId", getStatisticsId())
            .append("eqTunnelId",getTunnelId())
            .append("tunnelName",getTunnelName())
            .append("deviceId", getDeviceId())
            .append("byLane", getByLane())
            .append("bySpeed", getBySpeed())
            .append("wArrivalFlow", getwArrivalFlow())
            .append("dwLightVehicle", getDwLightVehicle())
            .append("dwMidVehicle", getDwMidVehicle())
            .append("dwHeavyVehicle", getDwHeavyVehicle())
            .append("dwTimeHeadway", getDwTimeHeadway())
            .append("dwSpaceHeadway", getDwSpaceHeadway())
            .append("fSpaceOccupyRation", getfSpaceOccupyRation())
            .append("fTimeOccupyRation", getfTimeOccupyRation())
            .append("byStoppingTimes", getByStoppingTimes())
            .append("byQueueLen", getByQueueLen())
            .append("byFlag", getByFlag())
            .append("byVehicelNum", getByVehicelNum())
            .append("wDelay", getwDelay())
            .append("dwNonMotor", getDwNonMotor())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("eqDirection", getEqDirection())
            .append("deviceName", getDeviceName())
            .append("date",getDate())
            .append("holes",getHoles())
            .toString();
    }
}
