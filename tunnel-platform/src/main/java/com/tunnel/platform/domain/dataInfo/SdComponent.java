package com.tunnel.platform.domain.dataInfo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excels;
import com.ruoyi.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;
import java.util.List;

/**
 * 设备档案管理对象 sd_component
 * 
 * @author yanghousheng
 * @date 2020-11-18
 */
@ApiModel("设备档案管理类")
public class SdComponent extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    @ApiModelProperty("ID")
    private Long id;

    /** 设备id */
    @Excel(name = "设备id")
    @ApiModelProperty("设备id")
    private String equipmentId;

    /** 设备编号 */
    @Excel(name = "设备编号")
    @ApiModelProperty("设备编号")
    private String equipmentCode;

    /** 设备名称 */
    @Excel(name = "设备名称")
    @ApiModelProperty("设备名称")
    private String equipmentName;

    /** 品牌编号 */
    @Excel(name = "品牌编号")
    @ApiModelProperty("品牌编号")
    private Long brandId;

    /** 类型编号 */
    @ApiModelProperty("类型编号")
    @Excel(name = "类型编号")
    private Long equipmentTypeId;
    
    private List<SdEquipmentFile> pFileList;
    
    public List<SdEquipmentFile> getpFileList() {
		return pFileList;
	}

	public void setpFileList(List<SdEquipmentFile> pFileList) {
		this.pFileList = pFileList;
	}

	/** equipmentType对象 */
    @Excels({
        @Excel(name = "equipmentType", targetAttr = "typeName"),
    })
    @ApiModelProperty("equipmentType对象")
    private SdEquipmentType typeName;

    /** 隧道编号 */
    @Excel(name = "隧道编号")
    @ApiModelProperty("隧道编号")
    private String tunnelId;
    /**tunnel对象**/
    @Excels({
        @Excel(name = "tunnelName", targetAttr = "tunnel"),
    })
    @ApiModelProperty("tunnel对象")
    private SdTunnels tunnelName;

    public SdEquipmentType getTypeName() {
    	if(typeName == null){
    		typeName = new SdEquipmentType();
    	}
		return typeName;
	}

	public void setTypeName(SdEquipmentType typeName) {
		this.typeName = typeName;
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

	/** 生产商编号 */
    @Excel(name = "生产商编号")
    @ApiModelProperty("生产商编号")
    private Long producerId;

    /** 设备状态(0:正常 1:故障 2:离线) */
    @Excel(name = "设备状态",readConverterExp = "0=正常,1=故障,2=离线")
    @ApiModelProperty("设备状态(0:正常 1:故障 2:离线) ")
    private String equipmentState;

    /** 场所编号 */
    @Excel(name = "场所编号")
    @ApiModelProperty("场所编号")
    private String installationPlaceId;
    /**SdPlaceLocation对象**/
    @Excels({
        @Excel(name = "locationPlace", targetAttr = "placeLocation"),
    })
    private SdPlaceLocation locationPlace;
        public SdPlaceLocation getLocationPlace() {
        	if(locationPlace == null){
        		locationPlace = new  SdPlaceLocation();
        	}
    	return locationPlace;
	}

	public void setLocationPlace(SdPlaceLocation locationPlace) {
		this.locationPlace = locationPlace;
	}

		/** 安装位置 */
    @Excel(name = "安装位置")
    @ApiModelProperty("安装位置 ")
    private String installationPosition;

    /** 安装时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty("安装时间 ")
    @Excel(name = "安装时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date installationTime;

    /** 生产日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty("生产日期 ")
    @Excel(name = "生产日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date productionTime;

    /** 设备库存数量 */
    @ApiModelProperty("设备库存数量 ")
    @Excel(name = "设备库存数量")
    private String equipmentInventory;

    /** 文件id */
    @Excel(name = "文件id")
    @ApiModelProperty("文件id ")
    private String fileId;

    /** 文件名称 */
    @Excel(name = "文件名称")
    @ApiModelProperty("文件名称 ")
    private String fileName;

    /** 图片路径 */
    @Excel(name = "图片路径")
    @ApiModelProperty("图片路径 ")
    private String imgUrl;

    /** 文件存储路径 */
    @Excel(name = "文件存储路径")
    @ApiModelProperty("文件存储路径 ")
    private String fileUrl;

    /** 设备图片 */
    @Excel(name = "设备图片")
    @ApiModelProperty("设备图片 ")
    private String equipmentPicture;

    /** 设备价格 */
    @Excel(name = "设备价格")
    @ApiModelProperty("设备价格 ")
    private String equipmentPrice;

    /** 采购时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty("采购时间 ")
    @Excel(name = "采购时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date buyingTime;

    /** 设备安装数量 */
    @Excel(name = "设备安装数量")
    @ApiModelProperty("设备安装数量 ")
    private String equipmentCount;

    private String remark;

    // 品牌
    @ApiModelProperty("品牌 ")
    private String brand;
    // 型号
    @ApiModelProperty("型号 ")
    private String model;
    // 生产厂家
    @ApiModelProperty("生产厂家 ")
    private String manufacturer;

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setEquipmentId(String equipmentId) 
    {
        this.equipmentId = equipmentId;
    }

    public String getEquipmentId() 
    {
        return equipmentId;
    }
    public void setEquipmentCode(String equipmentCode) 
    {
        this.equipmentCode = equipmentCode;
    }

    public String getEquipmentCode() 
    {
        return equipmentCode;
    }
    public void setEquipmentName(String equipmentName) 
    {
        this.equipmentName = equipmentName;
    }

    public String getEquipmentName() 
    {
        return equipmentName;
    }
    public void setBrandId(Long brandId) 
    {
        this.brandId = brandId;
    }

    public Long getBrandId() 
    {
        return brandId;
    }
    public void setEquipmentTypeId(Long equipmentTypeId) 
    {
        this.equipmentTypeId = equipmentTypeId;
    }

    public Long getEquipmentTypeId() 
    {
        return equipmentTypeId;
    }
    public void setTunnelId(String tunnelId) 
    {
        this.tunnelId = tunnelId;
    }

    public String getTunnelId() 
    {
        return tunnelId;
    }
    public void setProducerId(Long producerId) 
    {
        this.producerId = producerId;
    }

    public Long getProducerId() 
    {
        return producerId;
    }
    public void setEquipmentState(String equipmentState) 
    {
        this.equipmentState = equipmentState;
    }

    public String getEquipmentState() 
    {
        return equipmentState;
    }
    public void setInstallationPlaceId(String installationPlaceId) 
    {
        this.installationPlaceId = installationPlaceId;
    }

    public String getInstallationPlaceId() 
    {
        return installationPlaceId;
    }
    public void setInstallationPosition(String installationPosition) 
    {
        this.installationPosition = installationPosition;
    }

    public String getInstallationPosition() 
    {
        return installationPosition;
    }
    public void setInstallationTime(Date installationTime) 
    {
        this.installationTime = installationTime;
    }

    public Date getInstallationTime() 
    {
        return installationTime;
    }
    public void setProductionTime(Date productionTime) 
    {
        this.productionTime = productionTime;
    }

    public Date getProductionTime() 
    {
        return productionTime;
    }
    public void setEquipmentInventory(String equipmentInventory) 
    {
        this.equipmentInventory = equipmentInventory;
    }

    public String getEquipmentInventory() 
    {
        return equipmentInventory;
    }
    public void setFileId(String fileId) 
    {
        this.fileId = fileId;
    }

    public String getFileId() 
    {
        return fileId;
    }
    public void setFileName(String fileName) 
    {
        this.fileName = fileName;
    }

    public String getFileName() 
    {
        return fileName;
    }
    public void setImgUrl(String imgUrl) 
    {
        this.imgUrl = imgUrl;
    }

    public String getImgUrl() 
    {
        return imgUrl;
    }
    public void setFileUrl(String fileUrl) 
    {
        this.fileUrl = fileUrl;
    }

    public String getFileUrl() 
    {
        return fileUrl;
    }
    public void setEquipmentPicture(String equipmentPicture) 
    {
        this.equipmentPicture = equipmentPicture;
    }

    public String getEquipmentPicture() 
    {
        return equipmentPicture;
    }
    public void setEquipmentPrice(String equipmentPrice) 
    {
        this.equipmentPrice = equipmentPrice;
    }

    public String getEquipmentPrice() 
    {
        return equipmentPrice;
    }
    public void setBuyingTime(Date buyingTime) 
    {
        this.buyingTime = buyingTime;
    }

    public Date getBuyingTime() 
    {
        return buyingTime;
    }
    public void setEquipmentCount(String equipmentCount) 
    {
        this.equipmentCount = equipmentCount;
    }

    public String getEquipmentCount() 
    {
        return equipmentCount;
    }

    @Override
    public String getRemark() {
        return remark;
    }

    @Override
    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("equipmentId", getEquipmentId())
            .append("equipmentCode", getEquipmentCode())
            .append("equipmentName", getEquipmentName())
            .append("brandId", getBrandId())
            .append("equipmentTypeId", getEquipmentTypeId())
            .append("pFileList", getpFileList())
            .append("typeName", getTypeName())
            .append("tunnelName", getTunnelName())
            .append("tunnelId", getTunnelId())
            .append("producerId", getProducerId())
            .append("equipmentState", getEquipmentState())
            .append("installationPlaceId", getInstallationPlaceId())
            .append("locationPlace", getLocationPlace())
            .append("installationPosition", getInstallationPosition())
            .append("installationTime", getInstallationTime())
            .append("productionTime", getProductionTime())
            .append("equipmentInventory", getEquipmentInventory())
            .append("fileId", getFileId())
            .append("fileName", getFileName())
            .append("imgUrl", getImgUrl())
            .append("fileUrl", getFileUrl())
            .append("equipmentPicture", getEquipmentPicture())
            .append("equipmentPrice", getEquipmentPrice())
            .append("buyingTime", getBuyingTime())
            .append("equipmentCount", getEquipmentCount())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("manufacturer", getManufacturer())
            .append("brand", getBrand())
            .append("model", getModel())
            .toString();
    }
}
