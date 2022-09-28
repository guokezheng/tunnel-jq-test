package com.tunnel.business.domain.informationBoard;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 情报板字体适应模板对象 iot_borad_font
 * 
 * @author wangyaozong
 * @date 2020-06-08
 */
public class IotBoradFont extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** null */
    private Long id;

    /** 分辨率 */
    @Excel(name = "分辨率")
    private String devicePixel;

    /** 字号 */
    @Excel(name = "字号")
    private Long fontSize;

    /** 字数最大长度 */
    @Excel(name = "字数最大长度")
    private Long maxLength;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setDevicePixel(String devicePixel) 
    {
        this.devicePixel = devicePixel;
    }

    public String getDevicePixel() 
    {
        return devicePixel;
    }
    public void setFontSize(Long fontSize) 
    {
        this.fontSize = fontSize;
    }

    public Long getFontSize() 
    {
        return fontSize;
    }
    public void setMaxLength(Long maxLength) 
    {
        this.maxLength = maxLength;
    }

    public Long getMaxLength() 
    {
        return maxLength;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("devicePixel", getDevicePixel())
            .append("fontSize", getFontSize())
            .append("maxLength", getMaxLength())
            .toString();
    }
}
