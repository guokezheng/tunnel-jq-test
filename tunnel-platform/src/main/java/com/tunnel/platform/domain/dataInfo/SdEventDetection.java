package com.tunnel.platform.domain.dataInfo;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 事件监测记录对象 sd_event_detection
 * 
 * @author liubaokui
 * @date 2021-04-21
 */
public class SdEventDetection extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 摄像机ID */
    @Excel(name = "摄像机ID")
    private String cameraId;

    /** 方向 */
    @Excel(name = "方向")
    private String direction;

    /** 车道号 */
    @Excel(name = "车道号")
    private String road;

    /** 数据类型 */
    @Excel(name = "数据类型")
    private String datatype;

    /** 事件类型 */
    @Excel(name = "事件类型")
    private String eventtype;

    /** 事件录像FTP地址 */
    @Excel(name = "事件录像FTP地址")
    private String eventVideoFtpAddress;

    /** 事件抓图FTP地址 */
    @Excel(name = "事件抓图FTP地址")
    private String eventPicFtpAddress;
    
    /** 摄像机名字 */
    @Excel(name = "事件抓图FTP地址")
    private String camName;
    
	/** 隧道名字 */
    @Excel(name = "事件抓图FTP地址")
    private String tunnelName;

	public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setCameraId(String cameraId) 
    {
        this.cameraId = cameraId;
    }

    public String getCameraId() 
    {
        return cameraId;
    }
    public void setDirection(String direction) 
    {
        this.direction = direction;
    }

    public String getDirection() 
    {
        return direction;
    }
    public void setRoad(String road) 
    {
        this.road = road;
    }

    public String getRoad() 
    {
        return road;
    }
    public void setDatatype(String datatype) 
    {
        this.datatype = datatype;
    }

    public String getDatatype() 
    {
        return datatype;
    }
    public void setEventtype(String eventtype) 
    {
        this.eventtype = eventtype;
    }

    public String getEventtype() 
    {
        return eventtype;
    }
    public void setEventVideoFtpAddress(String eventVideoFtpAddress) 
    {
        this.eventVideoFtpAddress = eventVideoFtpAddress;
    }

    public String getEventVideoFtpAddress() 
    {
        return eventVideoFtpAddress;
    }
    public void setEventPicFtpAddress(String eventPicFtpAddress) 
    {
        this.eventPicFtpAddress = eventPicFtpAddress;
    }

    public String getEventPicFtpAddress() 
    {
        return eventPicFtpAddress;
    }
    
    public String getCamName() {
		return camName;
	}

	public void setCamName(String camName) {
		this.camName = camName;
	}
	
	 public String getTunnelName() {
			return tunnelName;
		}

	public void setTunnelName(String tunnelName) {
		this.tunnelName = tunnelName;
	}

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("cameraId", getCameraId())
            .append("direction", getDirection())
            .append("road", getRoad())
            .append("datatype", getDatatype())
            .append("eventtype", getEventtype())
            .append("eventVideoFtpAddress", getEventVideoFtpAddress())
            .append("eventPicFtpAddress", getEventPicFtpAddress())
            .append("camName", getCamName())
            .append("tunnelName", getTunnelName())
            .toString();
    }
}
