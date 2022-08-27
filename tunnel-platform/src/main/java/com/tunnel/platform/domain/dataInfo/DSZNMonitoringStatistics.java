package com.tunnel.platform.domain.dataInfo;

import java.util.List;

/**
 * @author ：WYZ
 * @date ：Created in 2021/11/30 10:16
 * @description：实时统计信息
 * @modified By：
 * @version: $
 */
public class DSZNMonitoringStatistics {
    //平均速度
    private Double vehicleSpeed;
    //总车流量
    private  int totalVehicleFlow;
    //当前车辆类型与数理
    private List vehicle;
    //总的车辆类型与数理
    private List totalVehicle;
    //历史车辆类型与数理
    private List hisVehicle;

    public Double getVehicleSpeed() {
        return vehicleSpeed;
    }

    public void setVehicleSpeed(Double vehicleSpeed) {
        this.vehicleSpeed = vehicleSpeed;
    }

    public int getTotalVehicleFlow() {
        return totalVehicleFlow;
    }

    public void setTotalVehicleFlow(int totalVehicleFlow) {
        this.totalVehicleFlow = totalVehicleFlow;
    }

    public List getVehicle() {
        return vehicle;
    }

    public void setVehicle(List vehicle) {
        this.vehicle = vehicle;
    }

    public List getTotalVehicle() {
        return totalVehicle;
    }

    public void setTotalVehicle(List totalVehicle) {
        this.totalVehicle = totalVehicle;
    }

    public List getHisVehicle() {
        return hisVehicle;
    }

    public void setHisVehicle(List hisVehicle) {
        this.hisVehicle = hisVehicle;
    }
}
