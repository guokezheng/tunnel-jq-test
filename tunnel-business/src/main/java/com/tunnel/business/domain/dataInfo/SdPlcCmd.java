package com.tunnel.business.domain.dataInfo;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * plc 报文对象 sd_plc_cmd
 *
 * @author yangqichao
 * @date 2021-12-01
 */
public class SdPlcCmd extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long cmdId;

    /**
     * 相关plc
     */
    @Excel(name = "相关plc")
    private String cmdPlcId;

    /**
     * 包含的设备id，多个用逗号隔开
     */
    @Excel(name = "包含的设备id，多个用逗号隔开")
    private String cmdDevices;

    /**
     * 下发的报文
     */
    @Excel(name = "下发的报文")
    private String command;

    /**
     * 设备类型
     */
    @Excel(name = "设备类型")
    private String cmdDevicesType;

    public void setCmdId(Long cmdId) {
        this.cmdId = cmdId;
    }

    public Long getCmdId() {
        return cmdId;
    }

    public void setCmdPlcId(String cmdPlcId) {
        this.cmdPlcId = cmdPlcId;
    }

    public String getCmdPlcId() {
        return cmdPlcId;
    }

    public void setCmdDevices(String cmdDevices) {
        this.cmdDevices = cmdDevices;
    }

    public String getCmdDevices() {
        return cmdDevices;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public String getCmdDevicesType() {
        return cmdDevicesType;
    }

    public void setCmdDevicesType(String cmdDevicesType) {
        this.cmdDevicesType = cmdDevicesType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("cmdId", getCmdId())
                .append("cmdPlcId", getCmdPlcId())
                .append("cmdDevicesType", getCmdDevicesType())
                .append("cmdDevices", getCmdDevices())
                .append("command", getCommand())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
