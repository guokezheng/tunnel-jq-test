package com.tunnel.webthings.vo;

import io.swagger.annotations.ApiModelProperty;

import java.util.Date;


/**
 * @author dzy
 * @date 2022/8/23 8:51
 */
public class ResponseVO {

    private static final long serialVersionUID =-1242493306307174690L;

    @ApiModelProperty("就近相机编号")
    private String cameraId;

    @ApiModelProperty("所属分公司")
    private String companyId;

    @ApiModelProperty("countryId")
    private String countryId;

    @ApiModelProperty("countryName")
    private String countryName;

    @ApiModelProperty("数据采集方式")
    private String dataSource;

    @ApiModelProperty("数据类型")
    private String dataType;

    @ApiModelProperty("出厂时间")
    private String deliveryTime;

    //出厂时间
    private Date dyTime;

    @ApiModelProperty("设备类型（大类）")
    private String devCategory;

    @ApiModelProperty("设备型号")
    private String devModel;

    @ApiModelProperty("设备编号")
    private String devNo;

    @ApiModelProperty("设备密码")
    private String devPwd;

    @ApiModelProperty("设备状态 1-故障，2-告警")
    private String devStatus;

    @ApiModelProperty("设备状态更新时间 超过半小时未更新应视为断开")
    private String devStatusTime;

    //设备状态更新时间 超过半小时未更新应视为断开
    private Date dStatusTime;

    @ApiModelProperty("设备类型")
    private String devType;

    //设备类型
    private Integer dType;

    @ApiModelProperty("设备用户名")
    private String devUsr;

    @ApiModelProperty("边缘与设备连通状态 1-在线，2-离线")
    private String edgeNetstatus;

    @ApiModelProperty("边缘状态更新时间 超过半小时未更新应视为断开")
    private String edgeNetstatusTime;

    @ApiModelProperty("海拔")
    private String ele;

    @ApiModelProperty("终止桩号")
    private String endPile;

    @ApiModelProperty("安装设施编码")
    private String facilitiesId;

    @ApiModelProperty("设备安装序号 竖杆：从上往下，依次递增； 横杆：按所在车道号")
    private Integer facilitiesNum;

    @ApiModelProperty("设施归属类型1-道路设备，2-收费站，3-服务区，4-隧道，5-桥梁，6-收费广场，7-ETC门架，8移动视频源")
    private String facilitiesType;

    private String fdevNo;

    @ApiModelProperty("网关与设备连通状态 1-在线，2-离线")
    private String gatewayNetstatus;

    @ApiModelProperty("网关状态更新时间 超过半小时未更新应视为断开")
    private String gatewayNetstatusTime;

    //网关状态更新时间 超过半小时未更新应视为断开
    private Date gNetstatusTime;

    @ApiModelProperty("设备图标")
    private String icon;

    @ApiModelProperty("设备安装时间")
    private String installTime;

    //设备安装时间
    private Date inTime;

    @ApiModelProperty("设备IP")
    private String ip;

    @ApiModelProperty("是否监测 0-全部监测 1-只监测网络 2-只监测端口 3-不监测")
    private Integer isMonitor;

    @ApiModelProperty("纬度")
    private String lat;

    @ApiModelProperty("经度")
    private String lng;

    @ApiModelProperty("安装设施类型 1-道路中间，2-路侧竖杆，3-单侧门架，4-收费站内广场，5-收费站外广场")
    private String location;

    @ApiModelProperty("设备监测方向 车头方向、车尾方向、双向")
    private Integer monitorDirection;

    @ApiModelProperty("opmaCliqueId")
    private String opmaCliqueId;

    @ApiModelProperty("opmaCliqueName")
    private String opmaCliqueName;

    @ApiModelProperty("opmaManagerCropId")
    private String opmaManagerCropId;

    @ApiModelProperty("opmaManagerCropName")
    private String opmaManagerCropName;

    @ApiModelProperty("opmaManagerId")
    private String opmaManagerId;

    @ApiModelProperty("opmaManagerName")
    private String opmaManagerName;

    @ApiModelProperty("opmaSectionId")
    private String opmaSectionId;

    @ApiModelProperty("opmaSectionName")
    private String opmaSectionName;

    @ApiModelProperty("opmaSubSectionId")
    private String opmaSubSectionId;

    @ApiModelProperty("opmaXxsubcenterId")
    private String opmaXxsubcenterId;

    @ApiModelProperty("opmaXxsubcenterName")
    private String opmaXxsubcenterName;

