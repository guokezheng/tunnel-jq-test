const GlobalConfig = {
  eventTopicsShow: false,
  perceptionEventShow: false,
  roadTestEquipmentShow: false,
  roadNetworkFacilitiesShow: false,
  mapTreeShow: false,
  toolbarShow: false,
  eventTopics: [
    { label: '服务区异常', layerName: 'serviceAreaExceptionLayer', serviceAreaExceptionLayer: false, id: '1' },
    { label: '施工建设', layerName: 'constructionLayer', constructionLayer: false, id: '1' },
    { label: '设备设施隐患', layerName: 'sbssyhLayer', sbssyhLayer: false, id: '1' },
    { label: '路障清除', layerName: 'roadblockRemovalLayer', roadblockRemovalLayer: false, id: '1' },
    { label: '非法上路', layerName: 'breakIntoShLayer', breakIntoShLayer: false, id: '16' },
    { label: '交通拥堵', layerName: 'trafficJamLayer', trafficJamLayer: false, id: '2' },
    { label: '交通管制', layerName: 'trafficControlLayer', trafficControlLayer: false, id: '2' },
    { label: '车辆故障', layerName: 'vehicleFaultLayer', vehicleFaultLayer: false, id: '2' },
    { label: '交通事故', layerName: 'trafficAccidentLayer', trafficAccidentLayer: false, id: '12' },
    { label: '异常天气', layerName: 'abnormalWeatherLayer', abnormalWeatherLayer: false, id: '12' },
    { label: '其他', layerName: 'otherLayer', otherLayer: false, id: '99' },
    // { label: '道路', layerName: 'roadLayer', roadLayer: false, id: '3' },
    // { label: '道路积水', layerName: 'roadPondingLayer', roadPondingLayer: false, id: '5' },
    // { label: '道路团雾', layerName: 'roadMassFogLayer', roadMassFogLayer: false, id: '6' },
    // { label: '大风', layerName: 'galeLayer', galeLayer: false, id: '7' },
    // { label: '大雾', layerName: 'denseFogLayer', denseFogLayer: false, id: '8' },
    // { label: '道路湿滑', layerName: 'roadWetSlipperyLayer', roadWetSlipperyLayer: false, id: '9' },
    // { label: '道路施工', layerName: 'roadConstructionLayer', roadConstructionLayer: false, id: '10' },
    // { label: '超速', layerName: 'speedingLayer', speedingLayer: false, id: '14' },
    // { label: '排队超限', layerName: 'queueOverrunLayer', queueOverrunLayer: false, id: '17' },
  ],
  perceptionEvent: [
    { label: '交通拥堵', layerName: 'trafficJamGzLayer', trafficJamGzLayer: false, id: '2' },
    { label: '行人横穿', layerName: 'pedestrianCrossingGzLayer', pedestrianCrossingGzLayer: false, id: '18' },
    { label: '非机动车', layerName: 'nonMotorVehicletGzLayer', nonMotorVehicletGzLayer: false, id: '19' },
    { label: '停车', layerName: 'parkingGzLayer', parkingGzLayer: false, id: '4' },
    { label: '倒车', layerName: 'trafficRetrogradeGzLayer', trafficRetrogradeGzLayer: false, id: '1' },
    { label: '烟火', layerName: 'fireworksGzLayer', fireworksGzLayer: false, id: '20' },
    { label: '抛撒物', layerName: 'throwGzLayer', throwGzLayer: false, id: '20' },
    { label: '车辆慢行', layerName: 'slowDownGzLayer', slowDownGzLayer: false, id: '11' },
    { label: '超速', layerName: 'speedingGzLayer', speedingGzLayer: false, id: '14' },
    { label: '占用应急车道', layerName: 'emergencyLanesOccupancyGzLayer', emergencyLanesOccupancyGzLayer: false, id: '15' },
    { label: '异常变道', layerName: 'abnormalLaneChangeGzLayer', abnormalLaneChangeGzLayer: false, id: '13' },
    { label: '其他', layerName: 'otherGzLayer', otherGzLayer: false, id: '99' }
  ],
  roadTestEquipment: [
    { label: '摄像机', layerName: 'videoLayer', videoLayer: false, id: '003' },
    { label: '情报板', layerName: 'boardLayer', boardLayer: false, id: '005' },
    { label: 'MEC', layerName: 'mecLayer', mecLayer: false, id: '004', },
    { label: '广播', layerName: 'radioLayer', radioLayer: false, id: '015' },
    { label: '交调', layerName: 'CMLayer', CMLayer: false, id: '009' },
    { label: '能见度', layerName: 'visibilityLayer', visibilityLayer: false, id: '007' },
    { label: '路面状态', layerName: 'roadstateLayer', roadstateLayer: false, id: '105' },
    { label: '气象', layerName: 'meteorologicalLayer', meteorologicalLayer: false, id: '006' },
    { label: '卡口', layerName: 'mountLayer', mountLayer: false, id: '109' },
    { label: '智慧补光', layerName: 'lightSignLayer', lightSignLayer: false, id: '014' },
    { label: '微波车检', layerName: 'rippleLayer', rippleLayer: false, id: '011' },
    { label: '雷达', layerName: 'hmbRadarLayer', hmbRadarLayer: false, id: '001' },
    { label: '诱导灯', layerName: 'fogGuideLayer', fogGuideLayer: false, id: '012' },
    { label: '动静结合', layerName: 'moveStaticLayer', moveStaticLayer: false, id: '007' },
    { label: '智慧锥桶', layerName: 'coneBarrelLayer', coneBarrelLayer: false, id: '043' },
    { label: '雷视一体', layerName: 'radarAndvideoLayer', radarAndvideoLayer: false, id: '001' },
    { label: '智能机箱', layerName: 'smartBoxLayer', smartBoxLayer: false, id: '001' },
    { label: '车位指示', layerName: 'parkingSignLayer', parkingSignLayer: false, id: '001' },
    { label: '基站', layerName: 'baseStationLayer', baseStationLayer: false, id: '001' },
    { label: 'RSU', layerName: 'rsuLayer', rsuLayer: false, id: '001' },
    { label: '护栏碰撞', layerName: 'barrierCollisionWarningLayer', barrierCollisionWarningLayer: false, id: '001' },
    { label: '桥梁健康', layerName: 'bridgeHealthLayer', bridgeHealthLayer: false, id: '001' },
    { label: '疲劳唤醒', layerName: 'awakenLayer', awakenLayer: false, id: '001' },
    { label: '合流预警', layerName: 'confluenceLayer', confluenceLayer: false, id: '001' },
    { label: '融冰除雪', layerName: 'removeSnowLayer', removeSnowLayer: false, id: '001' },
    { label: '智慧道钉', layerName: 'spikeLayer', spikeLayer: false, id: '001' },
    { label: '距离标志', layerName: 'distanceMarkerLayer', distanceMarkerLayer: false, id: '001' },
    { label: '路段广播', layerName: 'sectionRadioLayer', sectionRadioLayer: false, id: '001' },
    { label: '北斗高精度', layerName: 'beidouLayer', beidouLayer: false, id: '001' },
  ],
  roadNetworkFacilities: [
    { label: '服务区', layerName: 'serviceLayer', serviceLayer: false, id: '003' },
    { label: '收费站', layerName: 'stationLayer', stationLayer: false, id: '004', },
    { label: '枢纽立交', layerName: 'pivotLayer', pivotLayer: false, id: '005' },
    { label: '互通立交', layerName: 'interchangeLayer', interchangeLayer: false, id: '011' },
    { label: '路管机构', layerName: 'organizationLayer', organizationLayer: false, id: '109' },
    { label: '桥梁', layerName: 'bridgeLayer', bridgeLayer: false, id: '007' },
    { label: '隧道', layerName: 'tunnelLayer', tunnelLayer: false, id: '105' },
    { label: '道路线', layerName: 'roadlineLayer', roadlineLayer: false, id: '015' },
    { label: '行政区划', layerName: 'districtLayer', districtLayer: false, id: '100' },
    { label: '桩号', layerName: 'pileLayer', pileLayer: false, id: '006' },
    { label: '共享路段', layerName: 'shareRoadLayer', shareRoadLayer: false, id: '014' },
  ],
  /*图层自选展示   true为展示  false为隐藏*/
  mecLayer: false, //mec图层
  radioLayer: false, //广播图层
  CMLayer: false, //交调图层
  visibilityLayer: false, //能见度图层
  roadstateLayer: false, //路面状态图层
  meteorologicalLayer: false, //气象图层
  mountLayer: false, //卡口相机图层
  lightSignLayer: false, // 主动发光标志图层
  rippleLayer: false, // 微波车检器图层
  hmbRadarLayer: false, // 雷达图层
  videoLayer: false, // 摄像机图层
  boardLayer: false, // 情报板图层
  serviceLayer: false, //服务区图层
  pivotLayer: false, //枢纽立交图层
  stationLayer: false,//收费站图层
  roadlineLayer: false,// 道路线图层
  bridgeLayer: false,//桥梁图层
  tunnelLayer: false,//隧道图层
  pileLayer: false,//桩号图层
  organizationLayer: false,//路管机构图层
  fogGuideLayer: false,//雾区诱导图层
  boardAllLayer: false,
  shareRoadLayer: false,//共享路段图层
  moveStaticLayer: false,//动静结合一体机图层
  interchangeLayer: false,// 互通立交图层
  coneBarrelLayer: false,//智慧锥桶
  radarAndvideoLayer: false,//雷视一体机
  smartBoxLayer: false,//智能机箱
  parkingSignLayer: false,//车位指示屏
  baseStationLayer: false,//基站
  barrierCollisionWarningLayer: false, // 护栏碰撞预警
  bridgeHealthLayer: false, // 桥梁健康监测
  rsuLayer: false, // RSU
  awakenLayer: false,//唤醒疲劳
  confluenceLayer: false,//合流预警
  removeSnowLayer: false,//融冰除雪
  spikeLayer: false,//智慧道钉
  distanceMarkerLayer: false,//距离标志
  sectionRadioLayer: false,//路段广播
  beidouLayer: false,//北斗高精度

  jiqingMidLayer: false, // 济青连接线高亮

  //交通事件
  serviceAreaExceptionLayer: false,//服务区异常
  constructionLayer: false,//施工建设
  sbssyhLayer: false,//设施设备隐患
  roadblockRemovalLayer: false,//路障清除
  breakIntoShLayer: false,//非法上路
  trafficJamLayer: false,//交通拥堵
  trafficControlLayer: false,//交通管制
  vehicleFaultLayer: false,//车辆故障
  trafficAccidentLayer: false,//交通事故
  abnormalWeatherLayer: false,//异常天气
  roadLayer: false,
  roadPondingLayer: false,
  roadMassFogLayer: false,
  galeLayer: false,
  denseFogLayer: false,
  roadWetSlipperyLayer: false,
  roadConstructionLayer: false,
  speedingLayer: false,
  queueOverrunLayer: false,
  otherLayer: false,//其他事件
  //感知事件
  trafficJamGzLayer: false,//交通庸俗
  pedestrianCrossingGzLayer: false,//行人
  nonMotorVehicletGzLayer: false,//非机动车
  parkingGzLayer: false,//停车
  trafficRetrogradeLayer: false,//倒车
  fireworksGzLayer: false,//烟火
  throwGzLayer: false,//抛撒物
  slowDownGzLayer: false,//车辆慢行
  speedingGzLayer: false,//超速
  emergencyLanesOccupancyGzLayer: false,//占用应急车道
  abnormalLaneChangeGzLayer: false,//异常变道
  otherGzLayer: false,//其他


  /*深色图层服务地址*/
  BaseMapUrl: "http://10.166.154.217:6080/arcgis/rest/services/BaseService/darkColor/MapServer",
  /*浅色图层服务地址*/
  BaseMapUrl_L: "http://10.166.154.217:6080/arcgis/rest/services/BaseService/lightColor/MapServer",
  /*济青道路中心线*/
  jiqingRoadUrl: 'http://10.166.154.217:6080/arcgis/rest/services/Facility/jq_road/MapServer',
  /*济青高亮线*/
  jiqingHLUrl: 'http://10.166.154.217:6080/arcgis/rest/services/Facility/jq_all/MapServer',
  /*京台高亮线*/
  jingtaiHLUrl: 'http://10.166.154.217:6080/arcgis/rest/services/Facility/jt_road/MapServer',
  /*济青连接线*/
  jiqingMidUrl: 'http://10.166.154.217:6080/arcgis/rest/services/Facility/jq_middle/MapServer',
  /* 路侧设备*/
  hmbRadarLayerUrl: "http://10.166.154.217:6080/arcgis/rest/services/Device/LD/MapServer", // 雷达
  radioLayerUrl: "http://10.166.154.217:6080/arcgis/rest/services/Device/GB/MapServer", // 广播
  barrierCollisionWarningLayerUrl: "http://10.166.154.217:6080/arcgis/rest/services/Device/HLPZYJXT/MapServer", // 护栏碰撞预警
  bridgeHealthLayerUrl: "http://10.166.154.217:6080/arcgis/rest/services/Device/QLJKJC/MapServer", // 桥梁健康监测
  rsuLayerUrl: "http://10.166.154.217:6080/arcgis/rest/services/Device/RSU/MapServer", // RSU
  MECLaryerUrl: "http://10.166.154.217:6080/arcgis/rest/services/Device/MEC/MapServer", // MEC
  CMLayerUrl: "http://10.166.154.217:6080/arcgis/rest/services/Device/JDSB/MapServer", // 交调
  visibilityLayerUrl: "http://10.166.154.217:6080/arcgis/rest/services/Device/NJD/MapServer", // 能见度
  meteorologicalLayerUrl: "http://10.166.154.217:6080/arcgis/rest/services/Device/QXJCQ/MapServer", // 气象
  mountLayerUrl: "http://10.166.154.217:6080/arcgis/rest/services/Device/KKZPXT/MapServer", // 卡口
  lightSignLayerUrl: " http://10.166.154.217:6080/arcgis/rest/services/Device/ZDFGBZ/MapServer", // 发光标志
  rippleLayerUrl: "http://10.166.154.217:6080/arcgis/rest/services/Device/WBCJ/MapServer", // 微波检测
  videoLayerUrl: "http://10.166.154.217:6080/arcgis/rest/services/Device/camera/MapServer", // 摄像机
  boardLayerUrl: "http://10.166.154.217:6080/arcgis/rest/services/Device/KBXXBZ/MapServer", // 情报板
  fogGuideLayerUrl: 'http://10.166.154.217:6080/arcgis/rest/services/Device/JTYDD/MapServer', // 雾区诱导
  radarAndvideoLayerUrl: 'http://10.166.154.217:6080/arcgis/rest/services/Device/LSYTJ/MapServer', // 雷视一体
  smartBoxLayerUrl: 'http://10.166.154.217:6080/arcgis/rest/services/Device/ZNPDG/MapServer', // 智能机箱
  parkingSignLayerUrl: 'http://10.166.154.217:6080/arcgis/rest/services/Device/FWQQCWZSP/MapServer', // 车位指示屏
  baseStationLayerUrl: 'http://10.166.154.217:6080/arcgis/rest/services/Device/LCTXJZ/MapServer', // 基站
  awakenLayerUrl: 'http://10.166.154.217:6080/arcgis/rest/services/Device/HXPLSB/MapServer', // 疲劳唤醒
  confluenceLayerUrl: 'http://10.166.154.217:6080/arcgis/rest/services/Device/HLQYJSB/MapServer', // 合流预警
  removeSnowLayerUrl: 'http://10.166.154.217:6080/arcgis/rest/services/Device/RBCX/MapServer', // 融冰除雪
  spikeLayerUrl: 'http://10.166.154.217:6080/arcgis/rest/services/Device/ZHDD/MapServer', // 智慧道钉
  // distanceMarkerLayerUrl: 'http://10.166.154.217:6080/arcgis/rest/services/Device/DDJLBZ/MapServer', // 距离标志
  sectionRadioLayerUrl: 'http://10.166.154.217:6080/arcgis/rest/services/Device/LDGBYJ/MapServer', // 路段广播
  beidouLayerUrl: 'http://10.166.154.217:6080/arcgis/rest/services/Device/BDGJDSKFWXT/MapServer', // 北斗高精度

  // roadstateLayerUrl: "http://10.168.28.37:6080/arcgis/rest/services/yh/roadM/MapServer", // 路面状态
  moveStaticLayerUrl: 'http://10.166.154.217:6080/arcgis/rest/services/Device/DDJLBZ/MapServer', // 一体化动静结合
  // coneBarrelLayerUrl: 'http://10.168.28.37:6080/arcgis/rest/services/yh/MECJQ/MapServer', // 智慧锥桶


  /* 路网设施 */
  serviceLayerUrl: 'http://10.166.154.217:6080/arcgis/rest/services/Facility/FWQ_T/MapServer', // 服务区
  stationLayerUrl: 'http://10.166.154.217:6080/arcgis/rest/services/Facility/SFZ/MapServer', // 收费站
  organizationLayerUrl: 'http://10.166.154.217:6080/arcgis/rest/services/Facility/LGJG/MapServer', // 机构
  interchangeLayerUrl: 'http://10.166.154.217:6080/arcgis/rest/services/Facility/HTLJ/MapServer', // 互通立交
  pivotLayerUrl: 'http://10.166.154.217:6080/arcgis/rest/services/Facility/SNLJ/MapServer',  // 枢纽立交
  bridgeLayerUrl: 'http://10.166.154.217:6080/arcgis/rest/services/Facility/QL/MapServer', // 桥梁
  tunnelLayerUrl: 'http://10.166.154.217:6080/arcgis/rest/services/Facility/SD/MapServer',// 隧道
  pileLayerUrl: 'http://10.166.154.217:6080/arcgis/rest/services/Facility/ZH/MapServer', // 桩号
  roadlineLayerUrl: 'http://10.166.154.217:6080/arcgis/rest/services/Facility/road/MapServer',// 道路线
  shareRoadLayerUrl: 'http://10.166.154.217:6080/arcgis/rest/services/Facility/shareRoad/MapServer', // 共享路段
  administrativeAreaLayerUrl: "http://10.166.154.217:6080/arcgis/rest/services/Facility/District/MapServer" // 行政区划

}
export default {
  GlobalConfig
}