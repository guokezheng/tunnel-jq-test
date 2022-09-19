package com.tunnel.business.domain.dataInfo;
/*
* 车辆状态
* */
public enum VehicleStatusEnum {
    UNKNOWN(0,"未定义"),

    ENTER(1,"新增车辆"),

    STOPPED(2,"停车"),

    ADJACENTFAKE(3,"邻道干扰"),

    CHANGELANE(4,"变道车辆"),

    EXIT(5,"车辆驶出");

    VehicleStatusEnum(int i, String name) {
    }
}
