package com.tunnel.business.domain.dataInfo;

/**
 * @author ：WYZ
 * @date ：Created in 2021/11/30 9:39
 * @description：主路与匝道汇入，汇出关联关系
 * @modified By：
 * @version: $
 */
public class DSZNDeviceRelation {
    //主路路段ID
    private Long mainRoadSectionId;
    //主路车道
    private int mainLane;
    //主路编号
    private int mainDevSort;
    //匝道路段ID
    private int rampRoadSectionId;
    //匝道车道
    private int rampLane;
    //匝道编号
    private int rampDevSort;
    // true-匝道汇入，false-匝道汇出
    private  Boolean into;

    public Long getMainRoadSectionId() {
        return mainRoadSectionId;
    }

    public void setMainRoadSectionId(Long mainRoadSectionId) {
        this.mainRoadSectionId = mainRoadSectionId;
    }

    public int getMainLane() {
        return mainLane;
    }

    public void setMainLane(int mainLane) {
        this.mainLane = mainLane;
    }

    public int getMainDevSort() {
        return mainDevSort;
    }

    public void setMainDevSort(int mainDevSort) {
        this.mainDevSort = mainDevSort;
    }

    public int getRampRoadSectionId() {
        return rampRoadSectionId;
    }

    public void setRampRoadSectionId(int rampRoadSectionId) {
        this.rampRoadSectionId = rampRoadSectionId;
    }

    public int getRampLane() {
        return rampLane;
    }

    public void setRampLane(int rampLane) {
        this.rampLane = rampLane;
    }

    public int getRampDevSort() {
        return rampDevSort;
    }

    public void setRampDevSort(int rampDevSort) {
        this.rampDevSort = rampDevSort;
    }

    public Boolean getInto() {
        return into;
    }

    public void setInto(Boolean into) {
        this.into = into;
    }
}
