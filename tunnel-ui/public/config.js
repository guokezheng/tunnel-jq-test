const GlobalConfig = {
  eventTopicsShow: false,
  roadTestEquipmentShow: false,
  roadNetworkFacilitiesShow: false,
  mapTreeShow: false,
  toolbarShow: false,
  eventTopics: [
    { label: '交通事故', layerName: 'trafficEventLayer', trafficEventLayer: false, id: '' },
    { label: '感知事件', layerName: 'perceptionEventLayer', perceptionEventLayer: false, id: '' }
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
  ],
  roadNetworkFacilities: [
    { label: '服务区', layerName: 'serviceLayer', serviceLayer: false, id: '003' },
    { label: '枢纽立交', layerName: 'pivotLayer', pivotLayer: false, id: '005' },
    { label: '收费站', layerName: 'stationLayer', stationLayer: false, id: '004', },
    { label: '道路线', layerName: 'roadlineLayer', roadlineLayer: false, id: '015' },
    { label: '行政区划', layerName: 'districtLayer', districtLayer: false, id: '100' },
    { label: '桥梁', layerName: 'organizationLayer', organizationLayer: false, id: '007' },
    { label: '隧道', layerName: 'tunnelLayer', tunnelLayer: false, id: '105' },
    { label: '桩号', layerName: 'pileLayer', pileLayer: false, id: '006' },
    { label: '路管机构', layerName: 'organizationLayer', organizationLayer: false, id: '109' },
    { label: '共享路段', layerName: 'shareRoadLayer', shareRoadLayer: false, id: '014' },
    { label: '互通立交', layerName: 'interchangeLayer', interchangeLayer: false, id: '011' },
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
  trafficEventLayer: false,//交通事故

  /*深色图层服务地址*/
  // BaseMapUrl: "http://10.168.10.200.1.8928.37:6080/arcgis/rest/services/testBack01/MapServer",
  BaseMapUrl: "http://10.200.1.89:6080/arcgis/rest/services/BaseService/darkColor/MapServer",
  /*浅色图层服务地址*/
  BaseMapUrl_L: "http://10.200.1.89:6080/arcgis/rest/services/lightColor/MapServer",

  /* 路侧设备*/
  // MECLaryerUrl: "http://10.168.28.37:6080/arcgis/rest/services/yh/MECJQ/MapServer", // 济青
  MECLaryerUrl: "http://124.128.152.251:6080/arcgis/rest/services/JTRoad/MEC/MapServer",
  radioLayerUrl: "http://10.168.28.37:6080/arcgis/rest/services/yh/GUANGBOJQ/MapServer",
  // CMLayerUrl: "http://10.168.28.37:6080/arcgis/rest/services/yh/JDJQ/MapServer", //济青
  CMLayerUrl: "http://10.200.1.89:6080/arcgis/rest/services/BaseService/JD/MapServer",
  visibilityLayerUrl: "http://10.168.28.37:6080/arcgis/rest/services/yh/NENGJIANDUJQ/MapServer",
  roadstateLayerUrl: "http://10.168.28.37:6080/arcgis/rest/services/yh/roadM/MapServer",
  // meteorologicalLayerUrl: "http://10.168.28.37:6080/arcgis/rest/services/yh/QXJQ/MapServer", // 济青
  meteorologicalLayerUrl: "http://10.200.1.89:6080/arcgis/rest/services/BaseService/QX/MapServer",
  mountLayerUrl: "http://10.168.28.37:6080/arcgis/rest/services/yh/kakou/MapServer",
  lightSignLayerUrl: "http://10.168.28.37:6080/arcgis/rest/services/yh/guangbiao/MapServer",
  // rippleLayerUrl: "http://10.168.28.37:6080/arcgis/rest/services/yh/weibo/MapServer",//济青
  rippleLayerUrl: "http://10.200.1.89:6080/arcgis/rest/services/BaseService/WBCJ/MapServer",
  hmbRadarLayerUrl: "http://10.168.28.37:6080/arcgis/rest/services/yh/haomibo/MapServer",
  // videoLayerUrl: "http://10.168.28.37:6080/arcgis/rest/services/yh/raodcm/MapServer",// 济青
  videoLayerUrl: "http://10.168.28.37:6080/arcgis/rest/services/BaseService/camera/MapServer",
  boardLayerUrl: "http://10.168.28.37:6080/arcgis/rest/services/BaseService/QBB/MapServer",
  fogGuideLayerUrl: 'http://10.168.28.37:6080/arcgis/rest/services/yh/MECJQ/MapServer', // 后期替换
  moveStaticLayerUrl: 'http://10.168.28.37:6080/arcgis/rest/services/yh/MECJQ/MapServer', // 后期替换
  coneBarrelLayerUrl: 'http://10.168.28.37:6080/arcgis/rest/services/yh/MECJQ/MapServer', // 后期替换
  radarAndvideoLayerUrl: 'http://10.168.28.37:6080/arcgis/rest/services/yh/MECJQ/MapServer', // 后期替换
  smartBoxLayerUrl: 'http://10.168.28.37:6080/arcgis/rest/services/yh/MECJQ/MapServer', // 后期替换
  parkingSignLayerUrl: 'http://10.168.28.37:6080/arcgis/rest/services/yh/MECJQ/MapServer', // 后期替换
  baseStationLayerUrl: 'http://10.168.28.37:6080/arcgis/rest/services/yh/MECJQ/MapServer', // 后期替换

  /* 路网设施 */
  serviceLayerUrl: 'http://10.168.28.37:6080/arcgis/rest/services/BaseService/FWQ/MapServer',
  pivotLayerUrl: 'http://10.168.28.37:6080/arcgis/rest/services/DN/SN/MapServer',
  stationLayerUrl: 'http://10.168.28.37:6080/arcgis/rest/services/DN/SFZ/MapServer',
  roadlineLayerUrl: ' http://10.168.28.37:6080/arcgis/rest/services/DN/raod/MapServer',
  bridgeLayerUrl: 'http://10.168.28.37:6080/arcgis/rest/services/DN/ql/MapServer',
  tunnelLayerUrl: 'http://10.168.28.37:6080/arcgis/rest/services/DN/sd/MapServer',
  pileLayerUrl: 'http://10.168.28.37:6080/arcgis/rest/services/ZH/MapServer',
  organizationLayerUrl: 'http://10.168.28.37:6080/arcgis/rest/services/DN/luguan/MapServer',
  shareRoadLayerUrl: 'http://10.168.28.37:6080/arcgis/rest/services/DN/send02/MapServer',
  interchangeLayerUrl: 'http://10.200.1.89:6080/arcgis/rest/services/BaseService/HTLJ/MapServer',
  /* 事件专题 */
  trafficEventLayerUrl: 'http://10.168.28.37:6080/arcgis/rest/services/yh/kakou/MapServer',
}
export default {
  GlobalConfig
}