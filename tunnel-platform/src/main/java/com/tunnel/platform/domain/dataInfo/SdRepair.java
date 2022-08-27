package com.tunnel.platform.domain.dataInfo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 设备巡检修对象 sd_repair
 * 
 * @author liubaokui
 * @date 2021-05-12
 */
public class SdRepair extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 设施巡检修id */
    private Long repairId;

    /** 检修人 */
    @Excel(name = "检修人")
    private String repairName;

    /** 所属隧道 */
    private String repairTunnel;
    
    /** 隧道名称 */
    @Excel(name = "所属隧道")
    private String tunnelName;
    
    /** 检修时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "检修时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date repairTime;
    
    /** 检修位置 */
    @Excel(name = "检修位置")
    private String repairPosition;

    /** 检修内容 */
    @Excel(name = "检修内容")
    private String repairContent;
    
    /** 检修结果   */
    @Excel(name = "检修结果  ")
    private String repairResult;

	/** 备注 */
    @Excel(name = "备注")
    private String repairRemark;
    
    /** 创建人  */
    private String createName;
    
    /** 修改人   */
    private String updateName;
    
	public void setRepairId(Long repairId) 
    {
        this.repairId = repairId;
    }

    public Long getRepairId() 
    {
        return repairId;
    }
    public void setRepairName(String repairName) 
    {
        this.repairName = repairName;
    }

    public String getRepairName() 
    {
        return repairName;
    }
    public void setRepairTunnel(String repairTunnel) 
    {
        this.repairTunnel = repairTunnel;
    }

    public String getRepairTunnel() 
    {
        return repairTunnel;
    }

    public String getTunnelName() {
		return tunnelName;
	}

	public void setTunnelName(String tunnelName) {
		this.tunnelName = tunnelName;
	}
	
    public void setRepairRemark(String repairRemark) 
    {
        this.repairRemark = repairRemark;
    }

    public String getRepairRemark() 
    {
        return repairRemark;
    }
    public void setRepairTime(Date repairTime) 
    {
        this.repairTime = repairTime;
    }

    public Date getRepairTime() 
    {
        return repairTime;
    }
    
    public void setRepairPosition(String repairPosition) 
    {
        this.repairPosition = repairPosition;
    }

    public String getRepairPosition() 
    {
        return repairPosition;
    }
    public void setRepairContent(String repairContent) 
    {
        this.repairContent = repairContent;
    }

    public String getRepairContent() 
    {
        return repairContent;
    }
    public void setRepairResult(String repairResult) 
    {
        this.repairResult = repairResult;
    }

    public String getRepairResult() 
    {
        return repairResult;
    }
    
    public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}

	public String getUpdateName() {
		return updateName;
	}

	public void setUpdateName(String updateName) {
		this.updateName = updateName;
	}

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("repairId", getRepairId())
            .append("repairName", getRepairName())
            .append("repairTunnel", getRepairTunnel())
            .append("tunnelName", getTunnelName())
            .append("repairRemark", getRepairRemark())
            .append("repairTime", getRepairTime())
            .append("repairPosition", getRepairPosition())
            .append("repairContent", getRepairContent())
            .append("repairResult", getRepairResult())
            .append("createName", getCreateName())
            .append("createTime", getCreateTime())
            .append("updateName", getUpdateName())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
