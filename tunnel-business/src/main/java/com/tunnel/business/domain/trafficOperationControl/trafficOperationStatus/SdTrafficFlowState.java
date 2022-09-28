package com.tunnel.business.domain.trafficOperationControl.trafficOperationStatus;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * 【交通流状态记录】对象 sd_traffic_flow_state
 *
 * @author ruoyi
 * @date 2022-02-14
 */
public class SdTrafficFlowState extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    private Long id;

    /** 隧道id */
    @Excel(name = "隧道id")
    private String tunnelId;

    @TableField(exist = false)
    @Excel(name="隧道名称")
    private String tunnelName;

    /** 统计时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "统计时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date statisticTime;

    /** 方向 */
    @Excel(name = "方向")
    private String direction;

    /** 交通流量（辆） */
    @Excel(name = "交通流量", readConverterExp = "辆=")
    private String trafficFlow;

    /** 平均速度（km/h） */
    @Excel(name = "平均速度", readConverterExp = "k=m/h")
    private String averageVelocity;

    @Excel(name="车道占有率")
    private String lineOccupancy;

    /** 客车流量 */
    @Excel(name = "客车流量")
    private String carNum;

    /** 货车流量 */
    @Excel(name = "货车流量")
    private String trunkNum;

    /** 专项车流量 */
    @Excel(name = "专项车流量")
    private String specialCarNum;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setTunnelId(String tunnelId)
    {
        this.tunnelId = tunnelId;
    }

    public String getTunnelId()
    {
        return tunnelId;
    }

    public String getTunnelName() {
        return tunnelName;
    }

    public void setTunnelName(String tunnelName) {
        this.tunnelName = tunnelName;
    }

    public void setStatisticTime(Date statisticTime)
    {
        this.statisticTime = statisticTime;
    }

    public Date getStatisticTime()
    {
        return statisticTime;
    }
    public void setDirection(String direction)
    {
        this.direction = direction;
    }

    public String getDirection()
    {
        return direction;
    }
    public void setTrafficFlow(String trafficFlow)
    {
        this.trafficFlow = trafficFlow;
    }

    public String getTrafficFlow()
    {
        return trafficFlow;
    }
    public void setAverageVelocity(String averageVelocity)
    {
        this.averageVelocity = averageVelocity;
    }

    public String getAverageVelocity()
    {
        return averageVelocity;
    }

    public String getLineOccupancy() {
        return lineOccupancy;
    }

    public void setLineOccupancy(String lineOccupancy) {
        this.lineOccupancy = lineOccupancy;
    }

    public void setCarNum(String carNum)
    {
        this.carNum = carNum;
    }

    public String getCarNum()
    {
        return carNum;
    }
    public void setTrunkNum(String trunkNum)
    {
        this.trunkNum = trunkNum;
    }

    public String getTrunkNum()
    {
        return trunkNum;
    }
    public void setSpecialCarNum(String specialCarNum)
    {
        this.specialCarNum = specialCarNum;
    }

    public String getSpecialCarNum()
    {
        return specialCarNum;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("tunnelId", getTunnelId())
                .append("tunnelName",getTunnelName())
            .append("statisticTime", getStatisticTime())
            .append("direction", getDirection())
            .append("trafficFlow", getTrafficFlow())
            .append("averageVelocity", getAverageVelocity())
            .append("lineOccupancy",getLineOccupancy())
            .append("carNum", getCarNum())
            .append("trunkNum", getTrunkNum())
            .append("specialCarNum", getSpecialCarNum())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .append("updateTime", getUpdateTime())
            .append("updateBy", getUpdateBy())
            .toString();
    }
}
