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
    private String orgId;

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

    /**
     * 车辆运行状态
     */
    private String accState;

    /**
     * 资产归属
     */
    private String ownerName;

    /**
     * 车辆型号
     */
    private String vehicleModel;

    /**
     * ETC使用情况名称
     */
    private String etcStateDesc;

    /**
     * ETC卡类型名称
     */
    private String etcTypeDesc;

    /**
     * 车龄
     */
    private String carAge;

    /**
     * 公里数
     */
    private String mileage;

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public String getEtcStateDesc() {
        return etcStateDesc;
    }

    public void setEtcStateDesc(String etcStateDesc) {
        this.etcStateDesc = etcStateDesc;
    }

    public String getEtcTypeDesc() {
        return etcTypeDesc;
    }

    public void setEtcTypeDesc(String etcTypeDesc) {
        this.etcTypeDesc = etcTypeDesc;
    }

    public String getCarAge() {
        return carAge;
    }

    public void setCarAge(String carAge) {
        this.carAge = carAge;
    }

    public String getMileage() {
        return mileage;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getAccState() {
        return accState;
    }

    public void setAccState(String accState) {
        this.accState = accState;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setOrgId(String orgId)
    {
        this.orgId = orgId;
    }

    public String getOrgId()
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
