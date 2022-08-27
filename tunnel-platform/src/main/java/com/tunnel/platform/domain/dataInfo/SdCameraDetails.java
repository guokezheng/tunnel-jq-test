package com.tunnel.platform.domain.dataInfo;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 摄像机详情对象 sd_camera_details
 * 
 * @author liubaokui
 * @date 2021-05-08
 */
public class SdCameraDetails extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 摄像机id */
    @Excel(name = "摄像机id")
    private String camId;

    /** 摄像机名字 */
    @Excel(name = "摄像机名字")
    private String camName;

    /** 摄像机IP */
    @Excel(name = "摄像机IP")
    private String camIp;

    /** 摄像机端口 */
    @Excel(name = "摄像机端口")
    private String camPort;
    /** 关联摄像头，id组合以#分隔 */
    private String linkedCamera;
    /** 桩号 */
    private String stakeMark;
    
    private String tunnelName;

	public String getLinkedCamera() {
		return linkedCamera;
	}

	public void setLinkedCamera(String linkedCamera) {
		this.linkedCamera = linkedCamera;
	}

	public String getStakeMark() {
		return stakeMark;
	}

	public void setStakeMark(String stakeMark) {
		this.stakeMark = stakeMark;
	}

	public String getTunnelName() {
		return tunnelName;
	}

	public void setTunnelName(String tunnelName) {
		this.tunnelName = tunnelName;
	}

	/** 摄像机详情 */
    @Excel(name = "摄像机详情")
    private String camDesc;

    /** 隧道id */
    @Excel(name = "隧道id")
    private String tunnelId;

    /** 车洞/ Y:右洞 Z:左洞 */
    @Excel(name = "车洞/ Y:右洞 Z:左洞")
    private String holeDirection;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setCamId(String camId) 
    {
        this.camId = camId;
    }

    public String getCamId() 
    {
        return camId;
    }
    public void setCamName(String camName) 
    {
        this.camName = camName;
    }

    public String getCamName() 
    {
        return camName;
    }
    public void setCamIp(String camIp) 
    {
        this.camIp = camIp;
    }

    public String getCamIp() 
    {
        return camIp;
    }
    public void setCamPort(String camPort) 
    {
        this.camPort = camPort;
    }

    public String getCamPort() 
    {
        return camPort;
    }
    public void setCamDesc(String camDesc) 
    {
        this.camDesc = camDesc;
    }

    public String getCamDesc() 
    {
        return camDesc;
    }
    public void setTunnelId(String tunnelId) 
    {
        this.tunnelId = tunnelId;
    }

    public String getTunnelId() 
    {
        return tunnelId;
    }
    public void setHoleDirection(String holeDirection) 
    {
        this.holeDirection = holeDirection;
    }

    public String getHoleDirection() 
    {
        return holeDirection;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("camId", getCamId())
            .append("camName", getCamName())
            .append("camIp", getCamIp())
            .append("camPort", getCamPort())
            .append("camDesc", getCamDesc())
            .append("tunnelId", getTunnelId())
            .append("holeDirection", getHoleDirection())
            .append("tunnelName",getTunnelName())
            .append("linkedCamera",getLinkedCamera())
            .append("stakeMark",getStakeMark())
            .toString();
    }
}
