package com.tunnel.business.domain.event;

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

    @ApiModelProperty("方向")
    private String direction;

    @ApiModelProperty("车流量")
    private String carNumber;

    private String originalData;

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
