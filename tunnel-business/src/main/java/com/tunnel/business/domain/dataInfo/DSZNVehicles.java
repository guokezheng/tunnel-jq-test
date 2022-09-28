package com.tunnel.business.domain.dataInfo;

import java.sql.Date;
import java.util.List;

/**
 * @author ：WYZ
 * @date ：Created in 2021/11/30 9:42
 * @description：车辆信息
 * @modified By：
 * @version: $
 */
public class DSZNVehicles {
    //记录ID
    private  String id;
    //数据存储时间
    private Date createTime;
    //数据上传时间
    private Date uploadTime;
    //抓怕事件，采集时间
    private  Date collectTime;
    //道路ID
    private Long roadCodeId;
    //道路名称
    private String roadCodeName;
    //道路分段ID
    private Long roadSectionId;
    //标识是否停车
    private Boolean parking;
    //标识，是否删除
    private Boolean deleted;
    //是否最新车辆
    private Boolean current;
    //邻道干扰产生的并行车辆
    private Boolean parallel;
    //车牌号
    private String carNum;
    //雷达上传车辆全局ID
    private int golbalId;
    //成功信标关联总数
    private int totalActualFlow;
    //车牌类型
    private int carPlateType;
    //摄像头上报的行驶方向。-1表示靠近摄像头，+1表示原理摄像头，0-表示方向不明
    private int direction;
    //true-正向行驶，false-逆行
    private Boolean reverse;
    //true-车辆上行，在一二车道。false-下行，在三四车道
    private Boolean upDown;
    //车辆经过的信标流，即轨迹
    private List<DSZNFlow> flows;
    ///事件
    private List<DSZNEvents> events;
    //车辆类型（1-微型;2-摩托车;3-小型车;4-中型车;5-大型车;6-大挂车）
    private VehicleTypeEnum vehicleStatus;

    /*
    * 当 vehicleStatus == ADJACENTFAKE(3,"邻道干扰") 时，此字段指向产生本车的真实车辆
     当 vehicleStatus != ADJACENTFAKE 时，此字段指向由本车产生的邻道干扰虚拟车辆
    carNum会变，golbalId会重复，只有vehicleId不变且不重复
    * */
    private String adjacentVehicleId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    public Date getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(Date collectTime) {
        this.collectTime = collectTime;
    }

    public Long getRoadCodeId() {
        return roadCodeId;
    }

    public void setRoadCodeId(Long roadCodeId) {
        this.roadCodeId = roadCodeId;
    }

    public String getRoadCodeName() {
        return roadCodeName;
    }

    public void setRoadCodeName(String roadCodeName) {
        this.roadCodeName = roadCodeName;
    }

    public Long getRoadSectionId() {
        return roadSectionId;
    }

    public void setRoadSectionId(Long roadSectionId) {
        this.roadSectionId = roadSectionId;
    }

    public Boolean getParking() {
        return parking;
    }

    public void setParking(Boolean parking) {
        this.parking = parking;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Boolean getCurrent() {
        return current;
    }

    public void setCurrent(Boolean current) {
        this.current = current;
    }

    public Boolean getParallel() {
        return parallel;
    }

    public void setParallel(Boolean parallel) {
        this.parallel = parallel;
    }

    public String getCarNum() {
        return carNum;
    }

    public void setCarNum(String carNum) {
        this.carNum = carNum;
    }

    public int getGolbalId() {
        return golbalId;
    }

    public void setGolbalId(int golbalId) {
        this.golbalId = golbalId;
    }

    public int getTotalActualFlow() {
        return totalActualFlow;
    }

    public void setTotalActualFlow(int totalActualFlow) {
        this.totalActualFlow = totalActualFlow;
    }

    public int getCarPlateType() {
        return carPlateType;
    }

    public void setCarPlateType(int carPlateType) {
        this.carPlateType = carPlateType;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public Boolean getReverse() {
        return reverse;
    }

    public void setReverse(Boolean reverse) {
        this.reverse = reverse;
    }

    public Boolean getUpDown() {
        return upDown;
    }

    public void setUpDown(Boolean upDown) {
        this.upDown = upDown;
    }

    public List getFlows() {
        return flows;
    }

    public void setFlows(List flows) {
        this.flows = flows;
    }

    public List getEvents() {
        return events;
    }

    public void setEvents(List events) {
        this.events = events;
    }

    public VehicleTypeEnum getVehicleStatus() {
        return vehicleStatus;
    }

    public void setVehicleStatus(VehicleTypeEnum vehicleStatus) {
        this.vehicleStatus = vehicleStatus;
    }

    public String getAdjacentVehicleId() {
        return adjacentVehicleId;
    }

    public void setAdjacentVehicleId(String adjacentVehicleId) {
        this.adjacentVehicleId = adjacentVehicleId;
    }
}
