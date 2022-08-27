package com.tunnel.platform.domain.trafficOperationControl.controlConfig;

/**
 * describe: 交通拥挤度
 * 分为 畅通、基本畅通、轻度拥堵、中度拥堵、严重拥堵 5个级别
 *
 * 注：当速度为0并且断面交通量也为0时，路段为畅通状态
 * @author zs
 * @date 2022/2/26
 */
public enum  CongestionDegreeEnum {


    /**
     * 畅通,平均速度大于等于60
     */
    free_moving("1","畅通",60,200),

    /**
     * 基本畅通，平均速度[50,60）
     */
    basic_free_moving("2","基本畅通",50,60),

    /**
     * 轻度拥堵，平均速度[35,50）
     */
    light_congestion("3","轻度拥堵",35,50),

    /**
     * 中度拥堵，平均速度[20,35）
     */
    moderate_congestion("4","中度拥堵",20,35),

    /**
     * 严重拥堵，平均速度[0,20）
     */
    severe_congestion("5","严重拥堵",0,20);

    /**
     * 代号
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 速度最小值
     */
    private int minSpeed;

    /**
     * 速度最大值
     */
    private int maxSpeed;


    CongestionDegreeEnum(String code,String name,int minSpeed, int maxSpeed){
        this.code = code;
        this.name = name;
        this.minSpeed = minSpeed;
        this.maxSpeed = maxSpeed;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMinSpeed() {
        return minSpeed;
    }

    public void setMinSpeed(int minSpeed) {
        this.minSpeed = minSpeed;
    }

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(int maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public static CongestionDegreeEnum findEnum(String code){
        for(CongestionDegreeEnum ele : values()){
            if(ele.getCode().equals(code)){
                return ele;
            }
        }
        return null;
    }

    public static String findName(String code){
        for(CongestionDegreeEnum ele : values()){
            if(ele.getCode().equals(code)){
                return ele.getName();
            }
        }
        return null;
    }
}
