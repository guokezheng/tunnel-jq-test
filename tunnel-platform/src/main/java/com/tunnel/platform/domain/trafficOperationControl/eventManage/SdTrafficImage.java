package com.tunnel.platform.domain.trafficOperationControl.eventManage;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 图片信息对象 sd_traffic_image
 *
 * @author ruoyi
 * @date 2022-02-22
 */
public class SdTrafficImage extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 图片主键id */
    private Long imgId;

    /** 图片名称 */
    @Excel(name = "图片名称")
    private String imgName;

    /** 图片url */
    @Excel(name = "图片url")
    private String imgUrl;

    /**
     * 图片base64编码格式，为在黄金服务器上显示
     */
    private String imgBase64;

    /** 图片宽度 */
    @Excel(name = "图片宽度")
    private String imgWidth;

    /** 图片高度 */
    @Excel(name = "图片高度")
    private String imgHeight;

    /** 关联业务id */
    @Excel(name = "关联业务id")
    private String businessId;

    public void setImgId(Long imgId)
    {
        this.imgId = imgId;
    }

    public Long getImgId()
    {
        return imgId;
    }
    public void setImgName(String imgName)
    {
        this.imgName = imgName;
    }

    public String getImgName()
    {
        return imgName;
    }
    public void setImgUrl(String imgUrl)
    {
        this.imgUrl = imgUrl;
    }

    public String getImgUrl()
    {
        return imgUrl;
    }
    public void setImgWidth(String imgWidth)
    {
        this.imgWidth = imgWidth;
    }

    public String getImgBase64() {
        return imgBase64;
    }

    public void setImgBase64(String imgBase64) {
        this.imgBase64 = imgBase64;
    }

    public String getImgWidth()
    {
        return imgWidth;
    }
    public void setImgHeight(String imgHeight)
    {
        this.imgHeight = imgHeight;
    }

    public String getImgHeight()
    {
        return imgHeight;
    }

    @Override
    public String toString() {
        return "SdTrafficImage{" +
                "imgId=" + imgId +
                ", imgName='" + imgName + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", imgBase64='" + imgBase64 + '\'' +
                ", imgWidth='" + imgWidth + '\'' +
                ", imgHeight='" + imgHeight + '\'' +
                ", businessId='" + businessId + '\'' +
                '}';
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }
}
