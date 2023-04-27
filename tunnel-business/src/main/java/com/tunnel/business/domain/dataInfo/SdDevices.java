package com.tunnel.business.domain.dataInfo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excels;
import com.ruoyi.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * 设备对象 sd_devices
 *
 * @author ruoyi
 * @date 2022-07-22
 */

@ApiModel("设备对象类")
public class SdDevices<SdEquipmentStateIconFile> extends BaseEntity {
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
     * plc主机
     */
    @Excel(name = "plc主机",type = Excel.Type.IMPORT)
    @ApiModelProperty("plc主机")
    private String fEqId;

    @ApiModelProperty("部门id")
    private String deptId;
    @ApiModelProperty("部门名称")
    private String deptName;

    /**
     * 所属隧道 ID
     */
    @Excel(name = "所属隧道",type = Excel.Type.IMPORT)
    @ApiModelProperty("所属隧道")
    private String eqTunnelId;

    /**
     * tunnel对象
     */
    @Excels({
            @Excel(name = "所属隧道", targetAttr = "tunnelName", type = Excel.Type.EXPORT),
    })
    @ApiModelProperty("tunnel对象")
    private SdTunnels tunnelName;

    /**
     * 设备名称
     */
    @Excel(name = "设备名称")
    @ApiModelProperty("设备名称")
    private String eqName;


    @ApiModelProperty("隧道名称")
    private String tunnel;

    public String getTunnel() {
        return this.tunnel;
    }

    public void setTunnel(final String tunnel) {
        this.tunnel = tunnel;
    }

    /**
     * 设备类型
     */
    @ApiModelProperty("设备类型")
    @Excel(name = "设备类型",type= Excel.Type.IMPORT)
    private Long eqType;


    private Long[] eqTypes;


    /**
     * eqType对象
     */
    @Excels({
            @Excel(name = "设备类型", targetAttr = "typeName", type = Excel.Type.EXPORT),
    })
    @ApiModelProperty("eqType对象")
    private SdEquipmentType eqTypeName;

    /**
     * 设备品牌
     */
    @Excel(name = "设备品牌ID",type = Excel.Type.IMPORT)
    @ApiModelProperty("设备品牌")
    private String brandId;

    /**
     * 设备品牌
     */
    @Excel(name = "设备品牌")
    @ApiModelProperty("设备品牌")
    private String brandName;



    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    /**
     * 设备型号
     */
    @Excel(name = "设备型号")
    @ApiModelProperty("设备型号")
    private String eqModel;



    /**
     * 外部系统ID
     */
    @Excel(name = "外部系统",type= Excel.Type.IMPORT)
    @ApiModelProperty("外部系统")
    private Long externalSystemId;


    /**
     * 设备大类
     */
   /* @Excel(name = "设备大类",type= Excel.Type.IMPORT)*/
    @ApiModelProperty("设备大类")
    private Long fEqType;

    /**
     * 设备大类
     */
    @Excel(name = "设备大类",type= Excel.Type.IMPORT)
    @ApiModelProperty("设备大类")
    private Long FEqType;

    /**
     * 所属道路方向(上行、下行)
     */
    @Excel(name = "设备方向", dictType = "sd_direction")
    @ApiModelProperty("设备方向")
    private String eqDirection;

    @Excel(name = "设备方向：1-潍坊方向 2-济南方向",type= Excel.Type.IMPORT)
    @ApiModelProperty("设备方向：1-潍坊方向 2-济南方向")
    private String direction;

    public String getDirection() {
        return this.direction;
    }

    public void setDirection( String direction) {
        this.direction = direction;
    }

    /**
     * 设备所属车道
     */
    @Excel(name = "所属车道",type= Excel.Type.IMPORT)
    @ApiModelProperty("所属车道")
    private String lane;

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
     * 设备桩号
     */
    @Excel(name = "桩号")
    @ApiModelProperty("桩号")
    private String pile;

    @Excel(name = "备注")
    private String remark;



    /**
     * 设备整形桩号
     */
    @Excel(name = "设备整形桩号",type= Excel.Type.IMPORT)
    @ApiModelProperty("设备整形桩号")
    private Long pileNum;

