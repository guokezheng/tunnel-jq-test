package com.tunnel.platform.domain.intelligentMonitoring;

import com.ruoyi.common.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 白名单车辆通行记录对象 vehicle_white_list_record
 *
 * @author ruoyi
 * @date 2021-11-30
 */
public class VehicleWhiteListRecordDTO
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Integer id;

    /** 车牌号 */
    @Excel(name = "车牌号")
    private String licensePlateNumber;

    /** 通行状态 1：正常 */
    @Excel(name = "通行状态 1：正常")
    private Integer status;

    /** 开始时间 **/
    private String startTime;

    /** 结束时间 **/
    private String endTime;

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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("licensePlateNumber", getLicensePlateNumber())
            .append("startTime", getStartTime())
            .append("endTime", getEndTime())
            .append("status", getStatus())
            .toString();
    }
}