    @ApiModelProperty("设备原值，一般为采购价格")
    private Double originalValue;

    @ApiModelProperty("所属业主")
    private String ownerId;

    @ApiModelProperty("设备图片 图片的路径")
    private String picture;

    @ApiModelProperty("桩号")
    private String pile;

    @ApiModelProperty("整形桩号（米）")
    private String pileNum;

    //整形桩号（米）
    private Integer pNum;

    @ApiModelProperty("设备端口号")
    private String port;

    @ApiModelProperty("端口状态")
    private String portStatus;

    //端口状态更新时间 超过半小时未更新应视为断
    private Date pStatusTime;

    @ApiModelProperty("端口状态更新时间 超过半小时未更新应视为断")
    private String portStatusTime;

    @ApiModelProperty("相机预置位")
    private Integer presetPosition;

    @ApiModelProperty("协议类型tcp/UDP")
    private String protocol;

    @ApiModelProperty("通信协议名称")
    private String protocolName;

    @ApiModelProperty("版本号")
    private  String protocolVersion;

    @ApiModelProperty("regionId")
    private  String regionId;

    @ApiModelProperty("regionName")
    private  String regionName;

    @ApiModelProperty("道路方向（上下行）")
    private  Integer roadDirection;

    //道路方向（上下行）
    private String rDirection;

    @ApiModelProperty("道路编号 如：G3")
    private  String roadId;

    @ApiModelProperty("道路名称")
    private  String roadName;

    @ApiModelProperty("所属路段")
    private  String sectionId;

    @ApiModelProperty("设备秘钥")
    private  String secureKey;

    @ApiModelProperty("设备身份识别码")
    private  String serialNo;

    @ApiModelProperty("所属服务区 同一服务区编码中需带方向（例如AB），或根据道路方向确定")
    private  String serveiceAreaId;

    @ApiModelProperty("serveiceAreaName")
    private  String serveiceAreaName;

    @ApiModelProperty("固件版本（软件版本）号")
    private  String softwareVer;

    @ApiModelProperty("起始桩号")
    private  String startPile;

    @ApiModelProperty("所属收费站")
    private  String stationId;

    @ApiModelProperty("设备厂商编号")
    private  String supplierId;

    @ApiModelProperty("设备厂商名称")
    private  String supplierName;

    @ApiModelProperty("所属隧道编码")
    private  String tunnelId;

    @ApiModelProperty("tunnelName")
    private  String tunnelName;

    @ApiModelProperty("预期寿命/设计寿命,单位为年")
    private  Integer useLife;

    //预期寿命/设计寿命,单位为年
    private  String uLife;

    @ApiModelProperty("使用状态:1-在用 2-停用 3-备用")
    private  Integer useStatus;

    //使用状态:1-在用 2-停用 3-备用
    private  String uStatus;

    @ApiModelProperty("维保截止时间")
    private String warrantyEndTime;


    //维保截止时间
    private Date wEndTime;

    public String getCameraId() {
        return cameraId;
    }

