package com.tunnel.business.domain.dataInfo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 备品备件库对象 sd_spare_parts_warehouse
 *
 * @author ruoyi
 * @date 2022-01-21
 */
@ApiModel("设备备件库类")
public class SdSparePartsWarehouseVO {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty("id")
    private Long id;

    /**
     * 所属隧道ID
     */
    @ApiModelProperty("所属隧道ID")
    private String tunnelId;

    /*@ApiModelProperty("tunnel对象")
    private String tunnelName;*/

    /**
     * 备件名称
     */
    @ApiModelProperty("备件名称")
    private String partName;

    /**
     * 品牌
     */
    @ApiModelProperty("品牌")
    private String brand;

    /**
     * 型号
     */
    @ApiModelProperty("型号")
    private String model;

    /**
     * 单位
     */
    @ApiModelProperty("单位")
    private String unit;

    /**
     * 生产厂家
     */
    @ApiModelProperty("生产厂家")
    private String manufacturer;

    /**
     * 上次采购时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty("上次采购时间")
    private Date lastPurchaseTime;

    /**
     * 上次采购数量
     */
    @ApiModelProperty("上次采购数量")
    private String lastPurchaseQuantity;

    /**
     * 上次采购单价
     */
    @ApiModelProperty("上次采购单价")
    private String lastPurchaseUnitPrice;

    /**
     * 当前库存量
     */
    @ApiModelProperty("当前库存量")
    private String currentInventory;

    /**
     * 管理员
     */
    @ApiModelProperty("管理员")
    private String keeper;

    /**
     * 联系方式
     */
    @ApiModelProperty("联系方式")
    private String phone;

    /**
     * 所在位置
     */
    @ApiModelProperty("所在位置")
    private String location;


    public String getTunnelId() {
        return tunnelId;
    }

    public void setTunnelId(String tunnelId) {
        this.tunnelId = tunnelId;
    }

    /*public String getTunnelName() {
        return tunnelName;
    }

    public void setTunnelName(String tunnelName) {
        this.tunnelName = tunnelName;
    }*/

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public String getPartName() {
        return partName;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getBrand() {
        return brand;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getModel() {
        return model;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUnit() {
        return unit;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setLastPurchaseTime(Date lastPurchaseTime) {
        this.lastPurchaseTime = lastPurchaseTime;
    }

    public Date getLastPurchaseTime() {
        return lastPurchaseTime;
    }

    public void setLastPurchaseQuantity(String lastPurchaseQuantity) {
        this.lastPurchaseQuantity = lastPurchaseQuantity;
    }

    public String getLastPurchaseQuantity() {
        return lastPurchaseQuantity;
    }

    public void setLastPurchaseUnitPrice(String lastPurchaseUnitPrice) {
        this.lastPurchaseUnitPrice = lastPurchaseUnitPrice;
    }

    public String getLastPurchaseUnitPrice() {
        return lastPurchaseUnitPrice;
    }

    public void setCurrentInventory(String currentInventory) {
        this.currentInventory = currentInventory;
    }

    public String getCurrentInventory() {
        return currentInventory;
    }

    public void setKeeper(String keeper) {
        this.keeper = keeper;
    }

    public String getKeeper() {
        return keeper;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("partName", getPartName())
                .append("brand", getBrand())
                .append("model", getModel())
                .append("unit", getUnit())
                .append("manufacturer", getManufacturer())
                .append("lastPurchaseTime", getLastPurchaseTime())
                .append("lastPurchaseQuantity", getLastPurchaseQuantity())
                .append("lastPurchaseUnitPrice", getLastPurchaseUnitPrice())
                .append("currentInventory", getCurrentInventory())
                .append("keeper", getKeeper())
                .append("phone", getPhone())
                .append("location", getLocation())
                .toString();
    }
}
