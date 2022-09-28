package com.tunnel.business.domain.intelligentMonitoring;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * 白名单车辆通行记录对象 vehicle_white_list_record
 *
 * @author ruoyi
 * @date 2021-11-30
 */
public class VehicleWhiteListRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @Excel(name = "通行记录编号")
    private Integer id;

    /** 车牌号 */
    @Excel(name = "车牌号")
    private String licensePlateNumber;

    /** 通行状态 1：正常 2：禁止 */
    @Excel(name = "通行状态", readConverterExp = "1=正常,2=禁止")
    private Integer status;

    /** 通行时间 */
    @Excel(name = "通行时间")
    private String passTime;

    public String getPassTime() {
        return passTime;
    }

    public void setPassTime(String passTime) {
        this.passTime = passTime;
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
                .append("createTime", getCreateTime())
                .append("status", getStatus())
                .append("passTime", getPassTime())
                .toString();
    }
}
