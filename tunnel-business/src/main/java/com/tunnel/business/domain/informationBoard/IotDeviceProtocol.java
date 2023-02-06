package com.tunnel.business.domain.informationBoard;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.beans.Transient;

/**
 * 通信协议对象 iot_board_protocol
 *
 * @author wangyaozong
 * @date 2020-03-28
 */
public class IotDeviceProtocol extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 协议ID */
    private Long protocolId;

    /** 协议名称 */
    @Excel(name = "协议名称")
    private String protocolName;

    /** 设备类型 */
    @Excel(name = "设备类型")
    private String deviceTypeId;

    /** 协议函数 */
    private String protocolFunction;

    /** 协议函数参数 */
    private String protocolParam;

    @Transient
    public String getProtocolFunction() {
        return protocolFunction;
    }
    public void setProtocolFunction(String protocolFunction) {
        this.protocolFunction = protocolFunction;
    }

    @Transient
    public String getProtocolParam() {
        return protocolParam;
    }
    public void setProtocolParam(String protocolParam) {
        this.protocolParam = protocolParam;
    }

    public void setProtocolId(Long protocolId)
    {
        this.protocolId = protocolId;
    }

    public Long getProtocolId()
    {
        return protocolId;
    }
    public void setProtocolName(String protocolName)
    {
        this.protocolName = protocolName;
    }

    public String getProtocolName()
    {
        return protocolName;
    }
    public void setDeviceTypeId(String deviceTypeId)
    {
        this.deviceTypeId = deviceTypeId;
    }

    public String getDeviceTypeId()
    {
        return deviceTypeId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("protocolId", getProtocolId())
            .append("protocolName", getProtocolName())
            .append("deviceTypeId", getDeviceTypeId())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
