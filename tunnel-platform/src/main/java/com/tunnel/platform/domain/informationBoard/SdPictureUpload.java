package com.tunnel.platform.domain.informationBoard;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 情报板使用图片对象 sd_picture_upload
 *
 * @author ruoyi
 * @date 2022-03-22
 */
public class SdPictureUpload extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 图片名称 */
    @Excel(name = "图片名称")
    private String pictureName;

    /** 图片url */
    @Excel(name = "图片url")
    private String pictureUrl;

    /** 图片备注 */
    @Excel(name = "图片备注")
    private String imageRemark;

    /** 宽度 */
    @Excel(name = "宽度")
    private String imageWidth;

    /** 高度 */
    @Excel(name = "高度")
    private String imageHeight;

    /** 图片类型 */
    @Excel(name = "图片类型")
    private String imageType;

    /** 图片分辨率 */
    @Excel(name = "图片分辨率")
    private String vmsSize;

    /** 速度 */
    @Excel(name = "速度")
    private String speed;

    /** 是否停用：0启用，1停用 */
    @Excel(name = "是否停用：0启用，1停用")
    private String deleteflag;

    /** 是否删除：0正常，1删除 */
    @Excel(name = "是否删除：0正常，1删除")
    private String logically;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setPictureName(String pictureName)
    {
        this.pictureName = pictureName;
    }

    public String getPictureName()
    {
        return pictureName;
    }
    public void setPictureUrl(String pictureUrl)
    {
        this.pictureUrl = pictureUrl;
    }

    public String getPictureUrl()
    {
        return pictureUrl;
    }
    public void setImageRemark(String imageRemark)
    {
        this.imageRemark = imageRemark;
    }

    public String getImageRemark()
    {
        return imageRemark;
    }
    public void setImageWidth(String imageWidth)
    {
        this.imageWidth = imageWidth;
    }

    public String getImageWidth()
    {
        return imageWidth;
    }
    public void setImageHeight(String imageHeight)
    {
        this.imageHeight = imageHeight;
    }

    public String getImageHeight()
    {
        return imageHeight;
    }
    public void setImageType(String imageType)
    {
        this.imageType = imageType;
    }

    public String getImageType()
    {
        return imageType;
    }
    public void setVmsSize(String vmsSize)
    {
        this.vmsSize = vmsSize;
    }

    public String getVmsSize()
    {
        return vmsSize;
    }
    public void setSpeed(String speed)
    {
        this.speed = speed;
    }

    public String getSpeed()
    {
        return speed;
    }
    public void setDeleteflag(String deleteflag)
    {
        this.deleteflag = deleteflag;
    }

    public String getDeleteflag()
    {
        return deleteflag;
    }
    public void setLogically(String logically)
    {
        this.logically = logically;
    }

    public String getLogically()
    {
        return logically;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("pictureName", getPictureName())
            .append("pictureUrl", getPictureUrl())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("imageRemark", getImageRemark())
            .append("imageWidth", getImageWidth())
            .append("imageHeight", getImageHeight())
            .append("imageType", getImageType())
            .append("vmsSize", getVmsSize())
            .append("speed", getSpeed())
            .append("deleteflag", getDeleteflag())
            .append("logically", getLogically())
            .toString();
    }
}
