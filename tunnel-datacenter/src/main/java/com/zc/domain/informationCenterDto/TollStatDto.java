package com.zc.domain.informationCenterDto;



/**
 * 收费站字典
 */
public class TollStatDto {

    //每页显示记录数
    private Integer limit;

    //国标路线id
    private String opmaCliqueId;

    //国标路线名称
    private String opmaCliqueName;

    //排序方式，可选值(asc、desc)
    private String order;

    //排序字段
    private String orderField;

    //当前页码，从1开始
    private Integer page;

    //国标路线id
    private String regionId;

    //国标路线名称
    private String regionName;

    //国标路线id
    private String roadId;

    //国标路线名称
    private String roadName;

    //收费站全称
    private String tollStationFullName;

    //收费站hex编码
    private String tollStationHex;

    //收费站编号
    private String tollStationId;

    //收费站名称
    private String tollStationName;

    //省标收费站编号
    private String tollStationProvinceId;

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public String getOpmaCliqueId() {
        return opmaCliqueId;
    }

    public void setOpmaCliqueId(String opmaCliqueId) {
        this.opmaCliqueId = opmaCliqueId;
    }

    public String getOpmaCliqueName() {
        return opmaCliqueName;
    }

    public void setOpmaCliqueName(String opmaCliqueName) {
        this.opmaCliqueName = opmaCliqueName;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getOrderField() {
        return orderField;
    }

    public void setOrderField(String orderField) {
        this.orderField = orderField;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getRoadId() {
        return roadId;
    }

    public void setRoadId(String roadId) {
        this.roadId = roadId;
    }

    public String getRoadName() {
        return roadName;
    }

    public void setRoadName(String roadName) {
        this.roadName = roadName;
    }

    public String getTollStationFullName() {
        return tollStationFullName;
    }

    public void setTollStationFullName(String tollStationFullName) {
        this.tollStationFullName = tollStationFullName;
    }

    public String getTollStationHex() {
        return tollStationHex;
    }

    public void setTollStationHex(String tollStationHex) {
        this.tollStationHex = tollStationHex;
    }

    public String getTollStationId() {
        return tollStationId;
    }

    public void setTollStationId(String tollStationId) {
        this.tollStationId = tollStationId;
    }

    public String getTollStationName() {
        return tollStationName;
    }

    public void setTollStationName(String tollStationName) {
        this.tollStationName = tollStationName;
    }

    public String getTollStationProvinceId() {
        return tollStationProvinceId;
    }

    public void setTollStationProvinceId(String tollStationProvinceId) {
        this.tollStationProvinceId = tollStationProvinceId;
    }

    @Override
    public String toString() {
        return "TollStatDto{" +
                "limit=" + limit +
                ", opmaCliqueId='" + opmaCliqueId + '\'' +
                ", opmaCliqueName='" + opmaCliqueName + '\'' +
                ", order='" + order + '\'' +
                ", orderField='" + orderField + '\'' +
                ", page=" + page +
                ", regionId='" + regionId + '\'' +
                ", regionName='" + regionName + '\'' +
                ", roadId='" + roadId + '\'' +
                ", roadName='" + roadName + '\'' +
                ", tollStationFullName='" + tollStationFullName + '\'' +
                ", tollStationHex='" + tollStationHex + '\'' +
                ", tollStationId='" + tollStationId + '\'' +
                ", tollStationName='" + tollStationName + '\'' +
                ", tollStationProvinceId='" + tollStationProvinceId + '\'' +
                '}';
    }
}
