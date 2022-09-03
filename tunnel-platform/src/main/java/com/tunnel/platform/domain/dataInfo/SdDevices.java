package com.tunnel.platform.domain.dataInfo;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excels;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 设备对象 sd_devices
 *
 * @author ruoyi
 * @date 2022-07-22
 */

@Data
@ApiModel("设备对象类")
public class SdDevices extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 设备ID
     */
    @Excel(name = "设备ID")
    @ApiModelProperty("设备ID")
    private String eqId;

    @ApiModelProperty("设备ID集合")
    private List<String> eqIds;

    /**
     * 父设备ID-PLC
     */
    @Excel(name = "父设备ID-PLC")
    @ApiModelProperty("父设备ID-PLC")
    private String fEqId;

    @ApiModelProperty("部门id")
    private Long deptId;
    @ApiModelProperty("部门名称")
    private String deptName;

    /**
     * 所属隧道 ID
     */
    @Excel(name = "所属隧道 ID")
    @ApiModelProperty("所属隧道 ID")
    private String eqTunnelId;

    /**
     * tunnel对象
     */
    @Excels({
            @Excel(name = "隧道名称", targetAttr = "tunnelName", type = Excel.Type.EXPORT),
    })
    @ApiModelProperty("tunnel对象")
    private SdTunnels tunnelName;

    /**
     * 设备名称
     */
    @Excel(name = "设备名称")
    @ApiModelProperty("设备名称")
    private String eqName;

    /**
     * 设备类型
     */
    @ApiModelProperty("设备类型")
    @Excel(name = "设备类型")
    private Long eqType;


    /**
     * eqType对象
     */
    @Excels({
            @Excel(name = "设备类型名称", targetAttr = "eqTypeName", type = Excel.Type.EXPORT),
    })
    @ApiModelProperty("eqType对象")
    private SdEquipmentType eqTypeName;

    /**
     * 设备型号
     */
    @Excel(name = "设备型号")
    @ApiModelProperty("设备型号")
    private String eqModel;

    /**
     * 设备品牌编号
     */
    @Excel(name = "设备品牌编号")
    @ApiModelProperty("设备品牌编号")
    private Long brandId;

