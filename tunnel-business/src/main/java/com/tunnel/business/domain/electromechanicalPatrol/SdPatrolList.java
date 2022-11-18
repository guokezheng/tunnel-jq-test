package com.tunnel.business.domain.electromechanicalPatrol;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tunnel.business.domain.trafficOperationControl.eventManage.SdTrafficImage;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 巡查点清单对象 sd_patrol_list
 * 
 * @author ruoyi
 * @date 2022-11-04
 */
public class SdPatrolList<SdTrafficImage> extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private String id;

    /** 巡检任务id */
    @Excel(name = "巡检任务id")
    private String taskId;

    /** 设备id或故障清单表id */
    @Excel(name = "设备id或故障清单表id")
    private String eqFaultId;

    /** 巡检点类型（0：巡检点；1：故障点） */
    @Excel(name = "巡检点类型", readConverterExp = "0=：巡检点；1：故障点")
    private String patrolType;

    /** 设备或巡检点名称 */
    @Excel(name = "设备或巡检点名称")
    private String eqName;

    /** 安装位置 */
    @Excel(name = "安装位置")
    private String position;

    /** 外观情况(0:外观完整;2:外观破损) */
    @Excel(name = "外观情况(0:外观完整;2:外观破损)")
    private String impression;

    /** 网络通讯情况(0:通讯正常;1:通讯异常) */
    @Excel(name = "网络通讯情况(0:通讯正常;1:通讯异常)")
    private String network;

    /** 供配电情况(0:配电正常;1:配电异常) */
    @Excel(name = "供配电情况(0:配电正常;1:配电异常)")
    private String power;

    /** 设备状态 */
    @Excel(name = "设备状态")
    private String eqStatus;

    /** 运行状态 */
    @Excel(name = "运行状态")
    private String runStatus;

    /** 设备故障码（碰一碰检测） */
    @Excel(name = "设备故障码", readConverterExp = "碰=一碰检测")
    private String eqFaultCode;

    /** 设备故障描述 */
    @Excel(name = "设备故障描述")
    private String eqFaultDescription;

    /** 故障处理情况（0：无故障:；1：已消除:；2：未消除） */
    @Excel(name = "故障处理情况", readConverterExp = "0=：无故障:；1：已消除:；2：未消除")
    private String faultClstatus;

    /** 巡查时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "巡查时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date xcTime;

    /** 巡查顺序 */
    @Excel(name = "巡查顺序")
    private Integer xcSort;

    /** 巡查状态（0：未巡检；1：已巡检） */
    @Excel(name = "巡查状态", readConverterExp = "0=：未巡检；1：已巡检")
    private String xcStatus;

    /** 图片id */
    @Excel(name = "图片id")
    private String imgFileId;

    @ApiModelProperty("故障图片")
    private List<SdTrafficImage> iFileList;

    private String falltRemoveStatue;

    private String bzId;

    private String walkerId;

    private String tunnelId;

    private String tunnelName;


    public void setId(String id) 
    {
        this.id = id;
    }

    public String getId() 
    {
        return id;
    }
    public void setTaskId(String taskId) 
    {
        this.taskId = taskId;
    }

    public String getTaskId() 
    {
        return taskId;
    }
    public void setEqFaultId(String eqFaultId) 
    {
        this.eqFaultId = eqFaultId;
    }

    public String getEqFaultId() 
    {
        return eqFaultId;
    }
    public void setPatrolType(String patrolType)
    {
        this.patrolType = patrolType;
    }

    public String getPatrolType()
    {
        return patrolType;
    }
    public void setEqName(String eqName) 
    {
        this.eqName = eqName;
    }

    public String getEqName() 
    {
        return eqName;
    }
    public void setPosition(String position) 
    {
        this.position = position;
    }

    public String getPosition() 
    {
        return position;
    }
    public void setImpression(String impression)
    {
        this.impression = impression;
    }

    public String getImpression()
    {
        return impression;
    }
    public void setNetwork(String network)
    {
        this.network = network;
    }

    public String getNetwork()
    {
        return network;
    }
    public void setPower(String power)
    {
        this.power = power;
    }

    public String getPower()
    {
        return power;
    }
    public void setEqStatus(String eqStatus) 
    {
        this.eqStatus = eqStatus;
    }

    public String getEqStatus() 
    {
        return eqStatus;
    }
    public void setRunStatus(String runStatus) 
    {
        this.runStatus = runStatus;
    }

    public String getRunStatus() 
    {
        return runStatus;
    }
    public void setEqFaultCode(String eqFaultCode) 
    {
        this.eqFaultCode = eqFaultCode;
    }

    public String getEqFaultCode() 
    {
        return eqFaultCode;
    }
    public void setEqFaultDescription(String eqFaultDescription) 
    {
        this.eqFaultDescription = eqFaultDescription;
    }

    public String getEqFaultDescription() 
    {
        return eqFaultDescription;
    }
    public void setFaultClstatus(String faultClstatus)
    {
        this.faultClstatus = faultClstatus;
    }

    public String getFaultClstatus()
    {
        return faultClstatus;
    }
    public void setXcTime(Date xcTime) 
    {
        this.xcTime = xcTime;
    }

    public Date getXcTime() 
    {
        return xcTime;
    }
    public void setXcSort(Integer xcSort) 
    {
        this.xcSort = xcSort;
    }

    public Integer getXcSort() 
    {
        return xcSort;
    }
    public void setXcStatus(String xcStatus)
    {
        this.xcStatus = xcStatus;
    }

    public String getXcStatus()
    {
        return xcStatus;
    }
    public void setImgFileId(String imgFileId) 
    {
        this.imgFileId = imgFileId;
    }

    public String getImgFileId() 
    {
        return imgFileId;
    }

    public List<SdTrafficImage> getiFileList() {
        return iFileList;
    }

    public void setiFileList(List<SdTrafficImage> iFileList) {
        this.iFileList = iFileList;
    }

    public String getFalltRemoveStatue() {
        return this.falltRemoveStatue;
    }

    public void setFalltRemoveStatue(final String falltRemoveStatue) {
        this.falltRemoveStatue = falltRemoveStatue;
    }

    public String getBzId() {
        return this.bzId;
    }

    public void setBzId(final String bzId) {
        this.bzId = bzId;
    }

    public String getWalkerId() {
        return this.walkerId;
    }

    public void setWalkerId(final String walkerId) {
        this.walkerId = walkerId;
    }

    public String getTunnelId() {
        return this.tunnelId;
    }

    public void setTunnelId(final String tunnelId) {
        this.tunnelId = tunnelId;
    }

    public String getTunnelName() {
        return this.tunnelName;
    }

    public void setTunnelName(final String tunnelName) {
        this.tunnelName = tunnelName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("taskId", getTaskId())
            .append("eqFaultId", getEqFaultId())
            .append("patrolType", getPatrolType())
            .append("eqName", getEqName())
            .append("position", getPosition())
            .append("impression", getImpression())
            .append("network", getNetwork())
            .append("power", getPower())
            .append("eqStatus", getEqStatus())
            .append("runStatus", getRunStatus())
            .append("eqFaultCode", getEqFaultCode())
            .append("eqFaultDescription", getEqFaultDescription())
            .append("faultClstatus", getFaultClstatus())
            .append("xcTime", getXcTime())
            .append("createTime", getCreateTime())
            .append("xcSort", getXcSort())
            .append("xcStatus", getXcStatus())
            .append("imgFileId", getImgFileId())
            .append("createBy", getCreateBy())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
