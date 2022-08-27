package com.tunnel.platform.domain.dataInfo;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * plc 报文关联设备对象 sd_cmd_rl
 * 
 * @author zhangweitian
 * @date 2020-09-02
 */
public class SdCmdRl extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 关系ID */
    private Long cmdRlId;

    /** 报文ID */
    @Excel(name = "报文ID")
    private String plcCmdId;

    /** PLC类型 */
    @Excel(name = "PLC类型")
    private Long cmdPlcId;

    /** plc下属设备 */
    @Excel(name = "plc下属设备")
    private String eqs;
    
    /** 指令 */
    @Excel(name = "指令")
    private String commands;

    public String getCommands() {
		return commands;
	}

	public void setCommands(String commands) {
		this.commands = commands;
	}

	public void setCmdRlId(Long cmdRlId) 
    {
        this.cmdRlId = cmdRlId;
    }

    public Long getCmdRlId() 
    {
        return cmdRlId;
    }
    public void setPlcCmdId(String plcCmdId) 
    {
        this.plcCmdId = plcCmdId;
    }

    public String getPlcCmdId() 
    {
        return plcCmdId;
    }
    public void setCmdPlcId(Long cmdPlcId) 
    {
        this.cmdPlcId = cmdPlcId;
    }

    public Long getCmdPlcId() 
    {
        return cmdPlcId;
    }
    public void setEqs(String eqs) 
    {
        this.eqs = eqs;
    }

    public String getEqs() 
    {
        return eqs;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("cmdRlId", getCmdRlId())
            .append("plcCmdId", getPlcCmdId())
            .append("cmdPlcId", getCmdPlcId())
            .append("eqs", getEqs())
            .append("commands", getCommands())
            .toString();
    }
}