package com.tunnel.platform.domain.informationBoard;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 设备接入信息对象 iot_device_access
 *
 * @author yangqichao
 * @date 2020-03-25
 */
public class IotDeviceAccess extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 设备ID
     */
    private Long deviceId;

    /**
     * 设备类型
     */
    @Excel(name = "设备类型")
    private Long deviceTypeId;

    /**
     * 设备IP
     */
    @Excel(name = "设备IP")
    private String deviceIp;

    /**
     * 设备端口
     */
    @Excel(name = "设备端口")
    private String devicePort;

    /**
     * 设备协议ID
     */
    @Excel(name = "设备协议ID")
    private Long deviceProtocolId;

    /**
     * 设备分辨率
     */
    @Excel(name = "设备分辨率")
    private String devicePixel;
    /**
     * 用户名
     */
    @Excel(name = "用户名")
    private String userName;

    /**
     * 密码
     */
    @Excel(name = "密码")
    private String passWord;

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public Long getDeviceTypeId() {
        return deviceTypeId;
    }

    public void setDeviceTypeId(Long deviceTypeId) {
        this.deviceTypeId = deviceTypeId;
    }

    public void setDeviceIp(String deviceIp) {
        this.deviceIp = deviceIp;
    }

    public String getDeviceIp() {
        return deviceIp;
    }

    public void setDevicePort(String devicePort) {
        this.devicePort = devicePort;
    }

    public String getDevicePort() {
        return devicePort;
    }

    public void setDeviceProtocolId(Long deviceProtocolId) {
        this.deviceProtocolId = deviceProtocolId;
    }

    public Long getDeviceProtocolId() {
        return deviceProtocolId;
    }

    public void setDevicePixel(String devicePixel) {
        this.devicePixel = devicePixel;
    }

    public String getDevicePixel() {
        return devicePixel;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("deviceId", getDeviceId())
                .append("deviceTypeId", getDeviceTypeId())
                .append("deviceIp", getDeviceIp())
                .append("devicePort", getDevicePort())
                .append("deviceProtocolId", getDeviceProtocolId())
                .append("devicePixel", getDevicePixel())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
