package com.tunnel.business.domain.dataInfo;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

/**
 * 设备类型对象 sd_equipment_type
 *
 * @author zhangweitian
 * @param <SdEquipmentStateIconFile>
 * @date 2020-08-27
 */
@Data
@ApiModel("设备类型实体")
public class SdEquipmentType<SdEquipmentStateIconFile> extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 设备类型ID */
    @ApiModelProperty("设备类型Id")
    private Long typeId;

    /** 设备类型名称 */
    @Excel(name = "设备类型名称")
    @ApiModelProperty("设备类型名称")
    private String typeName;

    /** 设备类型代号 */
    @Excel(name = "设备类型代号")
    @ApiModelProperty("设备类型代号")
    private String typeAbbr;

    /** 设备类型默认图标ID */
    @Excel(name = "设备类型默认图标ID")
    @ApiModelProperty("设备类型默认图标ID")
    private String iconFileId;

    /** 图片宽度 */
    @Excel(name = "图片宽度")
    @ApiModelProperty("图片宽度")
    private String iconWidth;
    /** 图片宽度 */
    @Excel(name = "图片高度")
    @ApiModelProperty("图片高度")
    private String iconHeight;

    @Excel(name = "是否可控")
    @ApiModelProperty("是否可控1：是 0：否")
    private String isControl;

    @ApiModelProperty("设备大类")
    private String bigType;

    @Override
    public String toString() {
        return "SdEquipmentType{" +
                "typeId=" + typeId +
                ", typeName='" + typeName + '\'' +
                ", typeAbbr='" + typeAbbr + '\'' +
                ", iconFileId='" + iconFileId + '\'' +
                ", iconWidth='" + iconWidth + '\'' +
                ", iconHeight='" + iconHeight + '\'' +
                ", isControl='" + isControl + '\'' +
                ", bigType='" + bigType + '\'' +
                ", iFileList=" + iFileList +
                '}';
    }

    @ApiModelProperty("默认图标")
    private List<SdEquipmentStateIconFile> iFileList;

    public String getIconWidth() {
		return iconWidth;
	}

	public void setIconWidth(String iconWidth) {
		this.iconWidth = iconWidth;
	}

	public String getIconHeight() {
		return iconHeight;
	}

	public void setIconHeight(String iconHeight) {
		this.iconHeight = iconHeight;
	}

    public void setTypeId(Long typeId)
    {
        this.typeId = typeId;
    }

    public Long getTypeId()
    {
        return typeId;
    }
    public void setIconFileId(String iconFileId)
    {
        this.iconFileId = iconFileId;
    }

    public String getIsControl() {
        return isControl;
    }

    public void setIsControl(String isControl) {
        this.isControl = isControl;
    }

    public String getIconFileId()
    {
        return iconFileId;
    }
    public List<SdEquipmentStateIconFile> getiFileList() {
  		return iFileList;
  	}

  	public void setiFileList(List<SdEquipmentStateIconFile> iFileList) {
  		this.iFileList = iFileList;
  	}

    public void setTypeName(String typeName)
    {
        this.typeName = typeName;
    }

    public String getTypeName()
    {
        return typeName;
    }

    public String getTypeAbbr() {
        return typeAbbr;
    }

    public void setTypeAbbr(String typeAbbr) {
        this.typeAbbr = typeAbbr;
    }

    public String getBigType() {
        return bigType;
    }

    public void setBigType(String bigType) {
        this.bigType = bigType;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

}
