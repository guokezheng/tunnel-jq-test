package com.zc.domain.informationCenterDto;

import lombok.Data;

/**
 * 国标公路字典
 */
public class RoadDto {

    //国标路线编码
    private String roadId;

    //国标路线名称
    private String roadName;

    //技术等级
    private String tecLevel;

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

    public String getTecLevel() {
        return tecLevel;
    }

    public void setTecLevel(String tecLevel) {
        this.tecLevel = tecLevel;
    }

    @Override
    public String toString() {
        return "RoadDto{" +
                "roadId='" + roadId + '\'' +
                ", roadName='" + roadName + '\'' +
                ", tecLevel='" + tecLevel + '\'' +
                '}';
    }
}
