package com.tunnel.platform.domain.dataInfo;

/**
 * @author ：WYZ
 * @date ：Created in 2021/11/30 10:24
 * @description：畅通信息
 * @modified By：
 * @version: $
 */
public class DSZNRoadSmooth {
    //畅通程度
    private Enum smooth;
    //发生的位置范围
    private int devSortBegin;
    //发生的位置范围
    private int devSortEnd;
    //车道
    private int lane;

    public Enum getSmooth() {
        return smooth;
    }

    public void setSmooth(Enum smooth) {
        this.smooth = smooth;
    }

    public int getDevSortBegin() {
        return devSortBegin;
    }

    public void setDevSortBegin(int devSortBegin) {
        this.devSortBegin = devSortBegin;
    }

    public int getDevSortEnd() {
        return devSortEnd;
    }

    public void setDevSortEnd(int devSortEnd) {
        this.devSortEnd = devSortEnd;
    }

    public int getLane() {
        return lane;
    }

    public void setLane(int lane) {
        this.lane = lane;
    }
}
