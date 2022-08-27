package com.tunnel.platform.domain.trafficOperationControl.eventManage;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 交通事故和清障信息对象 sd_traffic_accident_info
 *
 * @author ruoyi
 * @date 2022-02-18
 */
public class SdTrafficAccidentInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    private Long id;

    /** 事件表sd_traffic_incident_info主键id */
    @Excel(name = "事件表sd_traffic_incident_info主键id")
    private Long incidentId;

    /** 路政案件 */
    @Excel(name = "路政案件")
    private String roadCase;

    /** 暂不关联路政案件 */
    @Excel(name = "暂不关联路政案件")
    private String noRoadCase;

    /** 方向 */
    @Excel(name = "方向")
    private String direction;

    /** 桩号（KM） */
    @Excel(name = "桩号", readConverterExp = "K=M")
    private String stakeNumber;

    /** 桩号距离（+m） */
    @Excel(name = "桩号距离", readConverterExp = "+=m")
    private String stakeDistance;

    /** 特殊地点描述 */
    @Excel(name = "特殊地点描述")
    private String specialSiteDesc;

    /** 缓行距离（公里） */
    @Excel(name = "缓行距离", readConverterExp = "公=里")
    private String slowDistance;

    /** 预计缓行解除时间（小时） */
    @Excel(name = "预计缓行解除时间", readConverterExp = "小=时")
    private String slowTime;

    /** 事故情况【请在备注中详述】 */
    @Excel(name = "事故情况【请在备注中详述】")
    private String accidentCondition;

    /** 事故车辆数目不详 */
    @Excel(name = "事故车辆数目不详")
    private String vehicleNumUnclear;

    /** 轿车（数量） */
    @Excel(name = "轿车", readConverterExp = "数=量")
    private String sedanCarNum;

    /** 大客（数量） */
    @Excel(name = "大客", readConverterExp = "数=量")
    private String bigBusNum;

    /** 中客（数量） */
    @Excel(name = "中客", readConverterExp = "数=量")
    private String middleBusNum;

    /** 小客（数量） */
    @Excel(name = "小客", readConverterExp = "数=量")
    private String smallBusNum;

    /** 大货（数量） */
    @Excel(name = "大货", readConverterExp = "数=量")
    private String trunkNum;

    /** 小货（数量） */
    @Excel(name = "小货", readConverterExp = "数=量")
    private String smallTrunkNum;

    /** 罐车（数量） */
    @Excel(name = "罐车", readConverterExp = "数=量")
    private String tankTrunkNum;

    /** 其他（数量） */
    @Excel(name = "其他", readConverterExp = "数=量")
    private String otherCarNum;

    /** 是否有危险品 */
    @Excel(name = "是否有危险品")
    private String hasHazardGoods;

    /** 是否有货物撒落 */
    @Excel(name = "是否有货物撒落")
    private String hasGoodsScatter;

    /** 是否有人员伤亡 */
    @Excel(name = "是否有人员伤亡")
    private String hasCasualty;

    /** 是否有车辆起火 */
    @Excel(name = "是否有车辆起火")
    private String hasCarFire;

    /** 是否缓行 */
    @Excel(name = "是否缓行")
    private String isCarSlowdown;

    /** 道路堵塞情况 */
    @Excel(name = "道路堵塞情况")
    private String roadCongestion;

    /** 堵塞车道名 */
    @Excel(name = "堵塞车道名")
    private String congestLane;

    /** 是否处理完毕 */
    @Excel(name = "是否处理完毕")
    private String isFinish;

    /** 是否有人员被困 */
    @Excel(name = "是否有人员被困")
    private String hasPersonTrap;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setIncidentId(Long incidentId)
    {
        this.incidentId = incidentId;
    }

    public Long getIncidentId()
    {
        return incidentId;
    }
    public void setRoadCase(String roadCase)
    {
        this.roadCase = roadCase;
    }

    public String getRoadCase()
    {
        return roadCase;
    }
    public void setNoRoadCase(String noRoadCase)
    {
        this.noRoadCase = noRoadCase;
    }

    public String getNoRoadCase()
    {
        return noRoadCase;
    }
    public void setDirection(String direction)
    {
        this.direction = direction;
    }

    public String getDirection()
    {
        return direction;
    }
    public void setStakeNumber(String stakeNumber)
    {
        this.stakeNumber = stakeNumber;
    }

    public String getStakeNumber()
    {
        return stakeNumber;
    }
    public void setStakeDistance(String stakeDistance)
    {
        this.stakeDistance = stakeDistance;
    }

    public String getStakeDistance()
    {
        return stakeDistance;
    }
    public void setSpecialSiteDesc(String specialSiteDesc)
    {
        this.specialSiteDesc = specialSiteDesc;
    }

    public String getSpecialSiteDesc()
    {
        return specialSiteDesc;
    }
    public void setSlowDistance(String slowDistance)
    {
        this.slowDistance = slowDistance;
    }

    public String getSlowDistance()
    {
        return slowDistance;
    }
    public void setSlowTime(String slowTime)
    {
        this.slowTime = slowTime;
    }

    public String getSlowTime()
    {
        return slowTime;
    }
    public void setAccidentCondition(String accidentCondition)
    {
        this.accidentCondition = accidentCondition;
    }

    public String getAccidentCondition()
    {
        return accidentCondition;
    }
    public void setVehicleNumUnclear(String vehicleNumUnclear)
    {
        this.vehicleNumUnclear = vehicleNumUnclear;
    }

    public String getVehicleNumUnclear()
    {
        return vehicleNumUnclear;
    }
    public void setSedanCarNum(String sedanCarNum)
    {
        this.sedanCarNum = sedanCarNum;
    }

    public String getSedanCarNum()
    {
        return sedanCarNum;
    }
    public void setBigBusNum(String bigBusNum)
    {
        this.bigBusNum = bigBusNum;
    }

    public String getBigBusNum()
    {
        return bigBusNum;
    }
    public void setMiddleBusNum(String middleBusNum)
    {
        this.middleBusNum = middleBusNum;
    }

    public String getMiddleBusNum()
    {
        return middleBusNum;
    }
    public void setSmallBusNum(String smallBusNum)
    {
        this.smallBusNum = smallBusNum;
    }

    public String getSmallBusNum()
    {
        return smallBusNum;
    }
    public void setTrunkNum(String trunkNum)
    {
        this.trunkNum = trunkNum;
    }

    public String getTrunkNum()
    {
        return trunkNum;
    }
    public void setSmallTrunkNum(String smallTrunkNum)
    {
        this.smallTrunkNum = smallTrunkNum;
    }

    public String getSmallTrunkNum()
    {
        return smallTrunkNum;
    }
    public void setTankTrunkNum(String tankTrunkNum)
    {
        this.tankTrunkNum = tankTrunkNum;
    }

    public String getTankTrunkNum()
    {
        return tankTrunkNum;
    }
    public void setOtherCarNum(String otherCarNum)
    {
        this.otherCarNum = otherCarNum;
    }

    public String getOtherCarNum()
    {
        return otherCarNum;
    }
    public void setHasHazardGoods(String hasHazardGoods)
    {
        this.hasHazardGoods = hasHazardGoods;
    }

    public String getHasHazardGoods()
    {
        return hasHazardGoods;
    }
    public void setHasGoodsScatter(String hasGoodsScatter)
    {
        this.hasGoodsScatter = hasGoodsScatter;
    }

    public String getHasGoodsScatter()
    {
        return hasGoodsScatter;
    }
    public void setHasCasualty(String hasCasualty)
    {
        this.hasCasualty = hasCasualty;
    }

    public String getHasCasualty()
    {
        return hasCasualty;
    }
    public void setHasCarFire(String hasCarFire)
    {
        this.hasCarFire = hasCarFire;
    }

    public String getHasCarFire()
    {
        return hasCarFire;
    }
    public void setIsCarSlowdown(String isCarSlowdown)
    {
        this.isCarSlowdown = isCarSlowdown;
    }

    public String getIsCarSlowdown()
    {
        return isCarSlowdown;
    }
    public void setRoadCongestion(String roadCongestion)
    {
        this.roadCongestion = roadCongestion;
    }

    public String getRoadCongestion()
    {
        return roadCongestion;
    }
    public void setCongestLane(String congestLane)
    {
        this.congestLane = congestLane;
    }

    public String getCongestLane()
    {
        return congestLane;
    }
    public void setIsFinish(String isFinish)
    {
        this.isFinish = isFinish;
    }

    public String getIsFinish()
    {
        return isFinish;
    }
    public void setHasPersonTrap(String hasPersonTrap)
    {
        this.hasPersonTrap = hasPersonTrap;
    }

    public String getHasPersonTrap()
    {
        return hasPersonTrap;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("incidentId", getIncidentId())
            .append("roadCase", getRoadCase())
            .append("noRoadCase", getNoRoadCase())
            .append("direction", getDirection())
            .append("stakeNumber", getStakeNumber())
            .append("stakeDistance", getStakeDistance())
            .append("specialSiteDesc", getSpecialSiteDesc())
            .append("slowDistance", getSlowDistance())
            .append("slowTime", getSlowTime())
            .append("accidentCondition", getAccidentCondition())
            .append("vehicleNumUnclear", getVehicleNumUnclear())
            .append("sedanCarNum", getSedanCarNum())
            .append("bigBusNum", getBigBusNum())
            .append("middleBusNum", getMiddleBusNum())
            .append("smallBusNum", getSmallBusNum())
            .append("trunkNum", getTrunkNum())
            .append("smallTrunkNum", getSmallTrunkNum())
            .append("tankTrunkNum", getTankTrunkNum())
            .append("otherCarNum", getOtherCarNum())
            .append("hasHazardGoods", getHasHazardGoods())
            .append("hasGoodsScatter", getHasGoodsScatter())
            .append("hasCasualty", getHasCasualty())
            .append("hasCarFire", getHasCarFire())
            .append("isCarSlowdown", getIsCarSlowdown())
            .append("roadCongestion", getRoadCongestion())
            .append("congestLane", getCongestLane())
            .append("isFinish", getIsFinish())
            .append("hasPersonTrap", getHasPersonTrap())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .append("updateTime", getUpdateTime())
            .append("updateBy", getUpdateBy())
            .toString();
    }
}