    /**
     * 纬度
     */
    @Excel(name = "纬度",type= Excel.Type.IMPORT)
    @ApiModelProperty("纬度")
    private String lat;

    /**
     * 经度
     */
    @Excel(name = "经度",type= Excel.Type.IMPORT)
    @ApiModelProperty("经度")
    private String lng;



    /**
     * 设备密钥
     */
    @Excel(name = "设备密钥",type= Excel.Type.IMPORT)
    @ApiModelProperty("设备密钥")
    private String secureKey;

    /**
     * 设备用户名
     */
    @Excel(name = "设备用户名",type= Excel.Type.IMPORT)
    @ApiModelProperty("设备用户名")
    private String eqUser;

    /**
     * 设备密码
     */
    @Excel(name = "设备密码",type= Excel.Type.IMPORT)
    @ApiModelProperty("设备密码")
    private String eqPwd;

    /**
     * 协议类型（tcp/udp/api）
     */
    @Excel(name = "协议类型",type= Excel.Type.IMPORT)
    @ApiModelProperty("协议类型")
    private String commProtocol;

    /**
     * 出厂时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty("出厂时间")
    @Excel(name = "出厂时间", width = 30, dateFormat = "yyyy-MM-dd",type= Excel.Type.IMPORT)
    private Date deliveryTime;

    /**
     * 维保截止时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty("维保截止时间")
    @Excel(name = "维保截止时间", width = 30, dateFormat = "yyyy-MM-dd",type= Excel.Type.IMPORT)
    private Date warrantyEndTime;

    /**
     * 设备安装时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty("设备安装时间")
    @Excel(name = "设备安装时间", width = 30, dateFormat = "yyyy-MM-dd",type= Excel.Type.IMPORT)
    private Date installTime;

    /**
     * 预期寿命/设计寿命,单位为年
     */
    @Excel(name = "预期寿命",type= Excel.Type.IMPORT)
    @ApiModelProperty("预期寿命/设计寿命,单位为年")
    private String useLife;

    /**
     * 使用状态:1-在用 2-停用 3-备用
     */
    @Excel(name = "使用状态:1-在用 2-停用 3-备用",type= Excel.Type.IMPORT)
    @ApiModelProperty("使用状态:1-在用 2-停用 3-备用")
    private String useStatus;

    /**
     * 是否监控
     */
    @ApiModelProperty("是否监控")
    @Excel(name = "是否监控:0=是 1=否",type= Excel.Type.IMPORT)
    private Long isMonitor;

    /**
     * 端口状态
     */
    @Excel(name = "端口状态",type= Excel.Type.IMPORT)
    @ApiModelProperty("端口状态")
    private String portStatus;

    /**
     * 端口状态更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty("端口状态更新时间")
    @Excel(name = "端口状态更新时间	", width = 30, dateFormat = "yyyy-MM-dd",type= Excel.Type.IMPORT)
    private Date portStatusTime;

    /**
     * 网关与设备连通状态	1-在线，2-离线
     */
    @Excel(name = "网关与设备连通状态	1-在线，2-离线",type= Excel.Type.IMPORT)
    @ApiModelProperty("网关与设备连通状态\t1-在线，2-离线")
    private String gatewayNetstatus;

    /**
     * 网关状态更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "网关状态更新时间	", width = 30, dateFormat = "yyyy-MM-dd",type= Excel.Type.IMPORT)
    @ApiModelProperty("网关状态更新时间")
    private Date gatewayNetstatusTime;

    /**
     * 设备状态	1-故障，2-告警
     */
    @Excel(name = "设备状态	1-故障，2-告警",type= Excel.Type.IMPORT)
    @ApiModelProperty("设备状态\t1-故障，2-告警")
    private String eqStatus;

    /**
     * 设备状态更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty("设备状态更新时间")
    @Excel(name = "设备状态更新时间	", width = 30, dateFormat = "yyyy-MM-dd",type= Excel.Type.IMPORT)
    private Date eqStatusTime;

    /**
     * 控制点位地址
     */
    @Excel(name = "控制点位地址",type= Excel.Type.IMPORT)
    @ApiModelProperty("控制点位地址")
    private String controlPointAddress;

