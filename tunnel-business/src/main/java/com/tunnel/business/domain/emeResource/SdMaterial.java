package com.tunnel.business.domain.emeResource;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excels;
import com.ruoyi.common.core.domain.BaseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 应急资源对象 sd_material
 *
 * @author xuebi
 * @date 2020-08-28
 */
@ApiModel("应急资源类")
public class SdMaterial extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @ApiModelProperty("ID")
    private Long id;

    /**
     * 物资编号
     */
   /* @Excel(name = "物资编号")*/
    @ApiModelProperty("物资编号")
    private String materialId;

    /**
     * 物资名称
     */
    @Excel(name = "物资名称")
    @ApiModelProperty("物资名称")
    private String materialName;
    /**
     * 物资类型
     */
    /*@Excel(name = "物资类型")*/
    @ApiModelProperty("物资类型")
    private String materialType;
    @Excel(name = "物资类型")
    private String wzlx;
    @Excel(name = "隧道")
    private String tName;

    public String getWzlx() {
        return this.wzlx;
    }

    public void setWzlx( String wzlx) {
        this.wzlx = wzlx;
    }

    public String gettName() {
        return this.tName;
    }

    public void settName( String tName) {
        this.tName = tName;
    }

    /**
     * 物资类型对象
     */
    /*@Excels({
            @Excel(name = "物资类型", targetAttr = "name"),
    })*/
    @ApiModelProperty("物资类型对象")
    private SdMaterialType sdMaterialType;

    public SdMaterialType getSdMaterialType() {
        if (sdMaterialType == null) {
            sdMaterialType = new SdMaterialType();
        }
        return sdMaterialType;
    }

    public void setSdMaterialType(SdMaterialType sdMaterialType) {
        this.sdMaterialType = sdMaterialType;
    }

    /**
     * 库存数量
     */
   /* @Excel(name = "库存数量")*/
    @ApiModelProperty("库存数量")
    private Integer inventoryQuantity;

    /**
     * 单位，废除
     */
//    @Excel(name = "单位")
    @ApiModelProperty("单位，废除")
    private String company;

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    /**
     * 位置
     */
   /* @Excel(name = "位置")*/
    @ApiModelProperty("位置")
    private String position;



    private String ids;

    public String getIds() {
        return this.ids;
    }

    public void setIds( String ids) {
        this.ids = ids;
    }

    /**
     * 警告值 ，废除
     */
//    @Excel(name = "警告值")
    @ApiModelProperty("警告值")
    private Long warningValue;

    public Long getWarningValue() {
        return warningValue;
    }

    public void setWarningValue(Long warningValue) {
        this.warningValue = warningValue;
    }

    /**
     * 编码
     */
    /*@Excel(name = "编码")*/
    @ApiModelProperty("编码")
    private String code;

    /**
     * 状态（1.物资紧张，2.正常,3.缺货）
     */
   /* @Excel(name = "状态", readConverterExp = "1=.物资紧张，2.正常,3.缺货")*/
    @ApiModelProperty("状态（1.物资紧张，2.正常,3.缺货）")
    private String state;

    /**
     * 桩号
     */
    @Excel(name = "桩号")
    @ApiModelProperty("桩号")
    private String station;

    /**
     * 方向
     */
    @Excel(name = "方向",  readConverterExp = "1=潍坊方向,2=济南方向")
    @ApiModelProperty("方向" )
    private String direction;

    /**
     * 应急资源使用数量
     */
    @Excel(name = "数量")
    @ApiModelProperty("应急资源使用数量")
    private String number;

    /**
     * 安装日期
     * */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("安装日期")
    private Date installDate;

    /**
     * 生产日期
     * */
    @Excel(name = "生产日期",width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("生产日期")
    private Date dateOfManufacture;

    public String getStation() {
        return station;
    }

    /**
     * 保质期
     * */
    @Excel(name = "保质期(月)")
    @ApiModelProperty("保质期")
    private String qualityGuaranteePeriod;

    public String getQualityGuaranteePeriod() {
        return qualityGuaranteePeriod;
    }

    public void setQualityGuaranteePeriod(String qualityGuaranteePeriod) {
        this.qualityGuaranteePeriod = qualityGuaranteePeriod;
    }

    /**
     * 所属隧道ID
     * */
    @ApiModelProperty("所属隧道ID")
    private String tunnelId;
    private String tunnelName;

    /**
     * 部门ID
     */
    @ApiModelProperty("部门ID")
    private String deptId;

    @ApiModelProperty("终端站")
    private String endStation;

    public String getEndStation() {
        return endStation;
    }

    public void setEndStation(String endStation) {
        this.endStation = endStation;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getTunnelId() {
        return tunnelId;
    }

    public void setTunnelId(String tunnelId) {
        this.tunnelId = tunnelId;
    }

    public String getTunnelName() {
        return tunnelName;
    }

    public void setTunnelName(String tunnelName) {
        this.tunnelName = tunnelName;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Date getInstallDate() {
        return installDate;
    }

    public void setInstallDate(Date installDate) {
        this.installDate = installDate;
    }

    public Date getDateOfManufacture() {
        return dateOfManufacture;
    }

    public void setDateOfManufacture(Date dateOfManufacture) {
        this.dateOfManufacture = dateOfManufacture;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialType(String materialType) {
        this.materialType = materialType;
    }

    public String getMaterialType() {
        return materialType;
    }

    public void setInventoryQuantity(Integer inventoryQuantity) {
        this.inventoryQuantity = inventoryQuantity;
    }

    public Integer getInventoryQuantity() {
        return inventoryQuantity;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPosition() {
        return position;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("materialId", getMaterialId())
                .append("materialName", getMaterialName())
                .append("materialType", getMaterialType())
                .append("sdMaterialType", getSdMaterialType())
                .append("inventoryQuantity", getInventoryQuantity())
                .append("position", getPosition())
                .append("code", getCode())
                .append("state", getState())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .append("station", getStation())
                .append("direction", getDirection())
                .append("number", getNumber())
                .append("installDate", getInstallDate())
                .append("dateOfManufacture", getDateOfManufacture())
                .append("tunnelId", getTunnelId())
                .append("tunnnelName", getTunnelName())
                .append("qualityGuaranteePeriod", getQualityGuaranteePeriod())
                .append("endStation", getEndStation())
                .toString();
    }
}
