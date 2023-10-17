package com.tunnel.business.domain.event;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @author zhai
 * @date 2023/10/11
 */
public class SdTrafficVolume extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long id;

    @ApiModelProperty("隧道id")
    private String tunnelId;

    @Excel(name = "隧道名称")
    private String tunnelName;

    @ApiModelProperty("方向")
    @Excel(name = "方向",dictType = "sd_direction")
    private String direction;

    @ApiModelProperty("车流量")
    private String carNumber;

    @Excel(name = "车流量")
    private int carNum;

    private String originalData;

    @Excel(name = "统计类型",readConverterExp = "0=分,1=时,2=日,3=月,4=年")
    private int dateType;

    private String startDate;

    private String endDate;

    @Excel(name = "统计时间")
    private String carTime;

    private String ids;

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getCarTime() {
        return carTime;
    }

    public void setCarTime(String carTime) {
        this.carTime = carTime;
    }

    public int getCarNum() {
        return carNum;
    }

    public void setCarNum(int carNum) {
        this.carNum = carNum;
    }

    public String getTunnelName() {
        return tunnelName;
    }

    public void setTunnelName(String tunnelName) {
        this.tunnelName = tunnelName;
    }

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

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getOriginalData() {
        return originalData;
    }

    public void setOriginalData(String originalData) {
        this.originalData = originalData;
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

    public int getDateType() {
        return dateType;
    }

    public void setDateType(int dateType) {
        this.dateType = dateType;
    }

    @Override
    public String toString() {
        return "SdTrafficVolume{" +
                ", id=" + id +
                ", tunnelId='" + tunnelId + '\'' +
                ", direction='" + direction + '\'' +
                ", carNumber='" + carNumber + '\'' +
                ", originalData='" + originalData + '\'' +
                '}';
    }
}
