package com.tunnel.business.domain.protocol;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 设备点位状态详情对象 sd_device_point
 *
 * @author ruoyi
 * @date 2023-02-27
 */
public class SdDevicePoint extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 设备id */
    @Excel(name = "设备id")
    private String eqId;

    /** 设备类型 */
    @Excel(name = "设备类型")
    private Long eqType;

    private Long itemId;

    /** 寄存器点位 */
    @Excel(name = "寄存器点位")
    private String address;

    /** 二进制点 */
    @Excel(name = "二进制点")
    private Long addressIndex;

    /** 功能码 */
    @Excel(name = "功能码")
    private String functionCode;

    /** 数据长度（字节数） */
    @Excel(name = "数据长度（字节数）")
    private String dataLength;

    /** 功能 */
    @Excel(name = "功能")
    private String functionName;

    /** 数据类型 */
    @Excel(name = "数据类型")
    private String dataType;

    /** 信号名称 */
    @Excel(name = "信号名称")
    private String signalName;

    /** 设备状态 */
    @Excel(name = "设备状态")
    private Long stateId;

    /** 数据状态 */
    @Excel(name = "数据状态")
    private String dataStatus;

    /** 功能描述 */
    @Excel(name = "功能描述")
    private String functionDescription;

    /** 是否预留 */
    @Excel(name = "是否预留")
    private Long isReserved;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setEqId(String eqId)
    {
        this.eqId = eqId;
    }

    public String getEqId()
    {
        return eqId;
    }
    public void setEqType(Long eqType)
    {
        this.eqType = eqType;
    }

    public Long getEqType()
    {
        return eqType;
    }
    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getAddress()
    {
        return address;
    }
    public void setAddressIndex(Long addressIndex)
    {
        this.addressIndex = addressIndex;
    }

    public Long getAddressIndex()
    {
        return addressIndex;
    }
    public void setFunctionName(String functionName)
    {
        this.functionName = functionName;
    }

    public String getFunctionCode() {
        return functionCode;
    }

    public void setFunctionCode(String functionCode) {
        this.functionCode = functionCode;
    }

    public String getDataLength() {
        return dataLength;
    }

    public void setDataLength(String dataLength) {
        this.dataLength = dataLength;
    }

    public String getFunctionName()
    {
        return functionName;
    }
    public void setDataType(String dataType)
    {
        this.dataType = dataType;
    }

    public String getDataType()
    {
        return dataType;
    }
    public void setSignalName(String signalName)
    {
        this.signalName = signalName;
    }

    public String getSignalName()
    {
        return signalName;
    }
    public void setStateId(Long stateId)
    {
        this.stateId = stateId;
    }

    public Long getStateId()
    {
        return stateId;
    }
    public void setDataStatus(String dataStatus)
    {
        this.dataStatus = dataStatus;
    }

    public String getDataStatus()
    {
        return dataStatus;
    }
    public void setFunctionDescription(String functionDescription)
    {
        this.functionDescription = functionDescription;
    }

    public String getFunctionDescription()
    {
        return functionDescription;
    }
    public void setIsReserved(Long isReserved)
    {
        this.isReserved = isReserved;
    }

    public Long getIsReserved()
    {
        return isReserved;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("eqId", getEqId())
            .append("eqType", getEqType())
            .append("address", getAddress())
            .append("addressIndex", getAddressIndex())
            .append("functionName", getFunctionName())
            .append("dataType", getDataType())
            .append("signalName", getSignalName())
            .append("stateId", getStateId())
            .append("dataStatus", getDataStatus())
            .append("functionDescription", getFunctionDescription())
            .append("isReserved", getIsReserved())
            .toString();
    }
}
