package com.tunnel.business.domain.dataInfo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

/**
 * 巡视记录对象 sd_inspection
 *
 * @author liubaokui
 * @date 2021-05-12
 */
public class SdInspection extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 巡视id */
    private Long inspectionId;

    /** 巡视人员 */
    @Excel(name = "巡视人员")
    private String inspectionPerson;

    /** 巡视位置 */
    @Excel(name = "巡视位置")
    private String inspectionPosition;

    /** 检修时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "巡视时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date inspectionTime;

    /** 隧道名称 */
    @Excel(name = "所属隧道")
    private String tunnelName;

    /** 所属隧道 */
//    @Excel(name = "所属隧道")
    private String inspectionTunnel;


    /** 发现问题 */
    @Excel(name = "发现问题")
    private String identifyProblem;

    /** 处理方法 */
    @Excel(name = "处理方法")
    private String resolveProblem;

    /** 巡视内容 */
    @Excel(name = "巡视内容")
    private String inspectionContent;

    /** 创建者 */
    private String createName;

    /** 修改人 */
    private String updateName;

    /**
     * 是否维修（1：需要；0不需要）
     */
    @Excel(name = "是否维修",readConverterExp = "0=否,1=是")
    private int isRepair;

    /**
     * 维修人员
     */
    @Excel(name = "维修人员")
    private String repairPerson;

    /**
     * 维修人员联系电话
     */
    @Excel(name = "联系方式")
    private String phone;

    /**
     * 维修详情
     */
    @Excel(name = "维修详情")
    private String repairDetail;

    /** 设备ID */
    private String eqId;

    /** 巡检进行状态(1：未开始;2：进行中;3：已结束) */
    private String state;

    /** 巡检记录文件ID */
    private String planFileId;

    /** 巡检结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date inspectionEndTime;

    /** 备注 */
    @Excel(name = "备注")
    private String inspectionRemark;

    private List<SdInspectionFile> pFileList;

    private MultipartFile[] files;

    /** 拍摄位置 */
    private String position;

    /** 情况描述 */
    private String situationDescription;

    private List<SdInspectionFile> fileLists;

    public List<SdInspectionFile> getFileLists() {
        return fileLists;
    }

    public void setFileLists(List<SdInspectionFile> fileLists) {
        this.fileLists = fileLists;
    }

    public String getSituationDescription() {
        return situationDescription;
    }

    public void setSituationDescription(String situationDescription) {
        this.situationDescription = situationDescription;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public MultipartFile[] getFiles() {
        return files;
    }

    public void setFiles(MultipartFile[] files) {
        this.files = files;
    }

    public List<SdInspectionFile> getpFileList() {
        return pFileList;
    }

    public void setpFileList(List<SdInspectionFile> pFileList) {
        this.pFileList = pFileList;
    }

    public void setPlanFileId(String planFileId)
    {
        this.planFileId = planFileId;
    }

    public String getPlanFileId()
    {
        return planFileId;
    }

    public String getEqId() {
        return eqId;
    }

    public void setEqId(String eqId) {
        this.eqId = eqId;
    }

    public String getTunnelName() {
        return tunnelName;
    }

    public void setTunnelName(String tunnelName) {
        this.tunnelName = tunnelName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Date getInspectionEndTime() {
        return inspectionEndTime;
    }

    public void setInspectionEndTime(Date inspectionEndTime) {
        this.inspectionEndTime = inspectionEndTime;
    }

    public int getIsRepair() {
        return isRepair;
    }

    public void setIsRepair(int isRepair) {
        this.isRepair = isRepair;
    }

    public String getRepairPerson() {
        return repairPerson;
    }

    public void setRepairPerson(String repairPerson) {
        this.repairPerson = repairPerson;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRepairDetail() {
        return repairDetail;
    }

    public void setRepairDetail(String repairDetail) {
        this.repairDetail = repairDetail;
    }

    public void setInspectionId(Long inspectionId)
    {
        this.inspectionId = inspectionId;
    }

    public Long getInspectionId()
    {
        return inspectionId;
    }

    public void setInspectionPerson(String inspectionPerson)
    {
        this.inspectionPerson = inspectionPerson;
    }

    public String getInspectionPerson()
    {
        return inspectionPerson;
    }
    public void setInspectionPosition(String inspectionPosition)
    {
        this.inspectionPosition = inspectionPosition;
    }

    public Date getInspectionTime() {
		return inspectionTime;
	}

	public void setInspectionTime(Date inspectionTime) {
		this.inspectionTime = inspectionTime;
	}


    public String getInspectionPosition()
    {
        return inspectionPosition;
    }
    public void setInspectionTunnel(String inspectionTunnel)
    {
        this.inspectionTunnel = inspectionTunnel;
    }

    public String getInspectionTunnel()
    {
        return inspectionTunnel;
    }
    public void setInspectionRemark(String inspectionRemark)
    {
        this.inspectionRemark = inspectionRemark;
    }

    public String getInspectionRemark()
    {
        return inspectionRemark;
    }
    public void setIdentifyProblem(String identifyProblem)
    {
        this.identifyProblem = identifyProblem;
    }

    public String getIdentifyProblem()
    {
        return identifyProblem;
    }
    public void setResolveProblem(String resolveProblem)
    {
        this.resolveProblem = resolveProblem;
    }

    public String getResolveProblem()
    {
        return resolveProblem;
    }
    public void setInspectionContent(String inspectionContent)
    {
        this.inspectionContent = inspectionContent;
    }

    public String getInspectionContent()
    {
        return inspectionContent;
    }
    public void setCreateName(String createName)
    {
        this.createName = createName;
    }

    public String getCreateName()
    {
        return createName;
    }
    public void setUpdateName(String updateName)
    {
        this.updateName = updateName;
    }

    public String getUpdateName()
    {
        return updateName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("inspectionId", getInspectionId())
            .append("inspectionPerson", getInspectionPerson())
            .append("inspectionPosition", getInspectionPosition())
            .append("tunnelName", getTunnelName())
            .append("inspectionTunnel", getInspectionTunnel())
            .append("inspectionRemark", getInspectionRemark())
            .append("identifyProblem", getIdentifyProblem())
            .append("resolveProblem", getResolveProblem())
            .append("inspectionContent", getInspectionContent())
            .append("createTime", getCreateTime())
            .append("createName", getCreateName())
            .append("updateTime", getUpdateTime())
            .append("updateName", getUpdateName())
            .append("isRepair", getIsRepair())
            .append("repairPerson", getRepairPerson())
            .append("phone", getPhone())
            .append("repairDetail", getRepairDetail())
            .append("eqId", getEqId())
            .append("state", getState())
            .append("inspectionEndTime", getInspectionEndTime())
            .append("planFileId", getPlanFileId())
            .append("pFileList", getpFileList())
            .append("position", getPosition())
            .append("situationDescription", getSituationDescription())
            .toString();
    }
}
