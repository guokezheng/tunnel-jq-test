package com.tunnel.business.domain;

public class EventObjectsEntity {

    //视频结构化信息类型  0:人脸；1:人员；2:机动车；3:非机动车；4:物品；5:场景；6:事件
    private String ObjectType;
    //目标ID
    private Integer ObjectID;
    //抓拍目标置信度
    private float CaptureReliability;
    //目标速度
    private Double ObjectSpeed;
    //抓拍目标位置—左上角x坐标
    private Integer LeftTopX;
    //抓拍目标位置—左上角y坐标
    private Integer LeftTopY;
    //抓拍目标位置—右下角x坐标
    private Integer RightBtmX;
    //抓拍目标位置—右下角y坐标
    private Integer RightBtmY;
    //已存在次数
    private Integer ExistNum;
    //匹配次数
    private Integer MatchNum;
    //是否已报警
    private String HasAlarmed;
    /** 车辆类型
     * 0-轿车   1-面包车  2-小货车   3-中巴车
     * 4-大客车 5-大货车  6-越野车   7-皮卡车
     * 8-商务车  9-渣土车  10-农用货车 11-拖拉机   12-跑车   13-微型轿车  14-两厢轿车
     * 15-三厢轿车  16-轻型客车  17-中型货车  18-挂车  19-槽罐车  20-洒水车  21-小型车  22-中型车  23-大型车  99-未知
     */
    private String VehicleClass;
    /**车辆颜色
     *  1黑，2白，3灰，4红，5蓝，6黄，7橙，8棕，9绿，10紫，11青，12粉，13透明，99其他
     */
    private String VehicleColor;
    /**是否有车牌
     *    0没有,  1有
     */
    private String HasPlate;
    /**车牌类型
     *    01大型汽车，02小型汽车，03使馆汽车，04领馆汽车，05境外汽车，06外籍汽车，07两、三轮摩托车号牌，
     *    08轻便摩托车，09使馆摩托车，10领馆摩托车，11境外摩托车，12外籍摩托车，13农用运输车，14拖拉机，
     *    15挂车，16教练汽车，17教练摩托车，18试验汽车，19试验摩托车，20临时入境汽车，21临时入境摩托车，
     *    22临时行驶车，23警用汽车，24警用摩托，25原农机号牌，26香港入出境车，27澳门入出境车，28中型车，
     *    31武警号牌，32军队号牌，33行人，34非机动车，51大型新能源车牌，52小型新能源车牌，99其他
     */
    private String PlateClass;
    /**车牌颜色
     *      1黑，2白，3灰，4红，5蓝，6黄，7橙，8棕，9绿，10紫，11青，12粉，13透明，99其他
     */
    private String PlateColor;
    /**车牌号
     *      1黑，2白，3灰，4红，5蓝，6黄，7橙，8棕，9绿，10紫，11青，12粉，13透明，99其他
     */
    private String PlateNo;
    /**整个号牌的识别可信度
     *     0~100数值表示，值越大可信度越高
     */
    private String PlateReliability;
    //记录 ID
    private String recordID;

    public String getObjectType() {
        return ObjectType;
    }

    public void setObjectType(String objectType) {
        ObjectType = objectType;
    }

    public Integer getObjectID() {
        return ObjectID;
    }

    public void setObjectID(Integer objectID) {
        ObjectID = objectID;
    }

    public float getCaptureReliability() {
        return CaptureReliability;
    }

    public void setCaptureReliability(float captureReliability) {
        CaptureReliability = captureReliability;
    }

    public Double getObjectSpeed() {
        return ObjectSpeed;
    }

    public void setObjectSpeed(Double objectSpeed) {
        ObjectSpeed = objectSpeed;
    }

    public Integer getLeftTopX() {
        return LeftTopX;
    }

    public void setLeftTopX(Integer leftTopX) {
        LeftTopX = leftTopX;
    }

    public Integer getLeftTopY() {
        return LeftTopY;
    }

    public void setLeftTopY(Integer leftTopY) {
        LeftTopY = leftTopY;
    }

    public Integer getRightBtmX() {
        return RightBtmX;
    }

    public void setRightBtmX(Integer rightBtmX) {
        RightBtmX = rightBtmX;
    }

    public Integer getRightBtmY() {
        return RightBtmY;
    }

    public void setRightBtmY(Integer rightBtmY) {
        RightBtmY = rightBtmY;
    }

    public Integer getExistNum() {
        return ExistNum;
    }

    public void setExistNum(Integer existNum) {
        ExistNum = existNum;
    }

    public Integer getMatchNum() {
        return MatchNum;
    }

    public void setMatchNum(Integer matchNum) {
        MatchNum = matchNum;
    }

    public String getHasAlarmed() {
        return HasAlarmed;
    }

    public void setHasAlarmed(String hasAlarmed) {
        HasAlarmed = hasAlarmed;
    }

    public String getVehicleClass() {
        return VehicleClass;
    }

    public void setVehicleClass(String vehicleClass) {
        VehicleClass = vehicleClass;
    }

    public String getVehicleColor() {
        return VehicleColor;
    }

    public void setVehicleColor(String vehicleColor) {
        VehicleColor = vehicleColor;
    }

    public String getHasPlate() {
        return HasPlate;
    }

    public void setHasPlate(String hasPlate) {
        HasPlate = hasPlate;
    }

    public String getPlateClass() {
        return PlateClass;
    }

    public void setPlateClass(String plateClass) {
        PlateClass = plateClass;
    }

    public String getPlateColor() {
        return PlateColor;
    }

    public void setPlateColor(String plateColor) {
        PlateColor = plateColor;
    }

    public String getPlateNo() {
        return PlateNo;
    }

    public void setPlateNo(String plateNo) {
        PlateNo = plateNo;
    }

    public String getPlateReliability() {
        return PlateReliability;
    }

    public void setPlateReliability(String plateReliability) {
        PlateReliability = plateReliability;
    }

    public String getRecordID() {
        return recordID;
    }

    public void setRecordID(String recordID) {
        this.recordID = recordID;
    }
}
