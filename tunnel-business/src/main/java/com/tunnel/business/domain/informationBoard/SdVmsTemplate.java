package com.tunnel.business.domain.informationBoard;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 情报板模板对象 sd_vms_template
 *
 * @author 刘方堃
 * @date 2021-11-30
 */
public class SdVmsTemplate extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 模板ID */
    private Long id;

    /** 屏幕尺寸 */
    @Excel(name = "屏幕尺寸")
    private String screenSize;

    /** 入屏方式 */
    @Excel(name = "入屏方式")
    private String inScreenMode;

    /** 滚动速度/毫秒 */
    @Excel(name = "滚动速度/毫秒")
    private Long rollSpeed;

    /** 停留时间/秒 */
    @Excel(name = "停留时间/秒")
    private Long stopTime;

    /** 适用类型 */
    @Excel(name = "适用类型")
    private String applyType;

    /** 是否为通用模板 */
    @Excel(name = "是否为通用模板")
    private Integer isCurrency;

    /** 模板类型 */
    @Excel(name = "模板类型")
    private Integer templateType;

    /** 情报板类型 */
    @Excel(name = "情报板类型")
    private String vmsType;

    private SdVmsTemplateContent tcontent;

    public SdVmsTemplateContent getTcontent() {
        if (tcontent == null) {
            tcontent = new SdVmsTemplateContent();
        }
        return tcontent;
    }

    public void setTcontent(SdVmsTemplateContent tcontent) {
        this.tcontent = tcontent;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setScreenSize(String screenSize)
    {
        this.screenSize = screenSize;
    }

    public String getScreenSize()
    {
        return screenSize;
    }
    public void setInScreenMode(String inScreenMode)
    {
        this.inScreenMode = inScreenMode;
    }

    public String getInScreenMode()
    {
        return inScreenMode;
    }
    public void setRollSpeed(Long rollSpeed)
    {
        this.rollSpeed = rollSpeed;
    }

    public Long getRollSpeed()
    {
        return rollSpeed;
    }
    public void setStopTime(Long stopTime)
    {
        this.stopTime = stopTime;
    }

    public Long getStopTime()
    {
        return stopTime;
    }
    public void setApplyType(String applyType)
    {
        this.applyType = applyType;
    }

    public String getApplyType()
    {
        return applyType;
    }
    public void setIsCurrency(Integer isCurrency)
    {
        this.isCurrency = isCurrency;
    }

    public Integer getIsCurrency()
    {
        return isCurrency;
    }
    public void setTemplateType(Integer templateType)
    {
        this.templateType = templateType;
    }

    public Integer getTemplateType()
    {
        return templateType;
    }
    public void setVmsType(String vmsType)
    {
        this.vmsType = vmsType;
    }

    public String getVmsType()
    {
        return vmsType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("screenSize", getScreenSize())
            .append("inScreenMode", getInScreenMode())
            .append("rollSpeed", getRollSpeed())
            .append("stopTime", getStopTime())
            .append("applyType", getApplyType())
            .append("isCurrency", getIsCurrency())
            .append("templateType", getTemplateType())
            .append("vmsType", getVmsType())
            .append("remark", getRemark())
            .append("tcontent", getTcontent())
            .toString();
    }
}
