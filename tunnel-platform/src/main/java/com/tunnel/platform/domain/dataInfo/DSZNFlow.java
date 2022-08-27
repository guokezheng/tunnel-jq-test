package com.tunnel.platform.domain.dataInfo;

import java.sql.Date;

/**
 * @author ：WYZ
 * @date ：Created in 2021/11/30 9:55
 * @description：车辆轨迹
 * @modified By：
 * @version: $
 */
public class DSZNFlow {
    //设备唯一编号
    private String devEUI;
    //雷达ID
    private int  radarId;
    //设备顺序编号
    private int devSort;
    //速度，单位m/s，只有一个信标的时候速度为空
    private Double speed;
    //车辆方向，true--正向行驶，false--逆向行驶
    private Boolean direction;
    //是否离开，true--在这个信标之后离开
    private Double left;
    //距离雷达距离
    private  Double x;
    //速度，单位km/h，只有一个信标的时候速度为空
    private Double speedKMpH;
    //经过此信标的时间
    private Date passAt;
    //经过此信标的时间
    private  Long passTime;
    //车辆通过信标的时长，单位毫秒
    private int timeLength;
    //旋转方向：0-顺时针，1-逆时针
    private RotateEnum rotate;
    //地磁峰值
    private int geomPeak;
    //与前车的距离，可能为空
     private Double distance;
     //车道，取值范围1、2。车道1上排列1,2,3,4,5...100。车道2上排列101,101,103...200
    private  int lane;
    //车辆方向，true--正向行驶，false--逆向行驶
    private  Boolean vehicleDirection;
    //信标方向
    private  Boolean beaconDirection;
    //车辆类型(1-微型;2-摩托车;3-小型车;4-中型车;5-大型车;6-大挂车)
    private  VehicleTypeEnum vehicleType;
    //是否漏检。true-漏检，人工补齐的数据。false-没有漏检，信标上报的数据
    private LostReduEnum lost;
    //车辆状态
//    private  VehicleState vehicleState;
    //车辆状态协方差矩阵P
//    private  VehicleStateP vehicleStateP;
    //路段ID
    private Long roadSectionId;

    public String getDevEUI() {
        return devEUI;
    }

    public void setDevEUI(String devEUI) {
        this.devEUI = devEUI;
    }

    public int getRadarId() {
        return radarId;
    }

    public void setRadarId(int radarId) {
        this.radarId = radarId;
    }

    public int getDevSort() {
        return devSort;
    }

    public void setDevSort(int devSort) {
        this.devSort = devSort;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Boolean getDirection() {
        return direction;
    }

    public void setDirection(Boolean direction) {
        this.direction = direction;
    }

    public Double getLeft() {
        return left;
    }

    public void setLeft(Double left) {
        this.left = left;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getSpeedKMpH() {
        return speedKMpH;
    }

    public void setSpeedKMpH(Double speedKMpH) {
        this.speedKMpH = speedKMpH;
    }

    public Date getPassAt() {
        return passAt;
    }

    public void setPassAt(Date passAt) {
        this.passAt = passAt;
    }

    public Long getPassTime() {
        return passTime;
    }

    public void setPassTime(Long passTime) {
        this.passTime = passTime;
    }

    public int getTimeLength() {
        return timeLength;
    }

    public void setTimeLength(int timeLength) {
        this.timeLength = timeLength;
    }

    public RotateEnum getRotate() {
        return rotate;
    }

    public void setRotate(RotateEnum rotate) {
        this.rotate = rotate;
    }

    public int getGeomPeak() {
        return geomPeak;
    }

    public void setGeomPeak(int geomPeak) {
        this.geomPeak = geomPeak;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public int getLane() {
        return lane;
    }

    public void setLane(int lane) {
        this.lane = lane;
    }

    public Boolean getVehicleDirection() {
        return vehicleDirection;
    }

    public void setVehicleDirection(Boolean vehicleDirection) {
        this.vehicleDirection = vehicleDirection;
    }

    public Boolean getBeaconDirection() {
        return beaconDirection;
    }

    public void setBeaconDirection(Boolean beaconDirection) {
        this.beaconDirection = beaconDirection;
    }

    public VehicleTypeEnum getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleTypeEnum vehicleType) {
        this.vehicleType = vehicleType;
    }

    public LostReduEnum getLost() {
        return lost;
    }

    public void setLost(LostReduEnum lost) {
        this.lost = lost;
    }


    public Long getRoadSectionId() {
        return roadSectionId;
    }

    public void setRoadSectionId(Long roadSectionId) {
        this.roadSectionId = roadSectionId;
    }
}
