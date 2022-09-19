package com.tunnel.business.domain.xiaoFang;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 消防点位信息对象 xf_point_info
 * 
 * @author ruoyi
 * @date 2021-05-08
 */
public class XfPointInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 隧道ID */
    @Excel(name = "隧道ID")
    private String tunnelId;

    /** 方向：0左洞，1右洞 */
    @Excel(name = "方向：0左洞，1右洞")
    private String direction;

    /** 桩号 */
    @Excel(name = "桩号")
    private String stakeMark;

    /** 火焰报警器点位 */
    @Excel(name = "火焰报警器点位")
    private String firePoint;

    /** 声光报警器点位 */
    @Excel(name = "声光报警器点位")
    private String voicePoint;

    /** 手动报警按钮点位 */
    @Excel(name = "手动报警按钮点位")
    private String handPoint;

    /** 消火栓点位 */
    @Excel(name = "消火栓点位")
    private String hydrantPoint;

    /** 烟感点位 */
    @Excel(name = "烟感点位")
    private String smokePoint;

    /** 温感点位 */
    @Excel(name = "温感点位")
    private String warmPoint;

    /** 关联摄像头，id组合以#分隔 */
    @Excel(name = "关联摄像头，id组合以#分隔")
    private String linkedCamera;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    {
        this.tunnelId = tunnelId;
    }

    public void setTunnelId(String tunnelId) {
		this.tunnelId = tunnelId;
	}

	public String getTunnelId() 
    {
        return tunnelId;
    }
    public void setDirection(String direction) 
    {
        this.direction = direction;
    }

    public String getDirection() 
    {
        return direction;
    }
    public void setStakeMark(String stakeMark) 
    {
        this.stakeMark = stakeMark;
    }

    public String getStakeMark() 
    {
        return stakeMark;
    }
    public void setFirePoint(String firePoint) 
    {
        this.firePoint = firePoint;
    }

    public String getFirePoint() 
    {
        return firePoint;
    }
    public void setVoicePoint(String voicePoint) 
    {
        this.voicePoint = voicePoint;
    }

    public String getVoicePoint() 
    {
        return voicePoint;
    }
    public void setHandPoint(String handPoint) 
    {
        this.handPoint = handPoint;
    }

    public String getHandPoint() 
    {
        return handPoint;
    }
    public void setHydrantPoint(String hydrantPoint) 
    {
        this.hydrantPoint = hydrantPoint;
    }

    public String getHydrantPoint() 
    {
        return hydrantPoint;
    }
    public void setSmokePoint(String smokePoint) 
    {
        this.smokePoint = smokePoint;
    }

    public String getSmokePoint() 
    {
        return smokePoint;
    }
    public void setWarmPoint(String warmPoint) 
    {
        this.warmPoint = warmPoint;
    }

    public String getWarmPoint() 
    {
        return warmPoint;
    }
    public void setLinkedCamera(String linkedCamera) 
    {
        this.linkedCamera = linkedCamera;
    }

    public String getLinkedCamera() 
    {
        return linkedCamera;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("tunnelId", getTunnelId())
            .append("direction", getDirection())
            .append("stakeMark", getStakeMark())
            .append("firePoint", getFirePoint())
            .append("voicePoint", getVoicePoint())
            .append("handPoint", getHandPoint())
            .append("hydrantPoint", getHydrantPoint())
            .append("smokePoint", getSmokePoint())
            .append("warmPoint", getWarmPoint())
            .append("linkedCamera", getLinkedCamera())
            .toString();
    }
}
