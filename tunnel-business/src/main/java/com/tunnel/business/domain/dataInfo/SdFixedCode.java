package com.tunnel.business.domain.dataInfo;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 指令码固定字段表对象 sd_fixed_code
 * 
 * @author ruoyi
 * @date 2021-04-10
 */
public class SdFixedCode extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** Id */
    @Excel(name = "Id")
    private Long id;

    /** 模式：DM/OCI */
    @Excel(name = "模式：DM/OCI")
    private String modelCode;

    /** 固定码 */
    @Excel(name = "固定码")
    private String fixedCode;

    /** 本地IP */
    @Excel(name = "本地IP")
    private String localIp;

    /** 查控码：0101/0102 */
    @Excel(name = "查控码：0101/0102")
    private String queryControl;

    /** 方式码：80/82 */
    @Excel(name = "方式码：80/82")
    private String modelNum;

    /** DM控制固定：0001 */
    @Excel(name = "DM控制固定：0001")
    private String controlCode;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setModelCode(String modelCode) 
    {
        this.modelCode = modelCode;
    }

    public String getModelCode() 
    {
        return modelCode;
    }
    public void setFixedCode(String fixedCode) 
    {
        this.fixedCode = fixedCode;
    }

    public String getFixedCode() 
    {
        return fixedCode;
    }
    public void setLocalIp(String localIp) 
    {
        this.localIp = localIp;
    }

    public String getLocalIp() 
    {
        return localIp;
    }
    public void setQueryControl(String queryControl) 
    {
        this.queryControl = queryControl;
    }

    public String getQueryControl() 
    {
        return queryControl;
    }
    public void setModelNum(String modelNum) 
    {
        this.modelNum = modelNum;
    }

    public String getModelNum() 
    {
        return modelNum;
    }
    public void setControlCode(String controlCode) 
    {
        this.controlCode = controlCode;
    }

    public String getControlCode() 
    {
        return controlCode;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("modelCode", getModelCode())
            .append("fixedCode", getFixedCode())
            .append("localIp", getLocalIp())
            .append("queryControl", getQueryControl())
            .append("modelNum", getModelNum())
            .append("controlCode", getControlCode())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
