package com.tunnel.business.domain.informationBoard;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excels;
import com.ruoyi.common.core.domain.BaseEntity;
import com.sun.jna.NativeLong;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 设备列表对象 iot_device
 *
 * @author yangqichao
 * @date 2020-03-25
 */
public class SdIotDevice extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 设备编号 */
    @Excel(name = "设备编号")
    private Long deviceId;

    /** 设备名称 */
    @Excel(name = "设备名称")
    private String deviceName;

    /** 品牌id */
    @Excel(name = "品牌id")
    private Long brandId;
    /** 品牌列表对象 */
    @Excels({
        @Excel(name = "iotBrandName", targetAttr = "iotBrandName"),
    })
    private IotBrand iotBrandName;
	/**
     * 设备类型对象
     */
    @Excels({
        @Excel(name = "IotDeviceType", targetAttr = "IotDeviceType"),
    })
    private IotDeviceType IotDeviceType;
    public IotDeviceType getIotDeviceType() {
		return IotDeviceType;
	}
	public void setIotDeviceType(IotDeviceType iotDeviceType) {
		IotDeviceType = iotDeviceType;
	}

	/** 线路id */
    @Excel(name = "线路id")
    private Long routeId;

    /** 路段方向 */
    @Excel(name = "路段方向")
    private String routeDirection;

    /** 设备标识名称 */
    @Excel(name = "设备标识名称")
    private String deviceMarkingName;

    /** 设备类型 */
    @Excel(name = "设备类型")
    private Long deviceTypeId;

    /** 设备类型 */
    @Excel(name = "详细设备类型")
    private Long deviceTypeNumber;

    /** 设备型号 */
    @Excel(name = "设备型号")
    private Long deviceModelId;

    /** 所属仓库 */
    @Excel(name = "所属仓库")
    private String factoryLibrary;

    /** 使用单位 */
    @Excel(name = "使用单位")
    private Long userUnitId;

    /** 经办人 */
    @Excel(name = "经办人")
    private Long operatorId;

    /** 经度 */
    @Excel(name = "经度")
    private String longitude;

    /** 纬度 */
    @Excel(name = "纬度")
    private String latitude;

    /** 生产商 */
    @Excel(name = "生产商")
    private Long firmId;

    /** 购买日期 */
    @Excel(name = "购买日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date purchaseDate;

    /** 保修年限 */
    @Excel(name = "保修年限")
    private Integer warrantyYears;

    /** 单价 */
    @Excel(name = "单价")
    private Long unitPrice;

    /** 运行状态 */
    @Excel(name = "运行状态")
    private Integer runStatus;

    /** 设备状态 */
    @Excel(name = "隧道ID")
    private String tunnelId;

    /** 设备状态 */
    @Excel(name = "设备状态")
    private String deviceStatus;

    /** 安装时间 */
    @Excel(name = "安装时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date installDate;

    /** 维护商 */
    @Excel(name = "维护商")
    private Long maintainId;

    /** 领用机构 */
    @Excel(name = "领用机构")
    private Long collarAgencyId;

    /** 桩号 */
    @Excel(name = "桩号")
    private String pileNumber;

    /** 管理单位 */
    @Excel(name = "管理单位")
    private Long manageAgencyId;

    /** 生产日期 */
    @Excel(name = "生产日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date producteDate;

    /** 保修到期日 */
    @Excel(name = "保修到期日", width = 30, dateFormat = "yyyy-MM-dd")
    private Date repairDate;

    /** 操作日期 */
    @Excel(name = "操作日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date operateDate;

    /** 使用年限 */
    @Excel(name = "使用年限")
    private Integer serviceLife;

    /** 入库时间 */
    @Excel(name = "入库时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date storageDate;

    /** 是否监控 */
    @Excel(name = "是否监控")
    private Integer isMonitor;
    /** 是否配置*/
    @Excel(name = "是否配置")
    private Integer isConfig;

    /** 位置信息 */
    @Excel(name = "位置信息")
    private Integer localInfo;

    /** 路段编号 */
    private String routeNumber;

    /** 路段名称 */
    private String routeName;

    /** 设备IP */
    private String deviceIp;

    /** 设备端口 */
    private String devicePort;
    /*设备分辨率*/
    private String devicePixel;
    /** 用户名 */
    private String userName;

    /** 密码 */
    private String passWord;

    /** 协议版本 */
    private String protocolName;


    /** 协议版本 */
    private String deviceTypeIds;

    private NativeLong lUserID;

    private NativeLong lAlarmHandle;//报警布防句柄

    private String lUserIDStr;

    private String lAlarmHandleStr;

    public String getRouteNumber() {
        return routeNumber;
    }
    public void setDeviceId(Long deviceId)
    {
        this.deviceId = deviceId;
    }

    public String getRouteName() {
        return routeName;
    }
    public void setRouteNumber(String routeNumber) {
        this.routeNumber = routeNumber;
    }

    public String getDeviceIp() {
        return deviceIp;
    }
    public void setDeviceIp(String deviceIp) {
        this.deviceIp = deviceIp;
    }

    public String getDevicePort() {
        return devicePort;
    }
    public void setDevicePort(String devicePort) {
        this.devicePort = devicePort;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getProtocolName() {
        return protocolName;
    }
    public void setProtocolName(String protocolName) {
        this.protocolName = protocolName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public Long getDeviceId()
    {
        return deviceId;
    }
    public void setDeviceName(String deviceName)
    {
        this.deviceName = deviceName;
    }

    public String getDeviceName()
    {
        return deviceName;
    }
    public void setBrandId(Long brandId)
    {
        this.brandId = brandId;
    }

    public Long getBrandId()
    {
        return brandId;
    }
    public IotBrand getIotBrandName() {
		return iotBrandName;
	}
	public void setIotBrandName(IotBrand iotBrandName) {
		this.iotBrandName = iotBrandName;
	}
    public void setRouteId(Long routeId)
    {
        this.routeId = routeId;
    }

    public Long getRouteId()
    {
        return routeId;
    }
    public void setRouteDirection(String routeDirection)
    {
        this.routeDirection = routeDirection;
    }

    public String getRouteDirection()
    {
        return routeDirection;
    }
    public void setDeviceMarkingName(String deviceMarkingName)
    {
        this.deviceMarkingName = deviceMarkingName;
    }

    public String getDeviceMarkingName()
    {
        return deviceMarkingName;
    }
    public void setDeviceTypeId(Long deviceTypeId)
    {
        this.deviceTypeId = deviceTypeId;
    }

    public Long getDeviceTypeId()
    {
        return deviceTypeId;
    }
    public void setDeviceTypeNumber(Long deviceTypeNumber) {
        this.deviceTypeNumber = deviceTypeNumber;
    }

    public Long getDeviceTypeNumber() {
        return deviceTypeNumber;
    }
    public void setDeviceModelId(Long deviceModelId)
    {
        this.deviceModelId = deviceModelId;
    }

    public Long getDeviceModelId()
    {
        return deviceModelId;
    }
    public void setFactoryLibrary(String factoryLibrary)
    {
        this.factoryLibrary = factoryLibrary;
    }

    public String getFactoryLibrary()
    {
        return factoryLibrary;
    }
    public void setUserUnitId(Long userUnitId)
    {
        this.userUnitId = userUnitId;
    }

    public Long getUserUnitId()
    {
        return userUnitId;
    }
    public void setOperatorId(Long operatorId)
    {
        this.operatorId = operatorId;
    }

    public Long getOperatorId()
    {
        return operatorId;
    }
    public void setLongitude(String longitude)
    {
        this.longitude = longitude;
    }

    public String getLongitude()
    {
        return longitude;
    }
    public void setLatitude(String latitude)
    {
        this.latitude = latitude;
    }

    public String getLatitude()
    {
        return latitude;
    }
    public void setFirmId(Long firmId)
    {
        this.firmId = firmId;
    }

    public Long getFirmId()
    {
        return firmId;
    }
    public void setPurchaseDate(Date purchaseDate)
    {
        this.purchaseDate = purchaseDate;
    }

    public Date getPurchaseDate()
    {
        return purchaseDate;
    }
    public void setWarrantyYears(Integer warrantyYears)
    {
        this.warrantyYears = warrantyYears;
    }

    public Integer getWarrantyYears()
    {
        return warrantyYears;
    }
    public void setUnitPrice(Long unitPrice)
    {
        this.unitPrice = unitPrice;
    }

    public Long getUnitPrice()
    {
        return unitPrice;
    }
    public void setDeviceStatus(String deviceStatus)
    {
        this.deviceStatus = deviceStatus;
    }

    public Integer getRunStatus() {
        return runStatus;
    }

    public void setRunStatus(Integer runStatus) {
        this.runStatus = runStatus;
    }

    public String getDeviceStatus()
    {
        return deviceStatus;
    }
    public void setInstallDate(Date installDate)
    {
        this.installDate = installDate;
    }

    public Date getInstallDate()
    {
        return installDate;
    }
    public void setMaintainId(Long maintainId)
    {
        this.maintainId = maintainId;
    }

    public Long getMaintainId()
    {
        return maintainId;
    }
    public void setCollarAgencyId(Long collarAgencyId)
    {
        this.collarAgencyId = collarAgencyId;
    }

    public Long getCollarAgencyId()
    {
        return collarAgencyId;
    }
    public void setPileNumber(String pileNumber)
    {
        this.pileNumber = pileNumber;
    }

    public String getPileNumber()
    {
        return pileNumber;
    }
    public void setManageAgencyId(Long manageAgencyId)
    {
        this.manageAgencyId = manageAgencyId;
    }

    public Long getManageAgencyId()
    {
        return manageAgencyId;
    }
    public void setProducteDate(Date producteDate)
    {
        this.producteDate = producteDate;
    }

    public Date getProducteDate()
    {
        return producteDate;
    }
    public void setRepairDate(Date repairDate)
    {
        this.repairDate = repairDate;
    }

    public Date getRepairDate()
    {
        return repairDate;
    }
    public void setOperateDate(Date operateDate)
    {
        this.operateDate = operateDate;
    }

    public Date getOperateDate()
    {
        return operateDate;
    }
    public void setServiceLife(Integer serviceLife)
    {
        this.serviceLife = serviceLife;
    }

    public Integer getServiceLife()
    {
        return serviceLife;
    }
    public void setStorageDate(Date storageDate)
    {
        this.storageDate = storageDate;
    }

    public Date getStorageDate()
    {
        return storageDate;
    }
    public void setIsMonitor(Integer isMonitor)
    {
        this.isMonitor = isMonitor;
    }

    public Integer getIsMonitor()
    {
        return isMonitor;
    }
    public void setLocalInfo(Integer localInfo)
    {
        this.localInfo = localInfo;
    }

    public Integer getLocalInfo()
    {
        return localInfo;
    }

    public String getDeviceTypeIds() {
        return deviceTypeIds;
    }

    public void setDeviceTypeIds(String deviceTypeIds) {
        this.deviceTypeIds = deviceTypeIds;
    }

    public Integer getIsConfig() {
        return isConfig;
    }

    public void setIsConfig(Integer isConfig) {
        this.isConfig = isConfig;
    }

    public String getDevicePixel() {
        return devicePixel;
    }

    public void setDevicePixel(String devicePixel) {
        this.devicePixel = devicePixel;
    }

    public NativeLong getlUserID() {
        return lUserID;
    }

    public void setlUserID(NativeLong lUserID) {
        this.lUserID = lUserID;
    }

    public NativeLong getlAlarmHandle() {
        return lAlarmHandle;
    }

    public void setlAlarmHandle(NativeLong lAlarmHandle) {
        this.lAlarmHandle = lAlarmHandle;
    }

    public String getlUserIDStr() {
        return lUserIDStr;
    }

    public void setlUserIDStr(String lUserIDStr) {
        this.lUserIDStr = lUserIDStr;
    }

    public String getlAlarmHandleStr() {
        return lAlarmHandleStr;
    }

    public void setlAlarmHandleStr(String lAlarmHandleStr) {
        this.lAlarmHandleStr = lAlarmHandleStr;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("deviceId", getDeviceId())
            .append("deviceName", getDeviceName())
            .append("brandId", getBrandId())
            .append("iotBrandName", getIotBrandName())
            .append("iotDeviceType", getIotDeviceType())
            .append("routeId", getRouteId())
            .append("routeDirection", getRouteDirection())
            .append("deviceMarkingName", getDeviceMarkingName())
            .append("deviceTypeId", getDeviceTypeId())
            .append("deviceModelId", getDeviceModelId())
            .append("factoryLibrary", getFactoryLibrary())
            .append("userUnitId", getUserUnitId())
            .append("operatorId", getOperatorId())
            .append("longitude", getLongitude())
            .append("latitude", getLatitude())
            .append("firmId", getFirmId())
            .append("purchaseDate", getPurchaseDate())
            .append("warrantyYears", getWarrantyYears())
            .append("unitPrice", getUnitPrice())
            .append("deviceStatus", getDeviceStatus())
            .append("installDate", getInstallDate())
            .append("maintainId", getMaintainId())
            .append("collarAgencyId", getCollarAgencyId())
            .append("pileNumber", getPileNumber())
            .append("manageAgencyId", getManageAgencyId())
            .append("producteDate", getProducteDate())
            .append("repairDate", getRepairDate())
            .append("operateDate", getOperateDate())
            .append("serviceLife", getServiceLife())
            .append("storageDate", getStorageDate())
            .append("isMonitor", getIsMonitor())
            .append("localInfo", getLocalInfo())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .append("deviceIp", getDeviceIp())
            .append("devicePort", getDevicePort())
            .append("devicePixel", getDevicePixel())
            .append("routeName", getRouteName())
            .append("routeNumber", getRouteNumber())
            .append("userName", getUserName())
            .append("passWord", getPassWord())
            .append("isConfig", getIsConfig())
            .append("lUserID", getlUserID())
            .append("lAlarmHandle", getlAlarmHandle())
            .append("lUserIDStr", getlUserIDStr())
            .append("lAlarmHandleStr", getlAlarmHandleStr())
            .toString();
    }

    public String getTunnelId() {
        return tunnelId;
    }

    public void setTunnelId(String tunnelId) {
        this.tunnelId = tunnelId;
    }
}
