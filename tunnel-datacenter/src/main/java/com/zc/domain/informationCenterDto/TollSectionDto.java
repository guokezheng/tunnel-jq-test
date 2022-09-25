package com.zc.domain.informationCenterDto;



/**
 * 收费路段字典
 */
public class TollSectionDto {

    //国标路线编码
    private String roadId;

    //国标路线名称
    private String roadName;

    //收费路段编号主键
    private String tollSectionId;

    //收费路段名称
    private String tollSectionName;

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

    public String getTollSectionId() {
        return tollSectionId;
    }

    public void setTollSectionId(String tollSectionId) {
        this.tollSectionId = tollSectionId;
    }

    public String getTollSectionName() {
        return tollSectionName;
    }

    public void setTollSectionName(String tollSectionName) {
        this.tollSectionName = tollSectionName;
    }

    @Override
    public String toString() {
        return "TollSectionDto{" +
                "roadId='" + roadId + '\'' +
                ", roadName='" + roadName + '\'' +
                ", tollSectionId='" + tollSectionId + '\'' +
                ", tollSectionName='" + tollSectionName + '\'' +
                '}';
    }
}