    /**
     * 点位地址1
     */
    @Excel(name = "点位地址",type= Excel.Type.IMPORT)
    @ApiModelProperty("点位地址")
    private String queryPointAddress;

    /**
     * 是否更新
     */
    private boolean updateSupport;



    /** 图片id */
    @Excel(name = "图片id",type= Excel.Type.IMPORT)
    private String iconFileId;

    @ApiModelProperty("设备类型图片")
    private List<SdEquipmentStateIconFile> iFileList;

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

    /**
     * 消息协议（Mobdus/Fins/API/自定义）
     * */
    private String messageProtocol;

    /**
     * 协议ID
     * */
    private Long protocolId;

    /**
     * road_id路段ID
     * */
    private String roadId;

    /**
     * 设备唯一标识码
     * */
    private String sn;

    /**
     * 外部设备ID
     **/
    private String externalDeviceId;

    /**
     * 导出设备ID
     **/
    private String exportIds;


    public Long[] getEqTypes() {
        return eqTypes;
    }

    public void setEqTypes(Long[] eqTypes) {
        this.eqTypes = eqTypes;
    }

    public Long getFEqType() {
        return this.FEqType;
    }

    public void setFEqType( Long FEqType) {
        this.FEqType = FEqType;
    }

    public String getExportIds() {
        return this.exportIds;
    }

    public void setExportIds(final String exportIds) {
        this.exportIds = exportIds;
    }

    @Override
    public String getRemark() {
        return this.remark;
    }

    @Override
    public void setRemark(final String remark) {
        this.remark = remark;
    }

    /**
     * 关联设备ID（关联iot_board中device_id字段）
     * */
    private Long associatedDeviceId;

    public Long getAssociatedDeviceId() {
        return associatedDeviceId;
    }

    public void setAssociatedDeviceId(Long associatedDeviceId) {
        this.associatedDeviceId = associatedDeviceId;
    }

    public String getExternalDeviceId() {
        if(externalDeviceId == null){
            return "-1";
        }
        return externalDeviceId;
    }

