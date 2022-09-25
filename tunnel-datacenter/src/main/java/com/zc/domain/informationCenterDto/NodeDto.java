package com.zc.domain.informationCenterDto;



/**
 * 节点信息
 */
public class NodeDto {

    //距离范围(单位：米)
    private String distance;

    //收费站或门架id
    private String id;

    //纬度
    private String lat;

    //经度
    private String lng;

    //所属运管单位编码
    private String managerId;

    //机构类型
    private String nodeType;

    //所属高速公路路线编码（运营业务）
    private String opmaRoadId;

    //所属高速公路路段编码（运营业务）
    private String opmaSectionId;

    //所在地市编码
    private String regionId;

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getOpmaRoadId() {
        return opmaRoadId;
    }

    public void setOpmaRoadId(String opmaRoadId) {
        this.opmaRoadId = opmaRoadId;
    }

    public String getOpmaSectionId() {
        return opmaSectionId;
    }

    public void setOpmaSectionId(String opmaSectionId) {
        this.opmaSectionId = opmaSectionId;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    @Override
    public String toString() {
        return "NodeDto{" +
                "distance='" + distance + '\'' +
                ", id='" + id + '\'' +
                ", lat='" + lat + '\'' +
                ", lng='" + lng + '\'' +
                ", managerId='" + managerId + '\'' +
                ", nodeType='" + nodeType + '\'' +
                ", opmaRoadId='" + opmaRoadId + '\'' +
                ", opmaSectionId='" + opmaSectionId + '\'' +
                ", regionId='" + regionId + '\'' +
                '}';
    }
}
