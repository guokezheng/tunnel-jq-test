package com.tunnel.business.domain.emeResource;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author zhai
 * @date 2024/1/24
 */
public class SdFocusVehicle extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 隧道id */
    @Excel(name = "隧道Id")
    private String tunnelId;

    @Excel(name = "隧道名称")
    private String tunnelName;

    /** 隧道方向 */
    @Excel(name = "隧道方向")
    private String direction;

    /** 车牌号 */
    @Excel(name = "车牌号")
    private String plate;

    /** 车辆类型 */
    @Excel(name = " 车辆类型")
    private String carType;

    /** 平均速度 */
    @Excel(name = " 平均速度")
    private int speed;

    /** 平均速度 */
    @Excel(name = " 进入时间")
    private String inTime;

    /** 平均速度 */
    @Excel(name = " 离开时间")
    private String outTime;

    private String startDate;

    private String endDate;

    private String ids;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTunnelId() {
        return tunnelId;
    }

    public void setTunnelId(String tunnelId) {
        this.tunnelId = tunnelId;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public String getInTime() {
        return inTime;
    }

    public void setInTime(String inTime) {
        this.inTime = inTime;
    }

    public String getOutTime() {
        return outTime;
    }

    public void setOutTime(String outTime) {
        this.outTime = outTime;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getTunnelName() {

        return tunnelName;
    }

    public void setTunnelName(String tunnelName) {
        this.tunnelName = tunnelName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("tunnelId", getTunnelId())
                .append("direction", getDirection())
                .append("plate", getPlate())
                .append("carType", getCarType())
                .append("speed", getSpeed())
                .append("inTime", getInTime())
                .append("outTime", getOutTime())
                .toString();
    }
}
