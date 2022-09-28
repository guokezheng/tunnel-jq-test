package com.tunnel.webthings.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author dzy
 * @date 2022/8/22 17:10
 */
public class DeviceParameter extends BaseEntity {

    private static final long serialVersionUID =-1242493306307174690L;

    @ApiModelProperty("设备类型")
    private String devCategory;

    @ApiModelProperty("设备编号")
    private String devNo;

    @ApiModelProperty("设备类型")
    private String devType;

    @ApiModelProperty("集团公司编码")
    private String opmaCliqueId;

    @ApiModelProperty("管养公司编码")
    private String opmaManagerCropId;

    @ApiModelProperty("管养单位编码")
    private String opmaManagerId;

    @ApiModelProperty("道路编号")
    private String roadId; //如：G000337

    @ApiModelProperty("所属收费站")
    private String stationId;

    public String getDevCategory() {
        return devCategory;
    }

    public void setDevCategory(String devCategory) {
        this.devCategory = devCategory;
    }

    public String getDevNo() {
        return devNo;
    }

    public void setDevNo(String devNo) {
        this.devNo = devNo;
    }

    public String getDevType() {
        return devType;
    }

    public void setDevType(String devType) {
        this.devType = devType;
    }

    public String getOpmaCliqueId() {
        return opmaCliqueId;
    }

    public void setOpmaCliqueId(String opmaCliqueId) {
        this.opmaCliqueId = opmaCliqueId;
    }

    public String getOpmaManagerCropId() {
        return opmaManagerCropId;
    }

    public void setOpmaManagerCropId(String opmaManagerCropId) {
        this.opmaManagerCropId = opmaManagerCropId;
    }

    public String getOpmaManagerId() {
        return opmaManagerId;
    }

    public void setOpmaManagerId(String opmaManagerId) {
        this.opmaManagerId = opmaManagerId;
    }

    public String getRoadId() {
        return roadId;
    }

    public void setRoadId(String roadId) {
        this.roadId = roadId;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    @Override
    public String toString() {
        return "DeviceParameter{" +
                "devCategory='" + devCategory + '\'' +
                ", devNo='" + devNo + '\'' +
                ", devType='" + devType + '\'' +
                ", opmaCliqueId='" + opmaCliqueId + '\'' +
                ", opmaManagerCropId='" + opmaManagerCropId + '\'' +
                ", opmaManagerId='" + opmaManagerId + '\'' +
                ", roadId='" + roadId + '\'' +
                ", stationId='" + stationId + '\'' +
                '}';
    }
}
