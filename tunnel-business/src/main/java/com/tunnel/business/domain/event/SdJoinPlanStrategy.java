package com.tunnel.business.domain.event;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 预案策略情报板信息对象 sd_join_plan_strategy
 * 
 * @author ruoyi
 * @date 2023-04-28
 */
public class SdJoinPlanStrategy extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 通用id */
    @Excel(name = "通用id")
    private Long currentId;

    /** 模板ID */
    @Excel(name = "模板ID")
    private String templateId;

    /** 屏幕尺寸 */
    @Excel(name = "屏幕尺寸")
    private String screenSize;

    /** 内容 */
    @Excel(name = "内容")
    private String content;

    /** 字体颜色 */
    @Excel(name = "字体颜色")
    private String fontColor;

    /** 字体大小 */
    @Excel(name = "字体大小")
    private Long fontSize;

    /** 字体类型 */
    @Excel(name = "字体类型")
    private String fontType;

    /** 字体间距/px */
    @Excel(name = "字体间距/px")
    private Long fontSpacing;

    /** 起始点位置;前3位代表x点的位值，后3位代表y点的位置 */
    @Excel(name = "起始点位置;前3位代表x点的位值，后3位代表y点的位置")
    private String coordinate;

    /** 图片地址 */
    @Excel(name = "图片地址")
    private String imageUrl;

    /** 图片高度 */
    @Excel(name = "图片高度")
    private String height;

    /** 图片宽度 */
    @Excel(name = "图片宽度")
    private String width;

    /** 类型 0：预案 1：策略 */
    @Excel(name = "类型 0：预案 1：策略")
    private String type;

    /** 停留时间/秒 */
    @Excel(name = "停留时间/毫秒")
    private Long stopTime;

    public Long getStopTime() {
        return stopTime;
    }

    public void setStopTime(Long stopTime) {
        this.stopTime = stopTime;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setCurrentId(Long currentId) 
    {
        this.currentId = currentId;
    }

    public Long getCurrentId() 
    {
        return currentId;
    }
    public void setTemplateId(String templateId) 
    {
        this.templateId = templateId;
    }

    public String getTemplateId() 
    {
        return templateId;
    }
    public void setScreenSize(String screenSize) 
    {
        this.screenSize = screenSize;
    }

    public String getScreenSize() 
    {
        return screenSize;
    }
    public void setContent(String content) 
    {
        this.content = content;
    }

    public String getContent() 
    {
        return content;
    }
    public void setFontColor(String fontColor) 
    {
        this.fontColor = fontColor;
    }

    public String getFontColor() 
    {
        return fontColor;
    }
    public void setFontSize(Long fontSize) 
    {
        this.fontSize = fontSize;
    }

    public Long getFontSize() 
    {
        return fontSize;
    }
    public void setFontType(String fontType) 
    {
        this.fontType = fontType;
    }

    public String getFontType() 
    {
        return fontType;
    }
    public void setFontSpacing(Long fontSpacing) 
    {
        this.fontSpacing = fontSpacing;
    }

    public Long getFontSpacing() 
    {
        return fontSpacing;
    }
    public void setCoordinate(String coordinate) 
    {
        this.coordinate = coordinate;
    }

    public String getCoordinate() 
    {
        return coordinate;
    }
    public void setImageUrl(String imageUrl) 
    {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() 
    {
        return imageUrl;
    }
    public void setHeight(String height) 
    {
        this.height = height;
    }

    public String getHeight() 
    {
        return height;
    }
    public void setWidth(String width) 
    {
        this.width = width;
    }

    public String getWidth() 
    {
        return width;
    }

    public void setType(String type) 
    {
        this.type = type;
    }

    public String getType() 
    {
        return type;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("currentId", getCurrentId())
            .append("templateId", getTemplateId())
            .append("screenSize", getScreenSize())
            .append("content", getContent())
            .append("fontColor", getFontColor())
            .append("fontSize", getFontSize())
            .append("fontType", getFontType())
            .append("fontSpacing", getFontSpacing())
            .append("coordinate", getCoordinate())
            .append("imageUrl", getImageUrl())
            .append("height", getHeight())
            .append("width", getWidth())
            .append("type", getType())
            .append("createTime", getCreateTime())
            .append("stopTime", getStopTime())
            .toString();
    }
}
