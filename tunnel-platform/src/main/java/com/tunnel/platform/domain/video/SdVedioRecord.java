package com.tunnel.platform.domain.video;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tunnel.platform.domain.dataInfo.SdTunnels;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 历史视频信息对象 sd_vedio_record
 * 
 * @author xuebi
 * @date 2020-11-20
 */
public class SdVedioRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 隧道ID */
    @Excel(name = "隧道ID")
    private String tunnelId;

    private SdTunnels tunnels;
    
    /** 视频名称 */
    @Excel(name = "视频名称")
    private String vedioName;

    /** 视频ip */
    @Excel(name = "视频ip")
    private String videoIp;

    /** 视频ip */
    @Excel(name = "视频ip")
    private String url;

    /** 所在桩号 */
    @Excel(name = "所在桩号")
    private String stakeMark;

    /** 视频开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "视频开始时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /** 视频结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "视频结束时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date overTime;

    /** 视频大小 */
    @Excel(name = "视频大小")
    private String vedioSize;

    /** 视频格式 */
    @Excel(name = "视频格式")
    private String vedioFormat;

    /** 存储地址 */
    @Excel(name = "存储地址")
    private String storageAddress;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setTunnelId(String tunnelId)
    {
        this.tunnelId = tunnelId;
    }

    public String getTunnelId()
    {
        return tunnelId;
    }
    
    public SdTunnels getTunnels() {
		if (tunnels == null)
        {
			tunnels = new SdTunnels();
        }
		return tunnels;
	}

	public void setTunnels(SdTunnels tunnels) {
		this.tunnels = tunnels;
	}
    
    public void setVedioName(String vedioName) 
    {
        this.vedioName = vedioName;
    }

    public String getVedioName() 
    {
        return vedioName;
    }
    public void setVideoIp(String videoIp) 
    {
        this.videoIp = videoIp;
    }

    public String getVideoIp() 
    {
        return videoIp;
    }
    public void setStakeMark(String stakeMark) 
    {
        this.stakeMark = stakeMark;
    }

    public String getStakeMark() 
    {
        return stakeMark;
    }
    public void setStartTime(Date startTime) 
    {
        this.startTime = startTime;
    }

    public Date getStartTime() 
    {
        return startTime;
    }
    
    public void setOverTime(Date overTime) 
    {
        this.overTime = overTime;
    }
    
    public Date getOverTime() 
    {
        return overTime;
    }
    
    public void setVedioSize(String vedioSize) 
    {
        this.vedioSize = vedioSize;
    }

    public String getVedioSize() 
    {
        return vedioSize;
    }
    public void setVedioFormat(String vedioFormat) 
    {
        this.vedioFormat = vedioFormat;
    }

    public String getVedioFormat() 
    {
        return vedioFormat;
    }
    public void setStorageAddress(String storageAddress) 
    {
        this.storageAddress = storageAddress;
    }

    public String getStorageAddress() 
    {
        return storageAddress;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("tunnelId", getTunnelId())
            .append("tunnels", getTunnels())
            .append("vedioName", getVedioName())
            .append("videoIp", getVideoIp())
            .append("stakeMark", getStakeMark())
            .append("startTime", getStartTime())
            .append("overTime", getOverTime())
            .append("vedioSize", getVedioSize())
            .append("vedioFormat", getVedioFormat())
            .append("storageAddress", getStorageAddress())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}