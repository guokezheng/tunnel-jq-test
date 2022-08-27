package com.tunnel.platform.domain.dataInfo;

/*车型*/
public enum VehicleTypeEnum {
    /**
     * 未定义
     */
    UNKNOWN(0,"未定义"),

    /**
     * 微型
     */
    MINI(1,"微型车"),

    /**
     * 摩托车
     */
    MOTOR(2,"摩托车"),

    /**
     * 小型车
     */
    SMALL(3,"小型车"),

    /**
     * 中型车
     */
    MEDIUM(4,"中型车"),

    /**
     * 大型车
     */
    LARGE(5,"大型车"),
    /**
     * 大挂车
     */
    LONG_TRUCK(6,"大挂车"),
    /**
     * 客车
     */
    PASSENGER_CAR(7,"客车"),
    /**
     * 货车
     */
    TRUCK(8,"货车"),
    /**
     * 轿车
     */
    CAR(9,"轿车"),
    /**
     * 面包车
     */
    VAN(10,"面包车"),
    /**
     * 小货车
     */
    MINI_TRUCK(11,"小货车"),
    /**
     * 行人
     */
    PEDESTRIAN(12,"行人"),
    /**
     * 两轮车
     */
    DOGCART(13,"两轮车"),
    /**
     * 三轮车
     */
    TRICYCLE(14,"三轮车"),
    /**
     * SUV/MPV
     */
    SUV(15,"SUV/MPV"),
    /**
     * 中型客车
     */
    MEDIUM_SIZED_PASSENGER_CAR(16,"中型客车"),
    /**
     * 机动车
     */
    VEHICLE(17,"机动车"),
    /**
     * 非机动车
     */
    NON_MOTOR_VEHICLE(18,"非机动车"),
    /**
     * 小轿车
     */
    SMALL_CAR(19,"小轿车"),
    /**
     * 微型轿车
     */
    MINI_CAR(20,"微型轿车"),
    /**
     * 皮卡车
     */
    PICKUP_TRUCK(21,"皮卡车"),
    /**
     * 集装箱卡车
     */
    CONTAINER_TRUCK(22,"集装箱卡车"),
    /**
     * 微卡车
     */
    MICRO_TRUCK(23,"微卡车"),
    /**
     * 渣土车
     */
    BULLDOZER(24,"渣土车"),
    /**
     * 吊车/工程车
     */
    CRANE(25,"吊车/工程车"),
    /**
     * 油罐车
     */
    FUEL_TANK_CAR(26,"油罐车"),
    /**
     * 混凝土搅拌车
     */
    CONCRETE_MIXER_TRUCK(27,"混凝土搅拌车"),
    /**
     * 平板拖车
     */
    FLATBED_TRAILER(28,"平板拖车"),
    /**
     * 两厢轿车
     */
    HATCHBACK_CAR(29,"两厢轿车"),
    /**
     * 三厢轿车
     */
    A_hatchback(30,"三厢轿车"),
    /**
     * 轿跑
     */
    COUPE(31,"轿跑"),
    /**
     * 小型客车
     */
    MINIBUS(32,"小型客车");

    VehicleTypeEnum(int i, String name) {

    }
}
