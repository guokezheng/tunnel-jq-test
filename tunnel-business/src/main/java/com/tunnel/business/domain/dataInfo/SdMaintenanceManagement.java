package com.tunnel.business.domain.dataInfo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.util.List;

/**
 * 养护管理对象 sd_maintenance_management
 *
 * @author ruoyi
 * @date 2022-02-14
 */
public class SdMaintenanceManagement extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 养护人员 */
    @Excel(name = "养护人员")
    private String maintenancePerson;

    /** 隧道id */
    @Excel(name = "隧道id")
    private String tunnelId;

    /** 位置信息 */
    @Excel(name = "位置信息")
    private String maintenanceLocation;

    /** 联系方式 */
    @Excel(name = "联系方式")
    private String phone;

    /** 养护内容 */
    @Excel(name = "养护内容")
    private String maintenanceInformation;

    /** 养护进度 */
    @Excel(name = "养护进度")
    private String curingProgress;

    /** 备注 */
    @Excel(name = "备注")
    private String remake;

    /** 备注1 */
    @Excel(name = "备注1")
    private String remake1;

    /**
     * 隧道名
     */
    private String tunnelName;

    private List<SdMaintenanceManagement> pFileList;

    private String pFileId;

    private List<SdMaintenanceManagementFile> fileLists;

    public List<SdMaintenanceManagementFile> getFileLists() {
        return fileLists;
    }

    public void setFileLists(List<SdMaintenanceManagementFile> fileLists) {
        this.fileLists = fileLists;
    }

    public String getpFileId() {
        return pFileId;
    }

    public void setpFileId(String pFileId) {
        this.pFileId = pFileId;
    }

    public List<SdMaintenanceManagement> getpFileList() {
        return pFileList;
    }

    public void setpFileList(List<SdMaintenanceManagement> pFileList) {
        this.pFileList = pFileList;
    }

    public String getTunnelName() {
        return tunnelName;
    }

    public void setTunnelName(String tunnelName) {
        this.tunnelName = tunnelName;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setMaintenancePerson(String maintenancePerson)
    {
        this.maintenancePerson = maintenancePerson;
    }

    public String getMaintenancePerson()
    {
        return maintenancePerson;
    }
    public void setTunnelId(String tunnelId)
    {
        this.tunnelId = tunnelId;
    }

    public String getTunnelId()
    {
        return tunnelId;
    }
    public void setMaintenanceLocation(String maintenanceLocation)
    {
        this.maintenanceLocation = maintenanceLocation;
    }

    public String getMaintenanceLocation()
    {
        return maintenanceLocation;
    }
    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getPhone()
    {
        return phone;
    }
    public void setMaintenanceInformation(String maintenanceInformation)
    {
        this.maintenanceInformation = maintenanceInformation;
    }

    public String getMaintenanceInformation()
    {
        return maintenanceInformation;
    }
    public void setCuringProgress(String curingProgress)
    {
        this.curingProgress = curingProgress;
    }

    public String getCuringProgress()
    {
        return curingProgress;
    }
    public void setRemake(String remake)
    {
        this.remake = remake;
    }

    public String getRemake()
    {
        return remake;
    }
    public void setRemake1(String remake1)
    {
        this.remake1 = remake1;
    }

    public String getRemake1()
    {
        return remake1;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("maintenancePerson", getMaintenancePerson())
            .append("tunnelId", getTunnelId())
            .append("maintenanceLocation", getMaintenanceLocation())
            .append("phone", getPhone())
            .append("maintenanceInformation", getMaintenanceInformation())
            .append("curingProgress", getCuringProgress())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .append("updateTime", getUpdateTime())
            .append("updateBy", getUpdateBy())
            .append("remake", getRemake())
            .append("remake1", getRemake1())
            .append("pFileList", getpFileList())
            .append("pFileId", getpFileId())
            .append("fileLists", getFileLists())
            .toString();
    }
}
