package com.tunnel.business.domain.intelligentMonitoring;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 车辆通行白名单对象 vehicle_white_list
 *
 * @author ruoyi
 * @date 2021-11-30
 */
public class VehicleWhiteList extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Integer id;

    /** 车牌号 */
    @Excel(name = "车牌号")
    private String licensePlateNumber;

    /** 最近一次通行时间 */
    @Excel(name = "最近一次通行时间")
    private String lastPassTime;

    /** 最近一次通行状态 1:正常 */
    @Excel(name = "最近一次通行状态", readConverterExp = "1=正常")
    private Integer lastPassStatus;

    /** 状态 1:正常 2:禁用 */
    @Excel(name = "状态", readConverterExp = "1=正常,2=禁用")
    private Integer status;

    /** 车型 */
    private String carModel;

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getId()
    {
        return id;
    }
    public void setLicensePlateNumber(String licensePlateNumber)
    {
        this.licensePlateNumber = licensePlateNumber;
    }

    public String getLicensePlateNumber()
    {
        return licensePlateNumber;
    }
    public void setLastPassTime(String lastPassTime)
    {
        this.lastPassTime = lastPassTime;
    }

    public String getLastPassTime()
    {
        return lastPassTime;
    }
    public void setLastPassStatus(Integer lastPassStatus)
    {
        this.lastPassStatus = lastPassStatus;
    }

    public Integer getLastPassStatus()
    {
        return lastPassStatus;
    }
    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public Integer getStatus()
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("licensePlateNumber", getLicensePlateNumber())
                .append("lastPassTime", getLastPassTime())
                .append("createTime", getCreateTime())
                .append("lastPassStatus", getLastPassStatus())
                .append("status", getStatus())
                .append("carModel", getCarModel())
                .toString();
    }
}
