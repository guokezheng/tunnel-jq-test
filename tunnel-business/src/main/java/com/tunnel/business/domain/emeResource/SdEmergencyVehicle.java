package com.tunnel.business.domain.emeResource;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 应急车辆对象 sd_emergency_vehicle
 * 
 * @author ruoyi
 * @date 2022-08-09
 */
public class SdEmergencyVehicle extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 应急机构id */
    @Excel(name = "应急机构id")
    private Long orgId;

    //机构名
    private String orgName;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    /** 车牌 */
    @Excel(name = "车牌")
    private String plateNumber;

    /** 车型 */
    @Excel(name = "车型")
    private String vType;

    /** 存放地点 */
    @Excel(name = "存放地点")
    private String vPlace;

    /** 使用状态(取字典值) */
    @Excel(name = "使用状态(取字典值)")
    private String useStatus;

    /** 车载终端安装 */
    @Excel(name = "车载终端安装")
    private String terminalInstall;

    /** 技术状态描述 */
    @Excel(name = "技术状态描述")
    private String statusDesc;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setOrgId(Long orgId) 
    {
        this.orgId = orgId;
    }

    public Long getOrgId() 
    {
        return orgId;
    }
    public void setPlateNumber(String plateNumber) 
    {
        this.plateNumber = plateNumber;
    }

    public String getPlateNumber() 
    {
        return plateNumber;
    }
    public void setvType(String vType) 
    {
        this.vType = vType;
    }

    public String getvType() 
    {
        return vType;
    }
    public void setvPlace(String vPlace) 
    {
        this.vPlace = vPlace;
    }

    public String getvPlace() 
    {
        return vPlace;
    }
    public void setUseStatus(String useStatus) 
    {
        this.useStatus = useStatus;
    }

    public String getUseStatus() 
    {
        return useStatus;
    }
    public void setTerminalInstall(String terminalInstall) 
    {
        this.terminalInstall = terminalInstall;
    }

    public String getTerminalInstall() 
    {
        return terminalInstall;
    }
    public void setStatusDesc(String statusDesc) 
    {
        this.statusDesc = statusDesc;
    }

    public String getStatusDesc() 
    {
        return statusDesc;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("orgId", getOrgId())
            .append("plateNumber", getPlateNumber())
            .append("vType", getvType())
            .append("vPlace", getvPlace())
            .append("useStatus", getUseStatus())
            .append("terminalInstall", getTerminalInstall())
            .append("statusDesc", getStatusDesc())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
