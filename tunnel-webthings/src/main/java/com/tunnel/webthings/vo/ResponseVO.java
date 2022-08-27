package com.tunnel.webthings.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


/**
 * @author dzy
 * @date 2022/8/23 8:51
 */
@Data
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

}
