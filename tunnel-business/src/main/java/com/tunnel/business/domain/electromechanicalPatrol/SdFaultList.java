package com.tunnel.business.domain.electromechanicalPatrol;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tunnel.business.domain.trafficOperationControl.eventManage.SdTrafficImage;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 故障清单对象 sd_fault_list
 * 
 * @author ruoyi
 * @date 2022-11-02
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SdFaultList extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 故障编号 */
    private String id;

    /** 隧道id */
    /*@Excel(name = "隧道id")*/
    private String tunnelId;

    /** 设备id */
    @Excel(name = "故障设备")
    private String eqName;

    /** 故障位置（设备的“方向”、“桩号”，拼接展示） */
    @Excel(name = "故障位置")
    private String faultLocation;

    private Integer fbState;

    /** 故障描述 */
    @Excel(name = "故障描述")
    private String faultDescription;

    /** 故障类型(0:自然损坏；1：腐蚀泡水；2：变形或断裂；3：间歇性故障；4：机械故障；5：人为损坏；6：其他) */
    /*@Excel(name = "故障类型")*/
    private String faultType;

    private String paramFaultType;

    private String  paramFaultEscalationType;


    private String paramFaultRemoveState;

    private String paramFaultLevel;

    private String deptId;

    /** 故障来源 */
   /* @Excel(name = "故障来源")*/
    private String faultSource;


    /** 故障发现时间 */
    @JsonFormat(timezone = "GMT+8",pattern="yyyy-MM-dd HH:mm:ss")
    @Excel(name = "发现时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date faultFxtime;




    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
   /* @Excel(name = "故障消除时间", width = 30, dateFormat = "yyyy-MM-dd")*/
    private Date faultRemoveTime;
    /** 故障持续时间（根据当前时间与故障发现时间计算时间差，单位：天、小时；计算时间差，不保存只展示；记录变为“已消除”状态时，保存持续时间） */
/*
    @Excel(name = "故障持续时间", readConverterExp = "根=据当前时间与故障发现时间计算时间差，单位：天、小时；计算时间差，不保存只展示；记录变为“已消除”状态时，保存持续时间")
*/
    private String faultCxtime;

    /** 故障填报人 */
   /* @Excel(name = "故障填报人")*/
    private String faultTbr;

    /** 故障填报时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    /*@Excel(name = "故障填报时间", width = 30, dateFormat = "yyyy-MM-dd")*/
    private Date faultTbtime;

    /** 设备id */
   /* @Excel(name = "设备id")*/
    private String eqId;

    private String typeName;
    private Integer typeId;


    /** 设备状态（1-在线，2-离线，3-故障） */
    /*@Excel(name = "设备状态")*/
    private String eqStatus;

    /** 设备运行状态 */
   /* @Excel(name = "设备运行状态")*/
    private String eqRunStatus;

    /** 故障代码 */
    /*@Excel(name = "故障代码")*/
    private String faultCode;
    @Excel(name = "设备状态")
    private String fstatus;


    /** 故障等级 */
    /*@Excel(name = "故障等级")*/
    private String faultLevel;

    /** 故障消除状态（0：已消除；1：未消除） */
    /*@Excel(name = "消除状态")*/
    private String falltRemoveStatue;

    @Excel(name = "消除状态")
    private String fremove;

    @Excel(name = "故障等级")
    private String flevel;
    @Excel(name = "故障类型")
    private String ftype;
    @Excel(name = "所属隧道")
    private String tunnelName;

    private String ids;

    public Integer getFbState() {
        return this.fbState;
    }

    public void setFbState( Integer fbState) {
        this.fbState = fbState;
    }

    public Integer getTypeId() {
        return this.typeId;
    }

    public void setTypeId( Integer typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return this.typeName;
    }

    public void setTypeName( String typeName) {
        this.typeName = typeName;
    }

    public String getParamFaultEscalationType() {
        return this.paramFaultEscalationType;
    }

    public void setParamFaultEscalationType( String paramFaultEscalationType) {
        this.paramFaultEscalationType = paramFaultEscalationType;
    }

    public String getParamFaultLevel() {
        return this.paramFaultLevel;
    }

    public void setParamFaultLevel(final String paramFaultLevel) {
        this.paramFaultLevel = paramFaultLevel;
    }

    public String getParamFaultRemoveState() {
        return this.paramFaultRemoveState;
    }

    public void setParamFaultRemoveState(final String paramFaultRemoveState) {
        this.paramFaultRemoveState = paramFaultRemoveState;
    }

    public String getParamFaultType() {
        return this.paramFaultType;
    }

    public void setParamFaultType(final String paramFaultType) {
        this.paramFaultType = paramFaultType;
    }

    public String getFstatus() {
        return this.fstatus;
    }

    public void setFstatus( String fstatus) {
        this.fstatus = fstatus;
    }

    public String getDeptId() {
        return this.deptId;
    }

    public void setDeptId( String deptId) {
        this.deptId = deptId;
    }

    public String getIds() {
        return this.ids;
    }

    public void setIds( String ids) {
        this.ids = ids;
    }

    public Date getFaultRemoveTime() {
        return this.faultRemoveTime;
    }

    public void setFaultRemoveTime( Date faultRemoveTime) {
        this.faultRemoveTime = faultRemoveTime;
    }

    public String getFremove() {
        return this.fremove;
    }

    public void setFremove(final String fremove) {
        this.fremove = fremove;
    }

    public String getFlevel() {
        return this.flevel;
    }

    public void setFlevel(final String flevel) {
        this.flevel = flevel;
    }

    public String getFtype() {
        return this.ftype;
    }

    public void setFtype(final String ftype) {
        this.ftype = ftype;
    }

    /** 状态（0：已发布；1：未发布） */
   @Excel(name = "发布状态", dictType = "fault_status")
    private String faultStatus;

    /** 创建者 */
   /* @ApiModelProperty("创建者")*/
    private String createBy;

    /** 创建时间 */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 更新者 */
    @ApiModelProperty("更新者")
    private String updateBy;

    /** 更新时间 */
    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**故障图片ID */
    @ApiModelProperty("故障图片ID")
   /* @Excel(name = "故障图片ID")*/
    private String imgFileId;

    @ApiModelProperty("默认图标")
    private List<SdTrafficImage> iFileList;

    @ApiModelProperty("上报区分")

    private String faultEscalationType;


    @Excel(name = "故障来源")
    private String fEscalationType;

    @ApiModelProperty("设备类型")
    private String eqType;

    public String getfEscalationType() {
        return this.fEscalationType;
    }

    public void setfEscalationType( String fEscalationType) {
        this.fEscalationType = fEscalationType;
    }

    public String getEqType() {
        return eqType;
    }

    public void setEqType(String eqType) {
        this.eqType = eqType;
    }

    public String getFaultEscalationType() {
        return faultEscalationType;
    }

    public void setFaultEscalationType(String faultEscalationType) {
        this.faultEscalationType = faultEscalationType;
    }

    public List<SdTrafficImage> getiFileList() {
        return this.iFileList;
    }

    public void setiFileList(final List<SdTrafficImage> iFileList) {
        this.iFileList = iFileList;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getId() 
    {
        return id;
    }
    public void setTunnelId(String tunnelId) 
    {
        this.tunnelId = tunnelId;
    }

    public String getTunnelId() 
    {
        return tunnelId;
    }
    public void setFaultLocation(String faultLocation) 
    {
        this.faultLocation = faultLocation;
    }

    public String getFaultLocation() 
    {
        return faultLocation;
    }
    public void setFaultType(String faultType)
    {
        this.faultType = faultType;
    }

    public String getFaultType()
    {
        return faultType;
    }
    public void setFaultSource(String faultSource) 
    {
        this.faultSource = faultSource;
    }

    public String getFaultSource() 
    {
        return faultSource;
    }
    public void setFaultFxtime(Date faultFxtime) 
    {
        this.faultFxtime = faultFxtime;
    }

    public Date getFaultFxtime() 
    {
        return faultFxtime;
    }
    public void setFaultCxtime(String faultCxtime) 
    {
        this.faultCxtime = faultCxtime;
    }

    public String getFaultCxtime() 
    {
        return faultCxtime;
    }
    public void setFaultTbr(String faultTbr) 
    {
        this.faultTbr = faultTbr;
    }

    public String getFaultTbr() 
    {
        return faultTbr;
    }
    public void setFaultTbtime(Date faultTbtime) 
    {
        this.faultTbtime = faultTbtime;
    }

    public Date getFaultTbtime() 
    {
        return faultTbtime;
    }
    public void setEqId(String eqId) 
    {
        this.eqId = eqId;
    }

    public String getEqRunStatus() {
        return this.eqRunStatus;
    }

    public void setEqRunStatus(final String eqRunStatus) {
        this.eqRunStatus = eqRunStatus;
    }

    public String getEqId()
    {
        return eqId;
    }
    public void setEqStatus(String eqStatus) 
    {
        this.eqStatus = eqStatus;
    }

    public String getEqStatus() 
    {
        return eqStatus;
    }
    public void setFaultCode(String faultCode) 
    {
        this.faultCode = faultCode;
    }

    public String getFaultCode() 
    {
        return faultCode;
    }
    public void setFaultLevel(String faultLevel)
    {
        this.faultLevel = faultLevel;
    }

    public String getFaultLevel()
    {
        return faultLevel;
    }
    public void setFalltRemoveStatue(String falltRemoveStatue)
    {
        this.falltRemoveStatue = falltRemoveStatue;
    }

    public String getFalltRemoveStatue()
    {
        return falltRemoveStatue;
    }
    public void setFaultDescription(String faultDescription) 
    {
        this.faultDescription = faultDescription;
    }

    public String getFaultDescription() 
    {
        return faultDescription;
    }
    public void setFaultStatus(String faultStatus)
    {
        this.faultStatus = faultStatus;
    }

    public String getFaultStatus()
    {
        return faultStatus;
    }

    @Override
    public String getCreateBy() {
        return this.createBy;
    }

    @Override
    public void setCreateBy(final String createBy) {
        this.createBy = createBy;
    }

    @Override
    public Date getCreateTime() {
        return this.createTime;
    }

    @Override
    public void setCreateTime(final Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String getUpdateBy() {
        return this.updateBy;
    }

    @Override
    public void setUpdateBy(final String updateBy) {
        this.updateBy = updateBy;
    }

    @Override
    public Date getUpdateTime() {
        return this.updateTime;
    }

    @Override
    public void setUpdateTime(final Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getImgFileId() {
        return this.imgFileId;
    }

    public void setImgFileId(final String imgFileId) {
        this.imgFileId = imgFileId;
    }

    public String getEqName() {
        return this.eqName;
    }

    public void setEqName(final String eqName) {
        this.eqName = eqName;
    }

    public String getTunnelName() {
        return tunnelName;
    }

    public void setTunnelName(String tunnelName) {
        this.tunnelName = tunnelName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("tunnelId", getTunnelId())
            .append("faultLocation", getFaultLocation())
            .append("faultType", getFaultType())
            .append("faultSource", getFaultSource())
            .append("faultFxtime", getFaultFxtime())
            .append("faultCxtime", getFaultCxtime())
            .append("faultTbr", getFaultTbr())
            .append("faultTbtime", getFaultTbtime())
            .append("eqId", getEqId())
            .append("eqStatus", getEqStatus())
            .append("faultCode", getFaultCode())
            .append("faultLevel", getFaultLevel())
            .append("falltRemoveStatue", getFalltRemoveStatue())
            .append("faultDescription", getFaultDescription())
            .append("faultStatus", getFaultStatus())
            .append("imgFileId", getImgFileId())
            .append("updateTime", getUpdateTime())
            .append("updateBy", getUpdateBy())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .toString();
    }
}
