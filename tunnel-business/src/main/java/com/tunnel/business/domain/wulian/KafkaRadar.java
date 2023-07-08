package com.tunnel.business.domain.wulian;

import com.ruoyi.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;

/**
 * @author zhai
 * @date 2023/5/10
 */
@ApiModel("推送物联雷达轨迹数据实体")
public class KafkaRadar extends BaseEntity {

    private static final long serialVersionUID = 1L;

    //隧道id
    private String tunnelId;

    //方向
    private String direction;

    //车辆速度
    private String speed;

    //车道
    private String laneNo;

    //车辆类型
    private String vehicleType;

    //纬度
    private String lat;

    //经度
    private String lng;

    //距离
    private String distance;

    //车牌
    private String vehicleLicense;

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

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getLaneNo() {
        return laneNo;
    }

    public void setLaneNo(String laneNo) {
        this.laneNo = laneNo;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getVehicleLicense() {
        return vehicleLicense;
    }

    public void setVehicleLicense(String vehicleLicense) {
        this.vehicleLicense = vehicleLicense;
    }

    @Override
    public String toString() {
        return "KafkaRadar{" +
                "tunnelId='" + tunnelId + '\'' +
                ", direction=" + direction +
                ", speed=" + speed +
                ", laneNo=" + laneNo +
                ", vehicleType='" + vehicleType + '\'' +
                ", lat=" + lat +
                ", lng='" + lng + '\'' +
                ", distance='" + distance + '\'' +
                ", vehicleLicense='" + vehicleLicense + '\'' +
                '}';
    }
}