    public void setExternalDeviceId(String externalDeviceId) {
        this.externalDeviceId = externalDeviceId;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getRoadId() {
        return roadId;
    }

    public void setRoadId(String roadId) {
        this.roadId = roadId;
    }

    public Long getProtocolId() {
        return protocolId;
    }
    /**
     * 在线数量
     **/
    private Integer zxNum;

    /**
     * 离线数量
     **/
    private Integer lxNum;

    /**
     * 运行状态
     **/
    private String runStatus;


    private String runState;

    private String eqState;
    //摄像机类型
    private String camType;

    //设备  当前数据
    private String data;

    /** 设备数据项id */
    private Integer itemId;

    public String getRunState() {
        return this.runState;
    }

    public void setRunState(final String runState) {
        this.runState = runState;
    }

    public String getEqState() {
        return this.eqState;
    }

    public void setEqState(final String eqState) {
        this.eqState = eqState;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCamType() {
        return camType;
    }

    public void setCamType(String camType) {
        this.camType = camType;
    }

    public String getRunStatus() {
        return this.runStatus;
    }

    public void setRunStatus(final String runStatus) {
        this.runStatus = runStatus;
    }

    public Integer getZxNum() {
        return this.zxNum;
    }

    public void setZxNum(final Integer zxNum) {
        this.zxNum = zxNum;
    }

    public Integer getLxNum() {
        return this.lxNum;
    }

    public void setLxNum(final Integer lxNum) {
        this.lxNum = lxNum;
    }

    public void setProtocolId(Long protocolId) {
        this.protocolId = protocolId;
    }

    public String getMessageProtocol() {
        return messageProtocol;
    }

    public void setMessageProtocol(String messageProtocol) {
        this.messageProtocol = messageProtocol;
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

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
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

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getBrandId() {
        return brandId;
    }

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

    public void setCommProtocol(String commProtocol) {
        this.commProtocol = commProtocol;
    }

    public String getCommProtocol() {
        return commProtocol;
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

    public void setControlPointAddress(String controlPointAddress) {
        this.controlPointAddress = controlPointAddress;
    }

    public String getControlPointAddress() {
        return controlPointAddress;
    }

    public void setQueryPointAddress(String queryPointAddress) {
        this.queryPointAddress = queryPointAddress;
    }

    public String getQueryPointAddress() {
        return queryPointAddress;
    }

    public String getfEqId() {
        return fEqId;
    }

    public void setfEqId(String fEqId) {
        this.fEqId = fEqId;
    }

    public SdEquipmentType getEqTypeName() {
        return eqTypeName;
    }

    public void setEqTypeName(SdEquipmentType eqTypeName) {
        this.eqTypeName = eqTypeName;
    }

    public boolean isUpdateSupport() {
        return updateSupport;
    }

    public void setUpdateSupport(boolean updateSupport) {
        this.updateSupport = updateSupport;
    }

    public Long getExternalSystemId() {
        return externalSystemId;
    }

    public void setExternalSystemId(Long externalSystemId) {
        this.externalSystemId = externalSystemId;
    }

    public Long getfEqType() {
        return fEqType;
    }

    public void setfEqType(Long fEqType) {
        this.fEqType = fEqType;
    }

    public String getIconFileId() {
        return this.iconFileId;
    }

    public void setIconFileId(final String iconFileId) {
        this.iconFileId = iconFileId;
    }

    public List<SdEquipmentStateIconFile> getiFileList() {
        return this.iFileList;
    }

    public void setiFileList(final List<SdEquipmentStateIconFile> iFileList) {
        this.iFileList = iFileList;
    }


    @Override
    public String toString() {
        return "SdDevices{" +
                "eqId='" + eqId + '\'' +
                ", eqIds=" + eqIds +
                ", fEqId='" + fEqId + '\'' +
                ", deptId=" + deptId +
                ", deptName='" + deptName + '\'' +
                ", eqTunnelId='" + eqTunnelId + '\'' +
                ", tunnelName=" + tunnelName +
                ", eqName='" + eqName + '\'' +
                ", eqType=" + eqType +
                ", eqTypeName=" + eqTypeName +
                ", eqModel='" + eqModel + '\'' +
                ", brandId=" + brandId +
                ", externalSystemId=" + externalSystemId +
                ", fEqType=" + fEqType +
                ", eqDirection='" + eqDirection + '\'' +
                ", lane='" + lane + '\'' +
                ", pile='" + pile + '\'' +
                ", pileNum=" + pileNum +
                ", lat='" + lat + '\'' +
                ", lng='" + lng + '\'' +
                ", ip='" + ip + '\'' +
                ", port='" + port + '\'' +
                ", secureKey='" + secureKey + '\'' +
                ", eqUser='" + eqUser + '\'' +
                ", eqPwd='" + eqPwd + '\'' +
                ", commProtocol='" + commProtocol + '\'' +
                ", deliveryTime=" + deliveryTime +
                ", warrantyEndTime=" + warrantyEndTime +
                ", installTime=" + installTime +
                ", useLife='" + useLife + '\'' +
                ", useStatus='" + useStatus + '\'' +
                ", isMonitor=" + isMonitor +
                ", portStatus='" + portStatus + '\'' +
                ", portStatusTime=" + portStatusTime +
                ", gatewayNetstatus='" + gatewayNetstatus + '\'' +
                ", gatewayNetstatusTime=" + gatewayNetstatusTime +
                ", eqStatus='" + eqStatus + '\'' +
                ", eqStatusTime=" + eqStatusTime +
                ", controlPointAddress='" + controlPointAddress + '\'' +
                ", queryPointAddress='" + queryPointAddress + '\'' +
                ", instruction='" + instruction + '\'' +
                ", seat='" + seat + '\'' +
                ", qNumber='" + qNumber + '\'' +
                ", updateSupport='" + updateSupport + '\'' +
                '}';
    }
}
