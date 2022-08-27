package com.tunnel.platform.domain.dataInfo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.util.List;

/**
 * 隧道环境配置对象 sd_environment_configuration
 *
 * @author 刘方堃
 * @date 2021-12-13
 */
@ApiModel("道环境配置对象")
public class SdEnvironmentConfiguration extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    @ApiModelProperty("id")
    private Long id;

    /**
     * 上传名称
     */
    @Excel(name = "上传名称")
    @ApiModelProperty("上传名称")
    private String sdName;

    /**
     * 图片路径
     */
    @Excel(name = "图片路径")
    @ApiModelProperty("图片路径")
    private String url;

    /**
     * 环境类型
     */
    @Excel(name = "环境类型")
    @ApiModelProperty("环境类型")
    private String environmentType;

    /**
     * 图片宽度
     */
    @Excel(name = "图片宽度")
    @ApiModelProperty("图片宽度")
    private String width;

    /**
     * 图片高度
     */
    @Excel(name = "图片高度")
    @ApiModelProperty("图片高度")
    private String height;

    /**
     * 方向
     */
    @Excel(name = "方向")
    @ApiModelProperty("方向")
    private String direction;
    private List<SdEquipmentStateIconFile> iFileList;

    public List<SdEquipmentStateIconFile> getiFileList() {
        return iFileList;
    }

    public void setiFileList(List<SdEquipmentStateIconFile> iFileList) {
        this.iFileList = iFileList;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setSdName(String sdName) {
        this.sdName = sdName;
    }

    public String getSdName() {
        return sdName;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setEnvironmentType(String environmentType) {
        this.environmentType = environmentType;
    }

    public String getEnvironmentType() {
        return environmentType;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getWidth() {
        return width;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getHeight() {
        return height;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getDirection() {
        return direction;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("sdName", getSdName())
                .append("url", getUrl())
                .append("environmentType", getEnvironmentType())
                .append("width", getWidth())
                .append("height", getHeight())
                .append("direction", getDirection())
                .append("createBy", getCreateBy())
                .append("remark", getRemark())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .append("iFileList", getiFileList())
                .toString();
    }
}