//    /**
//     * 设备品牌
//     */
//    @Excel(name = "设备品牌")
//    private String brandName;

    /**
     * 所属道路方向(上行、下行)
     */
    @Excel(name = "所属道路方向(上行、下行)")
    @ApiModelProperty("所属道路方向(上行、下行)")
    private String eqDirection;

    /**
     * 设备所属车道
     */
    @Excel(name = "设备所属车道")
    @ApiModelProperty("设备所属车道")
    private String lane;

    /**
     * 设备桩号
     */
    @Excel(name = "设备桩号")
    @ApiModelProperty("设备桩号")
    private String pile;

    /**
     * 设备整形桩号
     */
    @Excel(name = "设备整形桩号")
    @ApiModelProperty("设备整形桩号")
    private Long pileNum;

    /**
     * 纬度
     */
    @Excel(name = "纬度")
    @ApiModelProperty("纬度")
    private String lat;

    /**
     * 经度
     */
    @Excel(name = "经度")
    @ApiModelProperty("经度")
    private String lng;

    /**
     * 设备IP
     */
    @Excel(name = "设备IP")
    @ApiModelProperty("设备IP")
    private String ip;

    /**
     * 设备端口号
     */
    @Excel(name = "设备端口号")
    @ApiModelProperty("设备端口号")
    private String port;

    /**
     * 设备密钥
     */
    @Excel(name = "设备密钥")
    @ApiModelProperty("设备密钥")
    private String secureKey;

    /**
     * 设备用户名
     */
    @Excel(name = "设备用户名")
    @ApiModelProperty("设备用户名")
    private String eqUser;

    /**
     * 设备密码
     */
    @Excel(name = "设备密码")
    @ApiModelProperty("设备密码")
    private String eqPwd;

    /**
     * 协议类型（tcp/udp/api）
     */
    @Excel(name = "协议类型")
    @ApiModelProperty("协议类型")
    private String protocol;

    /**
     * 协议通信地址
     */
    @Excel(name = "协议通信地址")
    @ApiModelProperty("协议通信地址")
    private String protocolUrl;

    /**
     * 版本号
     */
    @Excel(name = "版本号")
    @ApiModelProperty("版本号")
    private String protocolVersion;

    /**
     * 通信协议名称
     */
    @Excel(name = "通信协议名称")
    @ApiModelProperty("通信协议名称")
    private String protocolName;

    /**
     * 数据类型
     */
    @Excel(name = "数据类型")
    @ApiModelProperty("数据类型")
    private String dataType;

    /**
     * 数据采集方式
     */
    @Excel(name = "数据采集方式")
    @ApiModelProperty("数据采集方式")
    private String dataSource;

    /**
     * 出厂时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty("出厂时间")
    @Excel(name = "出厂时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date deliveryTime;

    /**
     * 维保截止时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty("维保截止时间")
    @Excel(name = "维保截止时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date warrantyEndTime;

    /**
     * 设备安装时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty("设备安装时间")
    @Excel(name = "设备安装时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date installTime;

    /**
     * 预期寿命/设计寿命,单位为年
     */
    @Excel(name = "预期寿命/设计寿命,单位为年")
    @ApiModelProperty("预期寿命/设计寿命,单位为年")
    private String useLife;

    /**
     * 使用状态:1-在用 2-停用 3-备用
     */
    @Excel(name = "使用状态:1-在用 2-停用 3-备用")
    @ApiModelProperty("使用状态:1-在用 2-停用 3-备用")
    private String useStatus;

    /**
     * 是否监控
     */
    @ApiModelProperty("是否监控")
    @Excel(name = "是否监控:0=是 1=否")
    private Long isMonitor;

    /**
     * 端口状态
     */
    @Excel(name = "端口状态")
    @ApiModelProperty("端口状态")
    private String portStatus;

    /**
     * 端口状态更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty("端口状态更新时间")
    @Excel(name = "端口状态更新时间	", width = 30, dateFormat = "yyyy-MM-dd")
    private Date portStatusTime;

    /**
     * 网关与设备连通状态	1-在线，2-离线
     */
    @Excel(name = "网关与设备连通状态	1-在线，2-离线")
    @ApiModelProperty("网关与设备连通状态\t1-在线，2-离线")
    private String gatewayNetstatus;

    /**
     * 网关状态更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "网关状态更新时间	", width = 30, dateFormat = "yyyy-MM-dd")
    @ApiModelProperty("网关状态更新时间")
    private Date gatewayNetstatusTime;

    /**
     * 设备状态	1-故障，2-告警
     */
    @Excel(name = "设备状态	1-故障，2-告警")
    @ApiModelProperty("设备状态\t1-故障，2-告警")
    private String eqStatus;

    /**
     * 设备状态更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty("设备状态更新时间")
    @Excel(name = "设备状态更新时间	", width = 30, dateFormat = "yyyy-MM-dd")
    private Date eqStatusTime;

    /**
     * 控制模式+机位
     */
    @Excel(name = "控制模式+机位")
    @ApiModelProperty("控制模式+机位")
    private String dmcontrolSeat;

    /**
     * 照明灯类型
     */
    @Excel(name = "照明灯类型")
    @ApiModelProperty("照明灯类型")
    private Long eqLampType;

    /**
     * 查询模式+机位
     */
    @Excel(name = "查询模式+机位")
    @ApiModelProperty("查询模式+机位")
    private String instructionSeat;

    /**
     * 控制点位地址
     */
    @Excel(name = "控制点位地址")
    @ApiModelProperty("控制点位地址")
    private String eqControlPointAddress;

    /**
     * 点位地址1
     */
    @Excel(name = "点位地址1")
    @ApiModelProperty("点位地址1")
    private String eqFeedbackAddress1;

    /**
     * 点位地址2
     */
    @Excel(name = "点位地址2")
    @ApiModelProperty("点位地址2")
    private String eqFeedbackAddress2;

    /**
     * 点位地址3
     */
    @Excel(name = "点位地址3")
    @ApiModelProperty("点位地址3")
    private String eqFeedbackAddress3;

    /**
     * 点位地址4
     */
    @Excel(name = "点位地址4")
    @ApiModelProperty("点位地址4")
    private String eqFeedbackAddress4;

    /**
     * 点位地址5
     */
    @Excel(name = "点位地址5")
    @ApiModelProperty("点位地址5")
    private String eqFeedbackAddress5;

    public void setEqId(String eqId) {
        this.eqId = eqId;
    }


    /**
     * 指令模式
     */
    private String instruction;
    /**
     * 机位
     **/
    private String seat;
    /**
     * 查询个数
     **/
    private String qNumber;

    //控制状态
    @ApiModelProperty("控制状态:1-手动控制 2-自动控制")
    private String controlStatus;

    public String getControlStatus() {
        return controlStatus;
    }

    public void setControlStatus(String controlStatus) {
        this.controlStatus = controlStatus;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getSeat() {
        return seat;
    }

    public void setSeat(String seat) {
        this.seat = seat;
    }

    public String getqNumber() {
        return qNumber;
    }

    public void setqNumber(String qNumber) {
        this.qNumber = qNumber;
    }


    public String getEqId() {
        return eqId;
    }

    public List<String> getEqIds() {
        return eqIds;
    }

    public void setEqIds(List<String> eqIds) {
        this.eqIds = eqIds;
    }

    public String getFEqId() {
        return fEqId;
    }

    public void setFEqId(String fEqId) {
        this.fEqId = fEqId;
    }

    public void setEqTunnelId(String eqTunnelId) {
        this.eqTunnelId = eqTunnelId;
    }

    public String getEqTunnelId() {
        return eqTunnelId;
    }

    public void setEqName(String eqName) {
        this.eqName = eqName;
    }


    public SdTunnels getTunnelName() {
        if (tunnelName == null) {
            tunnelName = new SdTunnels();
        }
        return tunnelName;
    }

    public void setTunnelName(SdTunnels tunnelName) {
        this.tunnelName = tunnelName;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }


    public String getEqName() {
        return eqName;
    }

    public void setEqType(Long eqType) {
        this.eqType = eqType;
    }

    public Long getEqType() {
        return eqType;
    }

    public void setEqModel(String eqModel) {
        this.eqModel = eqModel;
    }

    public SdEquipmentType getTypeName() {
        if (eqTypeName == null) {
            eqTypeName = new SdEquipmentType();
        }
        return eqTypeName;

    }

    public void setTypeName(SdEquipmentType eqTypeName) {
        this.eqTypeName = eqTypeName;
    }

    public String getEqModel() {
        return eqModel;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public Long getBrandId() {
        return brandId;
    }

//    public String getBrandName() {
//        return brandName;
//    }
//
//    public void setBrandName(String brandName) {
//        this.brandName = brandName;
//    }

    public void setEqDirection(String eqDirection) {
        this.eqDirection = eqDirection;
    }

    public String getEqDirection() {
        return eqDirection;
    }

    public void setLane(String lane) {
        this.lane = lane;
    }

    public String getLane() {
        return lane;
    }

    public void setPile(String pile) {
        this.pile = pile;
    }

    public String getPile() {
        return pile;
    }

    public void setPileNum(Long pileNum) {
        this.pileNum = pileNum;
    }

    public Long getPileNum() {
        return pileNum;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLat() {
        return lat;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLng() {
        return lng;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getIp() {
        return ip;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getPort() {
        return port;
    }

    public void setSecureKey(String secureKey) {
        this.secureKey = secureKey;
    }

    public String getSecureKey() {
        return secureKey;
    }

    public void setEqUser(String eqUser) {
        this.eqUser = eqUser;
    }

    public String getEqUser() {
        return eqUser;
    }

    public void setEqPwd(String eqPwd) {
        this.eqPwd = eqPwd;
    }

    public String getEqPwd() {
        return eqPwd;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocolUrl(String protocolUrl) {
        this.protocolUrl = protocolUrl;
    }

    public String getProtocolUrl() {
        return protocolUrl;
    }

    public void setProtocolVersion(String protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public String getProtocolVersion() {
        return protocolVersion;
    }

    public void setProtocolName(String protocolName) {
        this.protocolName = protocolName;
    }

    public String getProtocolName() {
        return protocolName;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDeliveryTime(Date deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public Date getDeliveryTime() {
        return deliveryTime;
    }

    public void setWarrantyEndTime(Date warrantyEndTime) {
        this.warrantyEndTime = warrantyEndTime;
    }

    public Date getWarrantyEndTime() {
        return warrantyEndTime;
    }

    public void setInstallTime(Date installTime) {
        this.installTime = installTime;
    }

    public Date getInstallTime() {
        return installTime;
    }

    public void setUseLife(String useLife) {
        this.useLife = useLife;
    }

    public String getUseLife() {
        return useLife;
    }

    public void setUseStatus(String useStatus) {
        this.useStatus = useStatus;
    }

    public String getUseStatus() {
        return useStatus;
    }

    public void setPortStatus(String portStatus) {
        this.portStatus = portStatus;
    }

    public Long getIsMonitor() {
        return isMonitor;
    }

    public void setIsMonitor(Long isMonitor) {
        this.isMonitor = isMonitor;
    }

    public String getPortStatus() {
        return portStatus;
    }

    public void setPortStatusTime(Date portStatusTime) {
        this.portStatusTime = portStatusTime;
    }

    public Date getPortStatusTime() {
        return portStatusTime;
    }

    public void setGatewayNetstatus(String gatewayNetstatus) {
        this.gatewayNetstatus = gatewayNetstatus;
    }

    public String getGatewayNetstatus() {
        return gatewayNetstatus;
    }

    public void setGatewayNetstatusTime(Date gatewayNetstatusTime) {
        this.gatewayNetstatusTime = gatewayNetstatusTime;
    }

    public Date getGatewayNetstatusTime() {
        return gatewayNetstatusTime;
    }

    public void setEqStatus(String eqStatus) {
        this.eqStatus = eqStatus;
    }

    public String getEqStatus() {
        return eqStatus;
    }

    public void setEqStatusTime(Date eqStatusTime) {
        this.eqStatusTime = eqStatusTime;
    }

    public Date getEqStatusTime() {
        return eqStatusTime;
    }

    public void setDmcontrolSeat(String dmcontrolSeat) {
        this.dmcontrolSeat = dmcontrolSeat;
    }

    public String getDmcontrolSeat() {
        return dmcontrolSeat;
    }

    public void setEqLampType(Long eqLampType) {
        this.eqLampType = eqLampType;
    }

    public Long getEqLampType() {
        return eqLampType;
    }

    public void setInstructionSeat(String instructionSeat) {
        this.instructionSeat = instructionSeat;
    }

    public String getInstructionSeat() {
        return instructionSeat;
    }

    public void setEqControlPointAddress(String eqControlPointAddress) {
        this.eqControlPointAddress = eqControlPointAddress;
    }

    public String getEqControlPointAddress() {
        return eqControlPointAddress;
    }

    public void setEqFeedbackAddress1(String eqFeedbackAddress1) {
        this.eqFeedbackAddress1 = eqFeedbackAddress1;
    }

    public String getEqFeedbackAddress1() {
        return eqFeedbackAddress1;
    }

    public void setEqFeedbackAddress2(String eqFeedbackAddress2) {
        this.eqFeedbackAddress2 = eqFeedbackAddress2;
    }

    public String getEqFeedbackAddress2() {
        return eqFeedbackAddress2;
    }

    public void setEqFeedbackAddress3(String eqFeedbackAddress3) {
        this.eqFeedbackAddress3 = eqFeedbackAddress3;
    }

    public String getEqFeedbackAddress3() {
        return eqFeedbackAddress3;
    }

    public void setEqFeedbackAddress4(String eqFeedbackAddress4) {
        this.eqFeedbackAddress4 = eqFeedbackAddress4;
    }

    public String getEqFeedbackAddress4() {
        return eqFeedbackAddress4;
    }

    public void setEqFeedbackAddress5(String eqFeedbackAddress5) {
        this.eqFeedbackAddress5 = eqFeedbackAddress5;
    }

    public String getEqFeedbackAddress5() {
        return eqFeedbackAddress5;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("eqId", getEqId())
                .append("fEqId", getFEqId())
                .append("eqTunnelId", getEqTunnelId())
                .append("eqName", getEqName())
                .append("eqType", getEqType())
                .append("eqModel", getEqModel())
                .append("brandId", getBrandId())
//                .append("brand", getBrand())
                .append("eqDirection", getEqDirection())
                .append("eqLane", getLane())
                .append("pile", getPile())
                .append("pileNum", getPileNum())
                .append("lat", getLat())
                .append("lng", getLng())
                .append("ip", getIp())
                .append("port", getPort())
                .append("secureKey", getSecureKey())
                .append("eqUser", getEqUser())
                .append("eqPwd", getEqPwd())
                .append("protocol", getProtocol())
                .append("protocolUrl", getProtocolUrl())
                .append("protocolVersion", getProtocolVersion())
                .append("protocolName", getProtocolName())
                .append("dataType", getDataType())
                .append("dataSource", getDataSource())
                .append("deliveryTime", getDeliveryTime())
                .append("warrantyEndTime", getWarrantyEndTime())
                .append("installTime", getInstallTime())
                .append("useLife", getUseLife())
                .append("useStatus", getUseStatus())
                .append("isMonitor", getIsMonitor())
                .append("portStatus", getPortStatus())
                .append("portStatusTime", getPortStatusTime())
                .append("gatewayNetstatus", getGatewayNetstatus())
                .append("gatewayNetstatusTime", getGatewayNetstatusTime())
                .append("eqStatus", getEqStatus())
                .append("eqStatusTime", getEqStatusTime())
                .append("dmcontrolSeat", getDmcontrolSeat())
                .append("eqLampType", getEqLampType())
                .append("instructionSeat", getInstructionSeat())
                .append("eqControlPointAddress", getEqControlPointAddress())
                .append("eqFeedbackAddress1", getEqFeedbackAddress1())
                .append("eqFeedbackAddress2", getEqFeedbackAddress2())
                .append("eqFeedbackAddress3", getEqFeedbackAddress3())
                .append("eqFeedbackAddress4", getEqFeedbackAddress4())
                .append("eqFeedbackAddress5", getEqFeedbackAddress5())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
