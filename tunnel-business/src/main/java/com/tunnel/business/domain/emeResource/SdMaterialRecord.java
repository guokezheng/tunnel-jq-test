package com.tunnel.business.domain.emeResource;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 出入库记录对象 sd_material_record
 *
 * @author xuebi
 * @date 2020-08-28
 */
@ApiModel("出入库记录类")
public class SdMaterialRecord extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 出入库记录Id
     */
    @ApiModelProperty("出入库记录Id")
    private Long id;

    /**
     * 物资编号
     */
    @Excel(name = "物资ID")
    @ApiModelProperty("物资编号")
    private Long materialId;

    /**
     * 用户id
     */
    @Excel(name = "用户id")
    @ApiModelProperty("用户id")
    private Long userId;

    /**
     * 用户姓名
     */
    @Excel(name = "用户姓名")
    @ApiModelProperty("用户姓名")
    private String userName;

    /**
     * 库存数量
     */
    @Excel(name = "库存数量")
    @ApiModelProperty("库存数量")
    private Integer stock;

    /**
     * 改变库存数
     */
    @Excel(name = "改变库存数")
    @ApiModelProperty("改变库存数")
    private Integer changeStock;

    /**
     * 出库/入库
     */
    @Excel(name = "出库/入库")
    @ApiModelProperty("出库/入库")
    private String type;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
    }

    public Long getMaterialId() {
        return materialId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getStock() {
        return stock;
    }

    public void setChangeStock(Integer changeStock) {
        this.changeStock = changeStock;
    }

    public Integer getChangeStock() {
        return changeStock;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("materialId", getMaterialId())
                .append("userId", getUserId())
                .append("userName", getUserName())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("stock", getStock())
                .append("changeStock", getChangeStock())
                .append("type", getType())
                .append("remark", getRemark())
                .toString();
    }
}