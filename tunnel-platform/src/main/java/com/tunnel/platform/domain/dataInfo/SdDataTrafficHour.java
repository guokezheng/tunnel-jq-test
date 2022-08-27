package com.tunnel.platform.domain.dataInfo;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 各路段小时车流量统计对象 sd_data_traffic_hour
 *
 * @author ruoyi
 * @date 2022-02-18
 */
public class SdDataTrafficHour extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 数据统计时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "数据统计时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date statTime;

    /** 路段编码 */
    @Excel(name = "路段编码")
    private String segmentNo;

    /** 年 */
    @Excel(name = "年")
    private Long year;

    /** 月 */
    @Excel(name = "月")
    private Long month;

    /** 日 */
    @Excel(name = "日")
    private Long day;

    /** 小时 */
    @Excel(name = "小时")
    private Long hour;

    /** 总车流量 */
    @Excel(name = "总车流量")
    private Long totalFlow;

    /** 小型车车流量 */
    @Excel(name = "小型车车流量")
    private Long flowSveh;

    /** 中型车车流量 */
    @Excel(name = "中型车车流量")
    private Long flowMveh;

    /** 大型车车流量 */
    @Excel(name = "大型车车流量")
    private Long flowBveh;

    /** 平均速度 */
    @Excel(name = "平均速度")
    private String avgSpeed;

    /** 客车车流量 */
    @Excel(name = "客车车流量")
    private Long flowCar;

    /** 货车车流量 */
    @Excel(name = "货车车流量")
    private Long flowTruck;

    /** 小车车流量 */
    @Excel(name = "小车车流量")
    private Long flowXveh;

    /** 大车车流量 */
    @Excel(name = "大车车流量")
    private Long flowDveh;

    /** 客一流量 */
    @Excel(name = "客一流量")
    private Long bus1;

    /** 客二流量 */
    @Excel(name = "客二流量")
    private Long bus2;

    /** 客三流量 */
    @Excel(name = "客三流量")
    private Long bus3;

    /** 客四流量 */
    @Excel(name = "客四流量")
    private Long bus4;

    /** 货一流量 */
    @Excel(name = "货一流量")
    private Long truck1;

    /** 货二流量 */
    @Excel(name = "货二流量")
    private Long truck2;

    /** 货三流量 */
    @Excel(name = "货三流量")
    private Long truck3;

    /** 货四流量 */
    @Excel(name = "货四流量")
    private Long truck4;

    /** 货五流量 */
    @Excel(name = "货五流量")
    private Long truck5;

    /** 货六流量 */
    @Excel(name = "货六流量")
    private Long truck6;

    /** 专一流量 */
    @Excel(name = "专一流量")
    private Long special1;

    /** 专二流量 */
    @Excel(name = "专二流量")
    private Long special2;

    /** 专三流量 */
    @Excel(name = "专三流量")
    private Long special3;

    /** 专四流量 */
    @Excel(name = "专四流量")
    private Long special4;

    /** 专五流量 */
    @Excel(name = "专五流量")
    private Long special5;

    /** 专六流量 */
    @Excel(name = "专六流量")
    private Long special6;

    /** 占比 */
    @Excel(name = "占比")
    private BigDecimal bus1Ratio;

    /** 占比 */
    @Excel(name = "占比")
    private BigDecimal bus2Ratio;

    /** 占比 */
    @Excel(name = "占比")
    private BigDecimal bus3Ratio;

    /** 占比 */
    @Excel(name = "占比")
    private BigDecimal bus4Ratio;

    /** 占比 */
    @Excel(name = "占比")
    private BigDecimal truck1Ratio;

    /** 占比 */
    @Excel(name = "占比")
    private BigDecimal truck2Ratio;

    /** 占比 */
    @Excel(name = "占比")
    private BigDecimal truck3Ratio;

    /** 占比 */
    @Excel(name = "占比")
    private BigDecimal truck4Ratio;

    /** 占比 */
    @Excel(name = "占比")
    private BigDecimal truck5Ratio;

    /** 占比 */
    @Excel(name = "占比")
    private BigDecimal truck6Ratio;

    /** 占比 */
    @Excel(name = "占比")
    private BigDecimal special1Ratio;

    /** 占比 */
    @Excel(name = "占比")
    private BigDecimal special2Ratio;

    /** 占比 */
    @Excel(name = "占比")
    private BigDecimal special3Ratio;

    /** 占比 */
    @Excel(name = "占比")
    private BigDecimal special4Ratio;

    /** 占比 */
    @Excel(name = "占比")
    private BigDecimal special5Ratio;

    /** 占比 */
    @Excel(name = "占比")
    private BigDecimal special6Ratio;

    private String tunnelId;

    private String tunnelName;

    private String deviceId;

    private Long eqType;

    private String direction;

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Long getEqType() {
        return eqType;
    }

    public void setEqType(Long eqType) {
        this.eqType = eqType;
    }

    public String getTunnelId() {
        return tunnelId;
    }

    public void setTunnelId(String tunnelId) {
        this.tunnelId = tunnelId;
    }

    public String getTunnelName() {
        return tunnelName;
    }

    public void setTunnelName(String tunnelName) {
        this.tunnelName = tunnelName;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setStatTime(Date statTime)
    {
        this.statTime = statTime;
    }

    public Date getStatTime()
    {
        return statTime;
    }
    public void setSegmentNo(String segmentNo)
    {
        this.segmentNo = segmentNo;
    }

    public String getSegmentNo()
    {
        return segmentNo;
    }
    public void setYear(Long year)
    {
        this.year = year;
    }

    public Long getYear()
    {
        return year;
    }
    public void setMonth(Long month)
    {
        this.month = month;
    }

    public Long getMonth()
    {
        return month;
    }
    public void setDay(Long day)
    {
        this.day = day;
    }

    public Long getDay()
    {
        return day;
    }
    public void setHour(Long hour)
    {
        this.hour = hour;
    }

    public Long getHour()
    {
        return hour;
    }
    public void setTotalFlow(Long totalFlow)
    {
        this.totalFlow = totalFlow;
    }

    public Long getTotalFlow()
    {
        return totalFlow;
    }
    public void setFlowSveh(Long flowSveh)
    {
        this.flowSveh = flowSveh;
    }

    public Long getFlowSveh()
    {
        return flowSveh;
    }
    public void setFlowMveh(Long flowMveh)
    {
        this.flowMveh = flowMveh;
    }

    public Long getFlowMveh()
    {
        return flowMveh;
    }
    public void setFlowBveh(Long flowBveh)
    {
        this.flowBveh = flowBveh;
    }

    public Long getFlowBveh()
    {
        return flowBveh;
    }
    public void setAvgSpeed(String avgSpeed)
    {
        this.avgSpeed = avgSpeed;
    }

    public String getAvgSpeed()
    {
        return avgSpeed;
    }
    public void setFlowCar(Long flowCar)
    {
        this.flowCar = flowCar;
    }

    public Long getFlowCar()
    {
        return flowCar;
    }
    public void setFlowTruck(Long flowTruck)
    {
        this.flowTruck = flowTruck;
    }

    public Long getFlowTruck()
    {
        return flowTruck;
    }
    public void setFlowXveh(Long flowXveh)
    {
        this.flowXveh = flowXveh;
    }

    public Long getFlowXveh()
    {
        return flowXveh;
    }
    public void setFlowDveh(Long flowDveh)
    {
        this.flowDveh = flowDveh;
    }

    public Long getFlowDveh()
    {
        return flowDveh;
    }
    public void setBus1(Long bus1)
    {
        this.bus1 = bus1;
    }

    public Long getBus1()
    {
        return bus1;
    }
    public void setBus2(Long bus2)
    {
        this.bus2 = bus2;
    }

    public Long getBus2()
    {
        return bus2;
    }
    public void setBus3(Long bus3)
    {
        this.bus3 = bus3;
    }

    public Long getBus3()
    {
        return bus3;
    }
    public void setBus4(Long bus4)
    {
        this.bus4 = bus4;
    }

    public Long getBus4()
    {
        return bus4;
    }
    public void setTruck1(Long truck1)
    {
        this.truck1 = truck1;
    }

    public Long getTruck1()
    {
        return truck1;
    }
    public void setTruck2(Long truck2)
    {
        this.truck2 = truck2;
    }

    public Long getTruck2()
    {
        return truck2;
    }
    public void setTruck3(Long truck3)
    {
        this.truck3 = truck3;
    }

    public Long getTruck3()
    {
        return truck3;
    }
    public void setTruck4(Long truck4)
    {
        this.truck4 = truck4;
    }

    public Long getTruck4()
    {
        return truck4;
    }
    public void setTruck5(Long truck5)
    {
        this.truck5 = truck5;
    }

    public Long getTruck5()
    {
        return truck5;
    }
    public void setTruck6(Long truck6)
    {
        this.truck6 = truck6;
    }

    public Long getTruck6()
    {
        return truck6;
    }
    public void setSpecial1(Long special1)
    {
        this.special1 = special1;
    }

    public Long getSpecial1()
    {
        return special1;
    }
    public void setSpecial2(Long special2)
    {
        this.special2 = special2;
    }

    public Long getSpecial2()
    {
        return special2;
    }
    public void setSpecial3(Long special3)
    {
        this.special3 = special3;
    }

    public Long getSpecial3()
    {
        return special3;
    }
    public void setSpecial4(Long special4)
    {
        this.special4 = special4;
    }

    public Long getSpecial4()
    {
        return special4;
    }
    public void setSpecial5(Long special5)
    {
        this.special5 = special5;
    }

    public Long getSpecial5()
    {
        return special5;
    }
    public void setSpecial6(Long special6)
    {
        this.special6 = special6;
    }

    public Long getSpecial6()
    {
        return special6;
    }
    public void setBus1Ratio(BigDecimal bus1Ratio)
    {
        this.bus1Ratio = bus1Ratio;
    }

    public BigDecimal getBus1Ratio()
    {
        return bus1Ratio;
    }
    public void setBus2Ratio(BigDecimal bus2Ratio)
    {
        this.bus2Ratio = bus2Ratio;
    }

    public BigDecimal getBus2Ratio()
    {
        return bus2Ratio;
    }
    public void setBus3Ratio(BigDecimal bus3Ratio)
    {
        this.bus3Ratio = bus3Ratio;
    }

    public BigDecimal getBus3Ratio()
    {
        return bus3Ratio;
    }
    public void setBus4Ratio(BigDecimal bus4Ratio)
    {
        this.bus4Ratio = bus4Ratio;
    }

    public BigDecimal getBus4Ratio()
    {
        return bus4Ratio;
    }
    public void setTruck1Ratio(BigDecimal truck1Ratio)
    {
        this.truck1Ratio = truck1Ratio;
    }

    public BigDecimal getTruck1Ratio()
    {
        return truck1Ratio;
    }
    public void setTruck2Ratio(BigDecimal truck2Ratio)
    {
        this.truck2Ratio = truck2Ratio;
    }

    public BigDecimal getTruck2Ratio()
    {
        return truck2Ratio;
    }
    public void setTruck3Ratio(BigDecimal truck3Ratio)
    {
        this.truck3Ratio = truck3Ratio;
    }

    public BigDecimal getTruck3Ratio()
    {
        return truck3Ratio;
    }
    public void setTruck4Ratio(BigDecimal truck4Ratio)
    {
        this.truck4Ratio = truck4Ratio;
    }

    public BigDecimal getTruck4Ratio()
    {
        return truck4Ratio;
    }
    public void setTruck5Ratio(BigDecimal truck5Ratio)
    {
        this.truck5Ratio = truck5Ratio;
    }

    public BigDecimal getTruck5Ratio()
    {
        return truck5Ratio;
    }
    public void setTruck6Ratio(BigDecimal truck6Ratio)
    {
        this.truck6Ratio = truck6Ratio;
    }

    public BigDecimal getTruck6Ratio()
    {
        return truck6Ratio;
    }
    public void setSpecial1Ratio(BigDecimal special1Ratio)
    {
        this.special1Ratio = special1Ratio;
    }

    public BigDecimal getSpecial1Ratio()
    {
        return special1Ratio;
    }
    public void setSpecial2Ratio(BigDecimal special2Ratio)
    {
        this.special2Ratio = special2Ratio;
    }

    public BigDecimal getSpecial2Ratio()
    {
        return special2Ratio;
    }
    public void setSpecial3Ratio(BigDecimal special3Ratio)
    {
        this.special3Ratio = special3Ratio;
    }

    public BigDecimal getSpecial3Ratio()
    {
        return special3Ratio;
    }
    public void setSpecial4Ratio(BigDecimal special4Ratio)
    {
        this.special4Ratio = special4Ratio;
    }

    public BigDecimal getSpecial4Ratio()
    {
        return special4Ratio;
    }
    public void setSpecial5Ratio(BigDecimal special5Ratio)
    {
        this.special5Ratio = special5Ratio;
    }

    public BigDecimal getSpecial5Ratio()
    {
        return special5Ratio;
    }
    public void setSpecial6Ratio(BigDecimal special6Ratio)
    {
        this.special6Ratio = special6Ratio;
    }

    public BigDecimal getSpecial6Ratio()
    {
        return special6Ratio;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("statTime", getStatTime())
            .append("segmentNo", getSegmentNo())
            .append("year", getYear())
            .append("month", getMonth())
            .append("day", getDay())
            .append("hour", getHour())
            .append("totalFlow", getTotalFlow())
            .append("flowSveh", getFlowSveh())
            .append("flowMveh", getFlowMveh())
            .append("flowBveh", getFlowBveh())
            .append("avgSpeed", getAvgSpeed())
            .append("flowCar", getFlowCar())
            .append("flowTruck", getFlowTruck())
            .append("flowXveh", getFlowXveh())
            .append("flowDveh", getFlowDveh())
            .append("bus1", getBus1())
            .append("bus2", getBus2())
            .append("bus3", getBus3())
            .append("bus4", getBus4())
            .append("truck1", getTruck1())
            .append("truck2", getTruck2())
            .append("truck3", getTruck3())
            .append("truck4", getTruck4())
            .append("truck5", getTruck5())
            .append("truck6", getTruck6())
            .append("special1", getSpecial1())
            .append("special2", getSpecial2())
            .append("special3", getSpecial3())
            .append("special4", getSpecial4())
            .append("special5", getSpecial5())
            .append("special6", getSpecial6())
            .append("bus1Ratio", getBus1Ratio())
            .append("bus2Ratio", getBus2Ratio())
            .append("bus3Ratio", getBus3Ratio())
            .append("bus4Ratio", getBus4Ratio())
            .append("truck1Ratio", getTruck1Ratio())
            .append("truck2Ratio", getTruck2Ratio())
            .append("truck3Ratio", getTruck3Ratio())
            .append("truck4Ratio", getTruck4Ratio())
            .append("truck5Ratio", getTruck5Ratio())
            .append("truck6Ratio", getTruck6Ratio())
            .append("special1Ratio", getSpecial1Ratio())
            .append("special2Ratio", getSpecial2Ratio())
            .append("special3Ratio", getSpecial3Ratio())
            .append("special4Ratio", getSpecial4Ratio())
            .append("special5Ratio", getSpecial5Ratio())
            .append("special6Ratio", getSpecial6Ratio())
            .append("tunnelId", getTunnelId())
            .append("tunnelName", getTunnelName())
            .append("deviceId", getDeviceId())
            .append("eqType", getEqType())
            .append("direction", getDirection())
            .toString();
    }
}
