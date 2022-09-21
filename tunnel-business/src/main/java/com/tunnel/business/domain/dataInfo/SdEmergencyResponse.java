package com.tunnel.business.domain.dataInfo;


import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excels;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 应急处置对象 sd_emergency_response
 * 
 * @author zhangweitian
 * @date 2020-11-18
 */
public class SdEmergencyResponse extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 应急处置id */
    private Long responseId;

    /** 应急处置名称 */
    @Excel(name = "应急处置名称")
    private String responseName;

    /** 所属隧道 */
    /*@Excel(name = "所属隧道")*/
    private String responseTunnel;
    
    /** tunnel对象 */
    @Excels({
        @Excel(name = "所属隧道", targetAttr = "tunnelName"),
    })
    private SdTunnels tunnelName;

    /** 设备类型 */
  /*  @Excel(name = "设备类型")*/
    private Long responseType;
    
    /** equipmentType对象 */
    @Excels({
        @Excel(name = "设备类型", targetAttr = "typeName"),
    })
    private SdEquipmentType typeName;

    /** 设备名称 */
    @Excel(name = "设备名称")
    private String responseEqName;

    /** 设备状态 */
    @Excel(name = "设备状态",readConverterExp = "0=正常,1=故障,2=离线")
    private String responseEqState;

    /** 备注 */
    @Excel(name = "备注")
    private String responseRemark;

    public void setResponseId(Long responseId) 
    {
        this.responseId = responseId;
    }

    public Long getResponseId() 
    {
        return responseId;
    }
    public void setResponseName(String responseName) 
    {
        this.responseName = responseName;
    }

    public String getResponseName() 
    {
        return responseName;
    }
    public void setResponseTunnel(String responseTunnel) 
    {
        this.responseTunnel = responseTunnel;
    }

    public String getResponseTunnel() 
    {
        return responseTunnel;
    }
    public void setResponseType(Long responseType) 
    {
        this.responseType = responseType;
    }

    public Long getResponseType() 
    {
        return responseType;
    }
    public void setResponseEqName(String responseEqName) 
    {
        this.responseEqName = responseEqName;
    }

    public String getResponseEqName() 
    {
        return responseEqName;
    }
    public void setResponseEqState(String responseEqState) 
    {
        this.responseEqState = responseEqState;
    }

    public String getResponseEqState() 
    {
        return responseEqState;
    }
    public void setResponseRemark(String responseRemark) 
    {
        this.responseRemark = responseRemark;
    }

    public String getResponseRemark() 
    {
        return responseRemark;
    }

    public SdTunnels getTunnelName() {
    	if (tunnelName == null)
        {
			tunnelName = new SdTunnels();
        }
		return tunnelName;
	}

	public void setTunnelName(SdTunnels tunnelName) {
		this.tunnelName = tunnelName;
	}

	public SdEquipmentType getTypeName() {
		if (typeName == null)
        {
			typeName = new SdEquipmentType();
        }
		return typeName;
	}

	public void setTypeName(SdEquipmentType typeName) {
		this.typeName = typeName;
	}

	@Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("responseId", getResponseId())
            .append("responseName", getResponseName())
            .append("responseTunnel", getResponseTunnel())
            .append("responseType", getResponseType())
            .append("responseEqName", getResponseEqName())
            .append("responseEqState", getResponseEqState())
            .append("responseRemark", getResponseRemark())
            .append("tunnelName", getTunnelName())
            .append("typeName", getTypeName())
            .toString();
    }
}