    public void setCameraId(String cameraId) {
        this.cameraId = cameraId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public Date getDyTime() {
        return dyTime;
    }

    public void setDyTime(Date dyTime) {
        this.dyTime = dyTime;
    }

    public String getDevCategory() {
        return devCategory;
    }

    public void setDevCategory(String devCategory) {
        this.devCategory = devCategory;
    }

    public String getDevModel() {
        return devModel;
    }

    public void setDevModel(String devModel) {
        this.devModel = devModel;
    }

    public String getDevNo() {
        return devNo;
    }

    public void setDevNo(String devNo) {
        this.devNo = devNo;
    }

    public String getDevPwd() {
        return devPwd;
    }

    public void setDevPwd(String devPwd) {
        this.devPwd = devPwd;
    }

    public String getDevStatus() {
        return devStatus;
    }

    public void setDevStatus(String devStatus) {
        this.devStatus = devStatus;
    }

    public String getDevStatusTime() {
        return devStatusTime;
    }

    public void setDevStatusTime(String devStatusTime) {
        this.devStatusTime = devStatusTime;
    }

    public Date getdStatusTime() {
        return dStatusTime;
    }

    public void setdStatusTime(Date dStatusTime) {
        this.dStatusTime = dStatusTime;
    }

    public String getDevType() {
        return devType;
    }

    public void setDevType(String devType) {
        this.devType = devType;
    }

    public Integer getdType() {
        return dType;
    }

    public void setdType(Integer dType) {
        this.dType = dType;
    }

    public String getDevUsr() {
        return devUsr;
    }

    public void setDevUsr(String devUsr) {
        this.devUsr = devUsr;
    }

    public String getEdgeNetstatus() {
        return edgeNetstatus;
    }

    public void setEdgeNetstatus(String edgeNetstatus) {
        this.edgeNetstatus = edgeNetstatus;
    }

    public String getEdgeNetstatusTime() {
        return edgeNetstatusTime;
    }

    public void setEdgeNetstatusTime(String edgeNetstatusTime) {
        this.edgeNetstatusTime = edgeNetstatusTime;
    }

    public String getEle() {
        return ele;
    }

    public void setEle(String ele) {
        this.ele = ele;
    }

    public String getEndPile() {
        return endPile;
    }

    public void setEndPile(String endPile) {
        this.endPile = endPile;
    }

    public String getFacilitiesId() {
        return facilitiesId;
    }

    public void setFacilitiesId(String facilitiesId) {
        this.facilitiesId = facilitiesId;
    }

    public Integer getFacilitiesNum() {
        return facilitiesNum;
    }

    public void setFacilitiesNum(Integer facilitiesNum) {
        this.facilitiesNum = facilitiesNum;
    }

    public String getFacilitiesType() {
        return facilitiesType;
    }

    public void setFacilitiesType(String facilitiesType) {
        this.facilitiesType = facilitiesType;
    }

    public String getFdevNo() {
        return fdevNo;
    }

    public void setFdevNo(String fdevNo) {
        this.fdevNo = fdevNo;
    }

    public String getGatewayNetstatus() {
        return gatewayNetstatus;
    }

    public void setGatewayNetstatus(String gatewayNetstatus) {
        this.gatewayNetstatus = gatewayNetstatus;
    }

    public String getGatewayNetstatusTime() {
        return gatewayNetstatusTime;
    }

    public void setGatewayNetstatusTime(String gatewayNetstatusTime) {
        this.gatewayNetstatusTime = gatewayNetstatusTime;
    }

    public Date getgNetstatusTime() {
        return gNetstatusTime;
    }

    public void setgNetstatusTime(Date gNetstatusTime) {
        this.gNetstatusTime = gNetstatusTime;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getInstallTime() {
        return installTime;
    }

    public void setInstallTime(String installTime) {
        this.installTime = installTime;
    }

    public Date getInTime() {
        return inTime;
    }

    public void setInTime(Date inTime) {
        this.inTime = inTime;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getIsMonitor() {
        return isMonitor;
    }

    public void setIsMonitor(Integer isMonitor) {
        this.isMonitor = isMonitor;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getMonitorDirection() {
        return monitorDirection;
    }

    public void setMonitorDirection(Integer monitorDirection) {
        this.monitorDirection = monitorDirection;
    }

    public String getOpmaCliqueId() {
        return opmaCliqueId;
    }

    public void setOpmaCliqueId(String opmaCliqueId) {
        this.opmaCliqueId = opmaCliqueId;
    }

    public String getOpmaCliqueName() {
        return opmaCliqueName;
    }

    public void setOpmaCliqueName(String opmaCliqueName) {
        this.opmaCliqueName = opmaCliqueName;
    }

    public String getOpmaManagerCropId() {
        return opmaManagerCropId;
    }

    public void setOpmaManagerCropId(String opmaManagerCropId) {
        this.opmaManagerCropId = opmaManagerCropId;
    }

    public String getOpmaManagerCropName() {
        return opmaManagerCropName;
    }

    public void setOpmaManagerCropName(String opmaManagerCropName) {
        this.opmaManagerCropName = opmaManagerCropName;
    }

    public String getOpmaManagerId() {
        return opmaManagerId;
    }

    public void setOpmaManagerId(String opmaManagerId) {
        this.opmaManagerId = opmaManagerId;
    }

    public String getOpmaManagerName() {
        return opmaManagerName;
    }

    public void setOpmaManagerName(String opmaManagerName) {
        this.opmaManagerName = opmaManagerName;
    }

    public String getOpmaSectionId() {
        return opmaSectionId;
    }

    public void setOpmaSectionId(String opmaSectionId) {
        this.opmaSectionId = opmaSectionId;
    }

    public String getOpmaSectionName() {
        return opmaSectionName;
    }

    public void setOpmaSectionName(String opmaSectionName) {
        this.opmaSectionName = opmaSectionName;
    }

    public String getOpmaSubSectionId() {
        return opmaSubSectionId;
    }

    public void setOpmaSubSectionId(String opmaSubSectionId) {
        this.opmaSubSectionId = opmaSubSectionId;
    }

    public String getOpmaXxsubcenterId() {
        return opmaXxsubcenterId;
    }

    public void setOpmaXxsubcenterId(String opmaXxsubcenterId) {
        this.opmaXxsubcenterId = opmaXxsubcenterId;
    }

    public String getOpmaXxsubcenterName() {
        return opmaXxsubcenterName;
    }

    public void setOpmaXxsubcenterName(String opmaXxsubcenterName) {
        this.opmaXxsubcenterName = opmaXxsubcenterName;
    }

    public Double getOriginalValue() {
        return originalValue;
    }

    public void setOriginalValue(Double originalValue) {
        this.originalValue = originalValue;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getPile() {
        return pile;
    }

    public void setPile(String pile) {
        this.pile = pile;
    }

    public String getPileNum() {
        return pileNum;
    }

    public void setPileNum(String pileNum) {
        this.pileNum = pileNum;
    }

    public Integer getpNum() {
        return pNum;
    }

    public void setpNum(Integer pNum) {
        this.pNum = pNum;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getPortStatus() {
        return portStatus;
    }

    public void setPortStatus(String portStatus) {
        this.portStatus = portStatus;
    }

    public Date getpStatusTime() {
        return pStatusTime;
    }

    public void setpStatusTime(Date pStatusTime) {
        this.pStatusTime = pStatusTime;
    }

    public String getPortStatusTime() {
        return portStatusTime;
    }

    public void setPortStatusTime(String portStatusTime) {
        this.portStatusTime = portStatusTime;
    }

    public Integer getPresetPosition() {
        return presetPosition;
    }

    public void setPresetPosition(Integer presetPosition) {
        this.presetPosition = presetPosition;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getProtocolName() {
        return protocolName;
    }

    public void setProtocolName(String protocolName) {
        this.protocolName = protocolName;
    }

    public String getProtocolVersion() {
        return protocolVersion;
    }

    public void setProtocolVersion(String protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public Integer getRoadDirection() {
        return roadDirection;
    }

    public void setRoadDirection(Integer roadDirection) {
        this.roadDirection = roadDirection;
    }

    public String getrDirection() {
        return rDirection;
    }

    public void setrDirection(String rDirection) {
        this.rDirection = rDirection;
    }

    public String getRoadId() {
        return roadId;
    }

    public void setRoadId(String roadId) {
        this.roadId = roadId;
    }

    public String getRoadName() {
        return roadName;
    }

    public void setRoadName(String roadName) {
        this.roadName = roadName;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public String getSecureKey() {
        return secureKey;
    }

    public void setSecureKey(String secureKey) {
        this.secureKey = secureKey;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getServeiceAreaId() {
        return serveiceAreaId;
    }

    public void setServeiceAreaId(String serveiceAreaId) {
        this.serveiceAreaId = serveiceAreaId;
    }

    public String getServeiceAreaName() {
        return serveiceAreaName;
    }

    public void setServeiceAreaName(String serveiceAreaName) {
        this.serveiceAreaName = serveiceAreaName;
    }

    public String getSoftwareVer() {
        return softwareVer;
    }

    public void setSoftwareVer(String softwareVer) {
        this.softwareVer = softwareVer;
    }

    public String getStartPile() {
        return startPile;
    }

    public void setStartPile(String startPile) {
        this.startPile = startPile;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
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

    public Integer getUseLife() {
        return useLife;
    }

    public void setUseLife(Integer useLife) {
        this.useLife = useLife;
    }

    public String getuLife() {
        return uLife;
    }

    public void setuLife(String uLife) {
        this.uLife = uLife;
    }

    public Integer getUseStatus() {
        return useStatus;
    }

    public void setUseStatus(Integer useStatus) {
        this.useStatus = useStatus;
    }

    public String getuStatus() {
        return uStatus;
    }

    public void setuStatus(String uStatus) {
        this.uStatus = uStatus;
    }

    public String getWarrantyEndTime() {
        return warrantyEndTime;
    }

    public void setWarrantyEndTime(String warrantyEndTime) {
        this.warrantyEndTime = warrantyEndTime;
    }

    public Date getwEndTime() {
        return wEndTime;
    }

    public void setwEndTime(Date wEndTime) {
        this.wEndTime = wEndTime;
    }
}
