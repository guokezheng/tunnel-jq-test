package com.tunnel.business.domain.logRecord;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.utils.DictUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

@ColumnWidth(20)
public class SdOperationLogDTO
{


    @ExcelProperty(value = "隧道名称")
    private String tunnelName;
    /** equipmentType对象 */

    @ExcelProperty(value = "设备类型")
    private String typeName;

    /** tunnel对象 */
    @ExcelProperty(value = "设备名称")
    private String eqName;

    /** 方向 */
    @ExcelProperty(value = "方向")
    private String direction;

    /** 桩号 */
    @ExcelProperty(value = "桩号")
    private String pile;

    /** tunnel对象 */
    @ExcelProperty(value = "操作状态")
    private String stateName;


    /** 控制方式   3：手动 1：时间控制 2：光强控制 */
    @ExcelProperty(value = "控制方式")
    private String controlType;

    /** 操作是否成功 0 成功；1失败 */
    @ExcelProperty(value = "操作结果")
    private String state;

    /** 操作地址 */
    @ExcelProperty(value = "操作地址")
    private String operIp;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelProperty(value = "创建时间")
    private Date createTime;

    public String getTunnelName() {
        return tunnelName;
    }

    public void setTunnelName(String tunnelName) {
        this.tunnelName = tunnelName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getEqName() {
        return eqName;
    }

    public void setEqName(String eqName) {
        this.eqName = eqName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getPile() {
        return pile;
    }

    public void setPile(String pile) {
        this.pile = pile;
    }

    public String getDirection() {

        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getControlType() {

        return controlType;

    }

    public void setControlType(String controlType) {
        this.controlType = controlType;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getOperIp() {
        return operIp;
    }

    public void setOperIp(String operIp) {
        this.operIp = operIp;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}