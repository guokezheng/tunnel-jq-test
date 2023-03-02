package com.tunnel.business.domain.dataInfo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 设备实时数据（存储模拟量）对象 sd_device_data
 *
 * @author ruoyi
 * @date 2022-09-13
 */
public class SdDeviceDWData
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 设备id */
    private String deviceId;


    @Excel(name = "设备编码")
    private String eqId;

    public String getEqId() {
        return this.eqId;
    }

    public void setEqId(String eqId) {
        this.eqId = eqId;
    }
    /** 设备数据项id */
    private Long itemId;



    /** 设备名称 */
    @Excel(name = "设备名称")
    private String eqName;

    /** 所属隧道 */
    @Excel(name = "所属隧道")
    private String tunnelName;

    /** 管理机构 */
    @Excel(name = "管理机构")
    private String deptName;

    /** 方向 */
    @Excel(name = "方向")
    private String direction;

    /** 桩号 */
    @Excel(name = "桩号")
    private String pile;

    /** 设备数据 */
    @Excel(name = "洞外亮度(cd/㎡)")
    private String data;

    /** 创建时间 */
    @Excel(name = "采集时间", width = 30)
    private String createTime;


    private String ids;

    public String getIds() {
        return this.ids;
    }

    public void setIds( String ids) {
        this.ids = ids;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime( String createTime) {
        this.createTime = createTime;
    }

    private Long deptId;

    private String dept;

    private String tunnelId;

    private String searchValue;

    private String eqType;

    public String getEqName() {
        return this.eqName;
    }

    public void setEqName( String eqName) {
        this.eqName = eqName;
    }

    public String getTunnelName() {
        return this.tunnelName;
    }

    public void setTunnelName( String tunnelName) {
        this.tunnelName = tunnelName;
    }

    public String getDeptName() {
        return this.deptName;
    }

    public void setDeptName( String deptName) {
        this.deptName = deptName;
    }

    public String getDirection() {
        return this.direction;
    }

    public void setDirection( String direction) {
        this.direction = direction;
    }

    public String getEqType() {
        return this.eqType;
    }

    public void setEqType(String eqType) {
        this.eqType = eqType;
    }

    public String getDept() {
        return this.dept;
    }

    public void setDept( String dept) {
        this.dept = dept;
    }

    public String getPile() {
        return pile;
    }

    public void setPile(String pile) {
        this.pile = pile;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getTunnelId() {
        return tunnelId;
    }

    public void setTunnelId(String tunnelId) {
        this.tunnelId = tunnelId;
    }

    public String getSearchValue() {
        return searchValue;
    }

    public void setSearchValue(String searchValue) {
        this.searchValue = searchValue;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setDeviceId(String deviceId)
    {
        this.deviceId = deviceId;
    }

    public String getDeviceId()
    {
        return deviceId;
    }
    public void setItemId(Long itemId)
    {
        this.itemId = itemId;
    }

    public Long getItemId()
    {
        return itemId;
    }
    public void setData(String data)
    {
        this.data = data;
    }

    public String getData()
    {
        return data;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("deviceId", getDeviceId())
            .append("itemId", getItemId())
            .append("data", getData())
            .append("createTime", getCreateTime())
            .toString();
    }
}
