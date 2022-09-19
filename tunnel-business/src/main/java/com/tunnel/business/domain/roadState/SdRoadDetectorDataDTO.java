package com.tunnel.business.domain.roadState;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ruoyi.common.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 路面监测信息对象 sd_road_detector_data
 *
 * @author lihaodong
 * @date 2021-11-17
 */
public class SdRoadDetectorDataDTO extends SdRoadDetectorData
{

    private static final long serialVersionUID = 1L;

    /** 设备名称 */
    @Excel(name = "设备名称", sort = 1)
    private String eqName;

    /** 方向 */
    @Excel(name = "设备方向", sort = 3, readConverterExp = "0=下行,1=上行")
    private String eqDirection;

    /** 桩号 */
    @Excel(name = "设备位置", sort = 4)
    private String stakeMark;

    /** 隧道名称 */
    @Excel(name = "所属隧道", sort = 2)
    private String tunnelName;

    /** 隧道编号 */
    //@Excel(name = "隧道编号")
    private String tunnelCode;

    /** 隧道地址 */
    //@Excel(name = "隧道地址")
    private String tunnelAddress;

    /** 状态（统计） **/
    @JsonIgnore
    private Integer status;

    /** 隧道ID */
    private String plcTunnelId;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getPlcTunnelId() {
        return plcTunnelId;
    }

    public void setPlcTunnelId(String plcTunnelId) {
        this.plcTunnelId = plcTunnelId;
    }

    public String getEqName() {
        return eqName;
    }

    public void setEqName(String eqName) {
        this.eqName = eqName;
    }

    public String getEqDirection() {
        return eqDirection;
    }

    public void setEqDirection(String eqDirection) {
        this.eqDirection = eqDirection;
    }

    public String getStakeMark() {
        return stakeMark;
    }

    public void setStakeMark(String stakeMark) {
        this.stakeMark = stakeMark;
    }

    public String getTunnelName() {
        return tunnelName;
    }

    public void setTunnelName(String tunnelName) {
        this.tunnelName = tunnelName;
    }

    public String getTunnelCode() {
        return tunnelCode;
    }

    public void setTunnelCode(String tunnelCode) {
        this.tunnelCode = tunnelCode;
    }

    public String getTunnelAddress() {
        return tunnelAddress;
    }

    public void setTunnelAddress(String tunnelAddress) {
        this.tunnelAddress = tunnelAddress;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("devno", getDevno())
                .append("pavementtemp", getPavementtemp())
                .append("icepointtemp", getIcepointtemp())
                .append("pavementstatus", getPavementstatus())
                .append("frictionalcoe", getFrictionalcoe())
                .append("waterfilmheigh", getWaterfilmheigh())
                .append("waterthickness", getWaterthickness())
                .append("snowthickness", getSnowthickness())
                .append("icethickness", getIcethickness())
                .append("saltnessnacl", getSaltnessnacl())
                .append("saltnessmgcl2", getSaltnessmgcl2())
                .append("saltnesscacl2", getSaltnesscacl2())
                .append("icepercent", getIcepercent())
                .append("icepossibiblity", getIcepossibiblity())
                .append("timeCollect", getTimeCollect())
                .append("timeUpdate", getTimeUpdate())
                .append("eqName", getEqName())
                .append("eqDirection", getEqDirection())
                .append("stakeMark", getStakeMark())
                .append("tunnelName", getTunnelName())
                .append("tunnelCode", getTunnelCode())
                .append("tunnelAddress", getTunnelAddress())
                .toString();
    }

}
