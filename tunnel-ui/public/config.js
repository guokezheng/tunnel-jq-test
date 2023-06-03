const GlobalConfig = {
  jiqing: { //济青配置
    eventTopicsShow: false, //交通事件 是否显示
    perceptionEventShow: false, //感知事件 是否显示
    roadTestEquipmentShow: false, //路测设备 是否显示
    equipmentFacilitiesShow: false,//设备设施
    otherClassShow: false,//其他
    roadNetworkFacilitiesShow: false, //路网设施 是否显示
    maintenanceShow: false, //建设养护 是否显示
    mapTreeShow: false,  //设备树 是否显示
    toolbarShow: false,  //工具栏 是否显示
    legendShow: false, //图例 是否显示
    roadIDs: ['S000637'],//'S000637' 道路id配置   S000637--济青
    roadAndBridgeTunnelShow: false,
    /*
      roadIdKey 值为对应图层属性中的道路id字段 用来根据道路id查询
      opmaManageIdKey 用来配置运管机构树 进行图层筛选 只根据两个值进行筛选 opmaManagerCorpIds、opmaManagerIds两个值是固定的 只需要改键 opmaManagerCorpIds对应的是树中一级发展公司 opmaManagerIds对应的是二级运管中心 
      {'opma_manag':'opmaManagerCorpIds','opma_man_2':'opmaManagerIds'}
      is_test 用来判断加不加测试基地的数据
      organizationSql 布尔值 用于判断是否要根据登录获取的机构id进行过滤 不设置默认过滤 设置true不过滤
    */
    //路桥隧道
    roadAndBridgeTunnel: [
      { label: '道路线', layerName: 'roadlineLayer', roadlineLayer: true, id: '015', roadIdKey: 'ROADID', opmaManageIdKey: { 'opma_manag': 'opmaManagerCorpIds', 'opma_man_2': 'opmaManagerIds' } },
      { label: '桩号', layerName: 'pileLayer', pileLayer: true, id: '006' },
      { label: '桥梁', layerName: 'bridgeLayerJQ', bridgeLayerJQ: true, id: '007', roadIdKey: 'road_id', opmaManageIdKey: { 'opma_manag': 'opmaManagerCorpIds', 'opma_man_2': 'opmaManagerIds' } },
      { label: '隧道', layerName: 'tunnelLayer', tunnelLayer: false, id: '105', roadIdKey: 'road_id', opmaManageIdKey: { 'opma_manag': 'opmaManagerCorpIds', 'opma_man_2': 'opmaManagerIds' } },
      { label: '隧道', layerName: 'lineTunnelLayer', lineTunnelLayer: true, id: '105', organizationSql: true }, //线隧道
    ],
    //设施、设备
    equipmentFacilities: [
      { label: '摄像机', layerName: 'videoLayer', videoLayer: false, id: '003', roadIdKey: 'road_id', opmaManageIdKey: { 'opma_manag': 'opmaManagerCorpIds', 'opma_man_2': 'opmaManagerIds' } },
      { label: '摄像机', layerName: 'jqvideoLayer', jqvideoLayer: true },
      { label: '情报板', layerName: 'boardLayer', boardLayer: true, id: '005', roadIdKey: 'road_id', opmaManageIdKey: { 'opma_manag': 'opmaManagerCorpIds', 'opma_man_2': 'opmaManagerIds' } },
      { label: '收费站', layerName: 'stationLayerJQ', stationLayerJQ: true, id: '004', roadIdKey: 'road_id', opmaManageIdKey: { 'opma_manag': 'opmaManagerCorpIds', 'opma_man_2': 'opmaManagerIds' } },
      { label: '服务区', layerName: 'serviceLayerJQ', serviceLayerJQ: true, id: '003', roadIdKey: 'road_id', opmaManageIdKey: { 'opma_manag': 'opmaManagerCorpIds', 'opma_man_2': 'opmaManagerIds' } },
      { label: '枢纽立交', layerName: 'pivotLayerJQ', pivotLayerJQ: true, id: '005', roadIdKey: 'road_id', opmaManageIdKey: { 'opma_manag': 'opmaManagerCorpIds', 'opma_man_2': 'opmaManagerIds' } },
      { label: '互通立交', layerName: 'interchangeLayerJQ', interchangeLayerJQ: true, id: '011', roadIdKey: 'road_id', opmaManageIdKey: { 'opma_manag': 'opmaManagerCorpIds', 'opma_man_2': 'opmaManagerIds' } },
      { label: '路管机构', layerName: 'organizationLayer', organizationLayer: false, id: '109', roadIdKey: 'road_id', opmaManageIdKey: { 'opma_manag': 'opmaManagerCorpIds', 'opma_man_2': 'opmaManagerIds' } },
      { label: 'ETC门架', layerName: 'ETCLayer', ETCLayer: true, id: '106', roadIdKey: 'road_id', opmaManageIdKey: { 'opma_manag': 'opmaManagerCorpIds', 'opma_man_2': 'opmaManagerIds' } },
    ],
    otherClass: [
      { label: '物资装备', layerName: 'emergencyLayer', emergencyLayer: false, id: '018', roadIdKey: 'road_id', opmaManageIdKey: { 'opma_manag': 'opmaManagerCorpIds', 'opma_man_2': 'opmaManagerIds' }, },
      { label: '交安标识', layerName: 'trafficSafetyLayer', trafficSafetyLayer: false, id: '021', roadIdKey: 'road_id', opmaManageIdKey: { 'opma_manag': 'opmaManagerCorpIds', 'opma_man_2': 'opmaManagerIds' }, organizationSql: true },
    ],
    eventTopics: [//交通事件配置
      { label: '服务区异常', layerName: 'serviceAreaExceptionLayer', serviceAreaExceptionLayer: true, id: '1' },
      { label: '施工建设', layerName: 'constructionLayer', constructionLayer: true, id: '1' },
      { label: '设备设施隐患', layerName: 'sbssyhLayer', sbssyhLayer: true, id: '1' },
      { label: '路障清除', layerName: 'roadblockRemovalLayer', roadblockRemovalLayer: true, id: '1' },
      { label: '非法上路', layerName: 'breakIntoShLayer', breakIntoShLayer: true, id: '16' },
      { label: '交通拥堵', layerName: 'trafficJamLayer', trafficJamLayer: true, id: '2' },
      { label: '交通管制', layerName: 'trafficControlLayer', trafficControlLayer: true, id: '2' },
      { label: '车辆故障', layerName: 'vehicleFaultLayer', vehicleFaultLayer: true, id: '2' },
      { label: '交通事故', layerName: 'trafficAccidentLayer', trafficAccidentLayer: true, id: '12' },
      { label: '异常天气', layerName: 'abnormalWeatherLayer', abnormalWeatherLayer: true, id: '12' },
      { label: '其他事件', layerName: 'otherLayer', otherLayer: true, id: '99' },
      // { label: '道路', layerName: 'roadLayer', roadLayer: true, id: '3' },
      // { label: '道路积水', layerName: 'roadPondingLayer', roadPondingLayer: true, id: '5' },
      // { label: '道路团雾', layerName: 'roadMassFogLayer', roadMassFogLayer: true, id: '6' },
      // { label: '大风', layerName: 'galeLayer', galeLayer: true, id: '7' },
      // { label: '大雾', layerName: 'denseFogLayer', denseFogLayer: true, id: '8' },
      // { label: '道路湿滑', layerName: 'roadWetSlipperyLayer', roadWetSlipperyLayer: true, id: '9' },
      // { label: '道路施工', layerName: 'roadConstructionLayer', roadConstructionLayer: true, id: '10' },
      // { label: '超速', layerName: 'speedingLayer', speedingLayer: true, id: '14' },
      // { label: '排队超限', layerName: 'queueOverrunLayer', queueOverrunLayer: true, id: '17' },
    ],
    perceptionEvent: [ //感知事件配置
      { label: '交通拥堵', layerName: 'trafficJamGzLayer', trafficJamGzLayer: true, id: '2' },
      { label: '行人横穿', layerName: 'pedestrianCrossingGzLayer', pedestrianCrossingGzLayer: true, id: '18' },
      { label: '非机动车', layerName: 'nonMotorVehicletGzLayer', nonMotorVehicletGzLayer: true, id: '19' },
      { label: '停车', layerName: 'parkingGzLayer', parkingGzLayer: true, id: '4' },
      { label: '倒车', layerName: 'trafficRetrogradeGzLayer', trafficRetrogradeGzLayer: true, id: '1' },
      { label: '烟火', layerName: 'fireworksGzLayer', fireworksGzLayer: true, id: '20' },
      { label: '撒落物', layerName: 'throwGzLayer', throwGzLayer: true, id: '20' },
      { label: '车辆慢行', layerName: 'slowDownGzLayer', slowDownGzLayer: true, id: '11' },
      { label: '超速', layerName: 'speedingGzLayer', speedingGzLayer: true, id: '14' },
      { label: '占用应急车道', layerName: 'emergencyLanesOccupancyGzLayer', emergencyLanesOccupancyGzLayer: true, id: '15' },
      { label: '异常变道', layerName: 'abnormalLaneChangeGzLayer', abnormalLaneChangeGzLayer: true, id: '13' },
      { label: '其他事件', layerName: 'otherGzLayer', otherGzLayer: true, id: '99' }
    ],
    /* roadTestEquipment: [//路侧设备配置
      { label: '摄像机', layerName: 'videoLayer', videoLayer: false, id: '003', roadIdKey: 'road_id', opmaManageIdKey: { 'opma_manag': 'opmaManagerCorpIds', 'opma_man_2': 'opmaManagerIds' } },
      { label: '摄像机', layerName: 'jqvideoLayer', jqvideoLayer: true },
      { label: '情报板', layerName: 'boardLayer', boardLayer: true, id: '005', roadIdKey: 'road_id', opmaManageIdKey: { 'opma_manag': 'opmaManagerCorpIds', 'opma_man_2': 'opmaManagerIds' } },
      { label: 'MEC', layerName: 'mecLayer', mecLayer: true, id: '004', roadIdKey: 'road_id' },
      { label: '广播', layerName: 'radioLayer', radioLayer: true, id: '015', roadIdKey: 'road_id' },
      { label: '交调', layerName: 'CMLayer', CMLayer: true, id: '009', roadIdKey: 'road_id' },
      { label: '能见度检测', layerName: 'visibilityLayer', visibilityLayer: true, id: '007', roadIdKey: 'road_id' },
      { label: '路面状态', layerName: 'roadstateLayer', roadstateLayer: false, id: '105', roadIdKey: 'road_id' },
      { label: '气象检测器', layerName: 'meteorologicalLayer', meteorologicalLayer: true, id: '006', roadIdKey: 'road_id', opmaManageIdKey: { 'opma_manag': 'opmaManagerCorpIds', 'opma_man_2': 'opmaManagerIds' } },
      // { label: '卡口', layerName: 'mountLayer', mountLayer: false, id: '109', roadIdKey: 'road_id' },
      { label: '智慧补光', layerName: 'lightSignLayer', lightSignLayer: true, id: '014', roadIdKey: 'road_id' },
      { label: '微波车检', layerName: 'rippleLayer', rippleLayer: true, id: '011', roadIdKey: 'road_id' },
      { label: '雷达', layerName: 'hmbRadarLayer', hmbRadarLayer: true, id: '001', roadIdKey: 'road_id' },
      { label: '雾区诱导', layerName: 'fogGuideLayer', fogGuideLayer: true, id: '012', organizationSql: true },
      { label: '动静一体化标志', layerName: 'moveStaticLayer', moveStaticLayer: true, id: '007', roadIdKey: 'road_id' },
      { label: '智慧锥桶', layerName: 'coneBarrelLayer', coneBarrelLayer: false, id: '043', roadIdKey: 'road_id' },
      { label: '雷视一体', layerName: 'radarAndvideoLayer', radarAndvideoLayer: true, id: '001', roadIdKey: 'road_id' },
      { label: '智能机箱', layerName: 'smartBoxLayer', smartBoxLayer: true, id: '001', roadIdKey: 'road_id' },
      // { label: '车位指示', layerName: 'parkingSignLayer', parkingSignLayer: false, id: '001', roadIdKey: 'road_id' },
      { label: '北斗基站', layerName: 'baseStationLayer', baseStationLayer: true, id: '001', roadIdKey: 'road_id' },
      { label: 'RSU', layerName: 'rsuLayer', rsuLayer: true, id: '001', roadIdKey: 'road_id' },
      { label: '护栏碰撞', layerName: 'barrierCollisionWarningLayer', barrierCollisionWarningLayer: true, id: '001', organizationSql: true },
      { label: '桥梁健康', layerName: 'bridgeHealthLayer', bridgeHealthLayer: true, id: '001', roadIdKey: 'road_id' },
      // { label: '疲劳唤醒', layerName: 'awakenLayer', awakenLayer: false, id: '001', roadIdKey: 'road_id' },
      { label: '合流预警', layerName: 'confluenceLayer', confluenceLayer: true, id: '001', roadIdKey: 'road_id' },
      { label: '融冰除雪', layerName: 'removeSnowLayer', removeSnowLayer: true, id: '001', organizationSql: true },
      // { label: '智慧道钉', layerName: 'spikeLayer', spikeLayer: false, id: '001', roadIdKey: 'road_id' },
      { label: '距离标志', layerName: 'distanceMarkerLayer', distanceMarkerLayer: false, id: '001', roadIdKey: 'road_id' },
      // { label: '路段广播', layerName: 'sectionRadioLayer', sectionRadioLayer: false, id: '001', roadIdKey: 'road_id' },
      // { label: '北斗高精度', layerName: 'beidouLayer', beidouLayer: false, id: '001', roadIdKey: 'road_id' },
      { label: '光栅阵列', layerName: 'rasterArrayLayer', rasterArrayLayer: true, id: '001', organizationSql: true },
    ], */
    roadTestEquipment: [//路侧设备配置-感知
      { label: '摄像机', layerName: 'videoLayer', videoLayer: false, id: '003', roadIdKey: 'road_id', opmaManageIdKey: { 'opma_manag': 'opmaManagerCorpIds', 'opma_man_2': 'opmaManagerIds' } },
      { label: '摄像机', layerName: 'jqvideoLayer', jqvideoLayer: true },
      { label: '交调', layerName: 'CMLayer', CMLayer: true, id: '009', roadIdKey: 'road_id' },
      { label: '能见度检测', layerName: 'visibilityLayer', visibilityLayer: true, id: '007', roadIdKey: 'road_id' },
      { label: '路面状态', layerName: 'roadstateLayer', roadstateLayer: false, id: '105', roadIdKey: 'road_id' },
      { label: '气象检测器', layerName: 'meteorologicalLayer', meteorologicalLayer: true, id: '006', roadIdKey: 'road_id', opmaManageIdKey: { 'opma_manag': 'opmaManagerCorpIds', 'opma_man_2': 'opmaManagerIds' } },
      { label: '微波车检', layerName: 'rippleLayer', rippleLayer: true, id: '011', roadIdKey: 'road_id' },
      { label: '雷达', layerName: 'hmbRadarLayer', hmbRadarLayer: true, id: '001', roadIdKey: 'road_id' },
      /* { label: '雷视一体', layerName: 'radarAndvideoLayer', radarAndvideoLayer: true, id: '001', roadIdKey: 'road_id' }, */

      { label: '光栅阵列', layerName: 'rasterArrayLayer', rasterArrayLayer: true, id: '001', organizationSql: true },
    ],
    roadTestPublishEquipment: [//路侧设备配置-发布
      { label: '情报板', layerName: 'boardLayer', boardLayer: true, id: '005', roadIdKey: 'road_id', opmaManageIdKey: { 'opma_manag': 'opmaManagerCorpIds', 'opma_man_2': 'opmaManagerIds' } },
      { label: '广播', layerName: 'radioLayer', radioLayer: true, id: '015', roadIdKey: 'road_id' },
      { label: '智慧补光', layerName: 'lightSignLayer', lightSignLayer: true, id: '014', roadIdKey: 'road_id' },
      { label: '雾区诱导', layerName: 'fogGuideLayer', fogGuideLayer: true, id: '012', organizationSql: true },
      { label: '雾区诱导标签', layerName: 'fogGuideTextLayer', fogGuideTextLayer: false, id: '0121' },
      { label: '动静一体化标志', layerName: 'moveStaticLayer', moveStaticLayer: true, id: '007', roadIdKey: 'road_id' },
      { label: '距离标志', layerName: 'distanceMarkerLayer', distanceMarkerLayer: false, id: '001', roadIdKey: 'road_id' },

    ],
    roadTestPerceptionPublishEquipment: [//路侧设备配置-感知发布-一体
      { label: '智慧锥桶', layerName: 'coneBarrelLayer', coneBarrelLayer: false, id: '043', roadIdKey: 'road_id' },
      { label: '合流预警', layerName: 'confluenceLayer', confluenceLayer: true, id: '001', roadIdKey: 'road_id' },
      { label: 'RSU', layerName: 'rsuLayer', rsuLayer: true, id: '001', roadIdKey: 'road_id' },
      { label: '护栏碰撞', layerName: 'barrierCollisionWarningLayer', barrierCollisionWarningLayer: true, id: '001', organizationSql: true },
      { label: '护栏碰撞', layerName: 'barrierCollisionWarningTextLayer', barrierCollisionWarningTextLayer: false, id: '001' },
    ],
    roadTestOthersEquipment: [//路侧设备配置-其他
      { label: 'MEC', layerName: 'mecLayer', mecLayer: true, id: '004', roadIdKey: 'road_id' },
      { label: '桥梁健康', layerName: 'bridgeHealthLayer', bridgeHealthLayer: true, id: '001', roadIdKey: 'road_id' },
      { label: '智能机箱', layerName: 'smartBoxLayer', smartBoxLayer: true, id: '001', roadIdKey: 'road_id' },
      { label: '北斗基站', layerName: 'baseStationLayer', baseStationLayer: true, id: '001', roadIdKey: 'road_id' },
      { label: '融冰除雪', layerName: 'removeSnowLayer', removeSnowLayer: true, id: '001', organizationSql: true },
      { label: '融冰除雪标签', layerName: 'removeSnowTextLayer', removeSnowTextLayer: false, id: '001' },
    ],
    roadNetworkFacilities: [//路网设施配置
      { label: '服务区', layerName: 'serviceLayerJQ', serviceLayerJQ: true, id: '003', roadIdKey: 'road_id', opmaManageIdKey: { 'opma_manag': 'opmaManagerCorpIds', 'opma_man_2': 'opmaManagerIds' } },
      { label: '收费站', layerName: 'stationLayerJQ', stationLayerJQ: true, id: '004', roadIdKey: 'road_id', opmaManageIdKey: { 'opma_manag': 'opmaManagerCorpIds', 'opma_man_2': 'opmaManagerIds' } },
      { label: '枢纽立交', layerName: 'pivotLayerJQ', pivotLayerJQ: true, id: '005', roadIdKey: 'road_id', opmaManageIdKey: { 'opma_manag': 'opmaManagerCorpIds', 'opma_man_2': 'opmaManagerIds' } },
      { label: '互通立交', layerName: 'interchangeLayerJQ', interchangeLayerJQ: true, id: '011', roadIdKey: 'road_id', opmaManageIdKey: { 'opma_manag': 'opmaManagerCorpIds', 'opma_man_2': 'opmaManagerIds' } },
      { label: '路管机构', layerName: 'organizationLayer', organizationLayer: false, id: '109', roadIdKey: 'road_id', opmaManageIdKey: { 'opma_manag': 'opmaManagerCorpIds', 'opma_man_2': 'opmaManagerIds' } },
      { label: '桥梁', layerName: 'bridgeLayerJQ', bridgeLayerJQ: true, id: '007', roadIdKey: 'road_id', opmaManageIdKey: { 'opma_manag': 'opmaManagerCorpIds', 'opma_man_2': 'opmaManagerIds' } },
      { label: '隧道', layerName: 'tunnelLayer', tunnelLayer: false, id: '105', roadIdKey: 'road_id', opmaManageIdKey: { 'opma_manag': 'opmaManagerCorpIds', 'opma_man_2': 'opmaManagerIds' } },
      { label: '隧道', layerName: 'lineTunnelLayer', lineTunnelLayer: true, id: '105', organizationSql: true }, //线隧道
      { label: 'ETC门架', layerName: 'ETCLayer', ETCLayer: true, id: '106', roadIdKey: 'road_id', opmaManageIdKey: { 'opma_manag': 'opmaManagerCorpIds', 'opma_man_2': 'opmaManagerIds' } },
      { label: '道路线', layerName: 'roadlineLayer', roadlineLayer: false, id: '015', roadIdKey: 'ROADID', opmaManageIdKey: { 'opma_manag': 'opmaManagerCorpIds', 'opma_man_2': 'opmaManagerIds' } },
      { label: '行政区划', layerName: 'districtLayer', districtLayer: false, id: '100' },
      { label: '桩号', layerName: 'pileLayer', pileLayer: false, id: '006' },
      { label: '共享路段', layerName: 'shareRoadLayer', shareRoadLayer: false, id: '014', organizationSql: true },
      { label: '建设工程', layerName: 'engineeringBuild', engineeringBuild: false, id: '016', organizationSql: true },
    ],
    maintenance: [ //建设养护配置
      { label: '道路损坏', layerName: 'roadDamageLayer', roadDamageLayer: false, id: '017', roadIdKey: 'road_id', organizationSql: true },
      { label: '路面磨耗', layerName: 'roadConsumeLayer', roadConsumeLayer: false, id: '022', roadIdKey: 'road_id', organizationSql: true },
      { label: '沥青路面损坏', layerName: 'asphaltRoadDamageLayer', asphaltRoadDamageLayer: false, id: '023', roadIdKey: 'road_id', organizationSql: true },
      { label: '路面跳车', layerName: 'roadBumpLayer', roadBumpLayer: false, id: '024', roadIdKey: 'road_id', organizationSql: true },
      { label: '路面抗滑', layerName: 'roadSkidLayer', roadSkidLayer: false, id: '025', roadIdKey: 'road_id', organizationSql: true },
      { label: '路面平整度', layerName: 'roadSmoothLayer', roadSmoothLayer: false, id: '026', roadIdKey: 'road_id', organizationSql: true },
      { label: '路面车辙', layerName: 'roadTrackLayer', roadTrackLayer: false, id: '027', roadIdKey: 'road_id', organizationSql: true },
      { label: '物资装备', layerName: 'emergencyLayer', emergencyLayer: false, id: '018', roadIdKey: 'road_id', opmaManageIdKey: { 'opma_manag': 'opmaManagerCorpIds', 'opma_man_2': 'opmaManagerIds' }, },
      { label: '交安标识', layerName: 'trafficSafetyLayer', trafficSafetyLayer: false, id: '021', roadIdKey: 'road_id', opmaManageIdKey: { 'opma_manag': 'opmaManagerCorpIds', 'opma_man_2': 'opmaManagerIds' }, organizationSql: true },
    ],
  },
  danao: { //大脑配置 跟济青一样
    eventTopicsShow: true,
    perceptionEventShow: true,
    roadTestEquipmentShow: false,
    equipmentFacilitiesShow: true,//设备设施
    otherClassShow: true,
    roadNetworkFacilitiesShow: false,
    maintenanceShow: true,
    roadAndBridgeTunnelShow: true,
    mapTreeShow: true,
    toolbarShow: true,
    legendShow: true, //图例是否显示
    roadIDs: [],//'G002237' 道路id配置
    //路桥隧道
    roadAndBridgeTunnel: [
      { label: '道路线', layerName: 'roadlineLayer', roadlineLayer: true, id: '015', roadIdKey: 'ROADID', opmaManageIdKey: { 'opma_manag': 'opmaManagerCorpIds', 'opma_man_2': 'opmaManagerIds' } },
      { label: '桩号', layerName: 'pileLayer', pileLayer: true, id: '006' },
      { label: '桥梁', layerName: 'bridgeLayer', bridgeLayer: true, id: '007', roadIdKey: 'road_id', opmaManageIdKey: { 'opma_manag': 'opmaManagerCorpIds', 'opma_man_2': 'opmaManagerIds' } },
      { label: '隧道', layerName: 'tunnelLayer', tunnelLayer: true, id: '105', roadIdKey: 'road_id', opmaManageIdKey: { 'opma_manag': 'opmaManagerCorpIds', 'opma_man_2': 'opmaManagerIds' } },
    ],
    //设施、设备
    equipmentFacilities: [
      { label: '摄像机', layerName: 'videoLayer', videoLayer: true, id: '003', roadIdKey: 'road_id', opmaManageIdKey: { 'opma_manag': 'opmaManagerCorpIds', 'opma_man_2': 'opmaManagerIds' } },
      { label: '情报板', layerName: 'boardLayer', boardLayer: true, id: '005', roadIdKey: 'road_id', opmaManageIdKey: { 'opma_manag': 'opmaManagerCorpIds', 'opma_man_2': 'opmaManagerIds' } },
      { label: '服务区', layerName: 'serviceLayer', serviceLayer: true, id: '003', roadIdKey: 'road_id', opmaManageIdKey: { 'opma_manag': 'opmaManagerCorpIds', 'opma_man_2': 'opmaManagerIds' } },
      { label: '收费站', layerName: 'stationLayer', stationLayer: true, id: '004', roadIdKey: 'road_id', opmaManageIdKey: { 'opma_manag': 'opmaManagerCorpIds', 'opma_man_2': 'opmaManagerIds' } },
      { label: '枢纽立交', layerName: 'pivotLayer', pivotLayer: true, id: '005', roadIdKey: 'road_id', opmaManageIdKey: { 'opma_manag': 'opmaManagerCorpIds', 'opma_man_2': 'opmaManagerIds' } },
      { label: '互通立交', layerName: 'interchangeLayer', interchangeLayer: true, id: '011', roadIdKey: 'road_id', opmaManageIdKey: { 'opma_manag': 'opmaManagerCorpIds', 'opma_man_2': 'opmaManagerIds' } },
      { label: '路管机构', layerName: 'organizationLayer', organizationLayer: true, id: '109', roadIdKey: 'road_id', opmaManageIdKey: { 'opma_manag': 'opmaManagerCorpIds', 'opma_man_2': 'opmaManagerIds' } },
      { label: 'ETC门架', layerName: 'ETCLayer', ETCLayer: true, id: '106', roadIdKey: 'road_id', opmaManageIdKey: { 'opma_manag': 'opmaManagerCorpIds', 'opma_man_2': 'opmaManagerIds' } },
    ],
    roadTestPublishEquipment: [//路侧设备配置

    ],
    roadTestPerceptionPublishEquipment: [//路侧设备配置

    ],
    roadTestOthersEquipment: [],
    otherClass: [
      { label: '物资装备', layerName: 'emergencyLayer', emergencyLayer: true, id: '018', opmaManageIdKey: { 'opma_manag': 'opmaManagerCorpIds', 'opma_man_2': 'opmaManagerIds' }, },
      { label: '交通态势', layerName: 'trafficSituationLayer', trafficSituationLayer: true, id: '019', organizationSql: true },
      { label: '交安标识', layerName: 'trafficSafetyLayer', trafficSafetyLayer: true, id: '021', roadIdKey: 'road_id', opmaManageIdKey: { 'opma_manag': 'opmaManagerCorpIds', 'opma_man_2': 'opmaManagerIds' }, organizationSql: true },
      { label: '气象数据', layerName: 'meteorologicaldataLayer', meteorologicaldataLayer: true, id: '006', roadIdKey: 'roadId', opmaManageIdKey: { 'opma_manag': 'opmaManagerCorpIds', 'opma_man_2': 'opmaManagerIds' } },
      { label: '行政区划', layerName: 'districtLayer', districtLayer: true, id: '100' },
      { label: '共享路段', layerName: 'shareRoadLayer', shareRoadLayer: true, id: '014', organizationSql: true },
      { label: '应急车辆', layerName: 'patrolAccidentLayer', patrolAccidentLayer: true, id: '020', opmaManageIdKey: { 'opmaManag': 'opmaManagerCorpIds', 'opmaMan2': 'opmaManagerIds' }, organizationSql: true },
      { label: '高德路况', layerName: 'gaodetrafficLayer', gaodetrafficLayer: true, id: '0002' },

    ],
    eventTopics: [
      { label: '服务区异常', layerName: 'serviceAreaExceptionLayer', serviceAreaExceptionLayer: true, id: '1' },
      { label: '施工建设', layerName: 'constructionLayer', constructionLayer: true, id: '1' },
      { label: '设备设施隐患', layerName: 'sbssyhLayer', sbssyhLayer: true, id: '1' },
      { label: '路障清除', layerName: 'roadblockRemovalLayer', roadblockRemovalLayer: true, id: '1' },
      { label: '非法上路', layerName: 'breakIntoShLayer', breakIntoShLayer: true, id: '16' },
      { label: '交通拥堵', layerName: 'trafficJamLayer', trafficJamLayer: true, id: '2' },
      { label: '交通管制', layerName: 'trafficControlLayer', trafficControlLayer: true, id: '2' },
      { label: '车辆故障', layerName: 'vehicleFaultLayer', vehicleFaultLayer: true, id: '2' },
      { label: '交通事故', layerName: 'trafficAccidentLayer', trafficAccidentLayer: true, id: '12' },
      { label: '异常天气', layerName: 'abnormalWeatherLayer', abnormalWeatherLayer: true, id: '12' },
      { label: '其他事件', layerName: 'otherLayer', otherLayer: true, id: '99' },
      // { label: '道路', layerName: 'roadLayer', roadLayer: true, id: '3' },
      // { label: '道路积水', layerName: 'roadPondingLayer', roadPondingLayer: true, id: '5' },
      // { label: '道路团雾', layerName: 'roadMassFogLayer', roadMassFogLayer: true, id: '6' },
      // { label: '大风', layerName: 'galeLayer', galeLayer: true, id: '7' },
      // { label: '大雾', layerName: 'denseFogLayer', denseFogLayer: true, id: '8' },
      // { label: '道路湿滑', layerName: 'roadWetSlipperyLayer', roadWetSlipperyLayer: true, id: '9' },
      // { label: '道路施工', layerName: 'roadConstructionLayer', roadConstructionLayer: true, id: '10' },
      // { label: '超速', layerName: 'speedingLayer', speedingLayer: true, id: '14' },
      // { label: '排队超限', layerName: 'queueOverrunLayer', queueOverrunLayer: true, id: '17' },
    ],
    perceptionEvent: [
      { label: '交通拥堵', layerName: 'trafficJamGzLayer', trafficJamGzLayer: true, id: '2' },
      { label: '行人横穿', layerName: 'pedestrianCrossingGzLayer', pedestrianCrossingGzLayer: true, id: '18' },
      { label: '非机动车', layerName: 'nonMotorVehicletGzLayer', nonMotorVehicletGzLayer: true, id: '19' },
      { label: '停车', layerName: 'parkingGzLayer', parkingGzLayer: true, id: '4' },
      { label: '倒车', layerName: 'trafficRetrogradeGzLayer', trafficRetrogradeGzLayer: true, id: '1' },
      { label: '烟火', layerName: 'fireworksGzLayer', fireworksGzLayer: true, id: '20' },
      { label: '撒落物', layerName: 'throwGzLayer', throwGzLayer: true, id: '20' },
      /* { label: '车辆慢行', layerName: 'slowDownGzLayer', slowDownGzLayer: true, id: '11' },
      { label: '超速', layerName: 'speedingGzLayer', speedingGzLayer: true, id: '14' },
      { label: '占用应急车道', layerName: 'emergencyLanesOccupancyGzLayer', emergencyLanesOccupancyGzLayer: true, id: '15' },
      { label: '异常变道', layerName: 'abnormalLaneChangeGzLayer', abnormalLaneChangeGzLayer: true, id: '13' },
      { label: '其他', layerName: 'otherGzLayer', otherGzLayer: true, id: '99' }
     */
    ],
    roadTestEquipment: [
      { label: '摄像机', layerName: 'videoLayer', videoLayer: true, id: '003', roadIdKey: 'road_id', opmaManageIdKey: { 'opma_manag': 'opmaManagerCorpIds', 'opma_man_2': 'opmaManagerIds' } },
      { label: '情报板', layerName: 'boardLayer', boardLayer: true, id: '005', roadIdKey: 'road_id', opmaManageIdKey: { 'opma_manag': 'opmaManagerCorpIds', 'opma_man_2': 'opmaManagerIds' } },
      { label: 'MEC', layerName: 'mecLayer', mecLayer: false, id: '004', },
      { label: '广播', layerName: 'radioLayer', radioLayer: false, id: '015', },
      { label: '交调', layerName: 'CMLayer', CMLayer: false, id: '009', },
      { label: '能见度检测', layerName: 'visibilityLayer', visibilityLayer: false, id: '007', },
      { label: '路面状态', layerName: 'roadstateLayer', roadstateLayer: false, id: '105', },
      { label: '气象', layerName: 'meteorologicalLayer', meteorologicalLayer: false, id: '006', opmaManageIdKey: { 'managerCorpId': 'opmaManagerCorpIds', 'managerId': 'opmaManagerIds' } },
      // { label: '卡口', layerName: 'mountLayer', mountLayer: false, id: '109', },
      { label: '智慧补光', layerName: 'lightSignLayer', lightSignLayer: false, id: '014', },
      { label: '微波车检', layerName: 'rippleLayer', rippleLayer: false, id: '011', },
      { label: '雷达', layerName: 'hmbRadarLayer', hmbRadarLayer: false, id: '001', },
      { label: '雾区诱导', layerName: 'fogGuideLayer', fogGuideLayer: false, id: '012', organizationSql: true },
      { label: '动静一体化标志', layerName: 'moveStaticLayer', moveStaticLayer: false, id: '007', },
      { label: '智慧锥桶', layerName: 'coneBarrelLayer', coneBarrelLayer: false, id: '043', },
      { label: '雷视一体', layerName: 'radarAndvideoLayer', radarAndvideoLayer: false, id: '001', },
      { label: '智能机箱', layerName: 'smartBoxLayer', smartBoxLayer: false, id: '001', },
      // { label: '车位指示', layerName: 'parkingSignLayer', parkingSignLayer: false, id: '001', },
      { label: '北斗基站', layerName: 'baseStationLayer', baseStationLayer: false, id: '001', },
      { label: 'RSU', layerName: 'rsuLayer', rsuLayer: false, id: '001', },
      { label: '护栏碰撞', layerName: 'barrierCollisionWarningLayer', barrierCollisionWarningLayer: false, id: '001', organizationSql: true },
      { label: '桥梁健康', layerName: 'bridgeHealthLayer', bridgeHealthLayer: false, id: '001', },
      // { label: '疲劳唤醒', layerName: 'awakenLayer', awakenLayer: false, id: '001', },
      { label: '合流预警', layerName: 'confluenceLayer', confluenceLayer: false, id: '001', },
      { label: '融冰除雪', layerName: 'removeSnowLayer', removeSnowLayer: false, id: '001', organizationSql: true },
      // { label: '智慧道钉', layerName: 'spikeLayer', spikeLayer: false, id: '001', },
      { label: '距离标志', layerName: 'distanceMarkerLayer', distanceMarkerLayer: false, id: '001', },
      // { label: '路段广播', layerName: 'sectionRadioLayer', sectionRadioLayer: false, id: '001', },
      // { label: '北斗高精度', layerName: 'beidouLayer', beidouLayer: false, id: '001', },
      // { label: '图标测试', layerName: 'iconTestlLayer', iconTestlLayer: true, id: '107',organizationSql:true },
    ],
    roadNetworkFacilities: [
      { label: '服务区', layerName: 'serviceLayer', serviceLayer: true, id: '003', roadIdKey: 'road_id', opmaManageIdKey: { 'opma_manag': 'opmaManagerCorpIds', 'opma_man_2': 'opmaManagerIds' } },
      { label: '收费站', layerName: 'stationLayer', stationLayer: true, id: '004', roadIdKey: 'road_id', opmaManageIdKey: { 'opma_manag': 'opmaManagerCorpIds', 'opma_man_2': 'opmaManagerIds' } },
      { label: '枢纽立交', layerName: 'pivotLayer', pivotLayer: true, id: '005', roadIdKey: 'road_id', opmaManageIdKey: { 'opma_manag': 'opmaManagerCorpIds', 'opma_man_2': 'opmaManagerIds' } },
      { label: '互通立交', layerName: 'interchangeLayer', interchangeLayer: true, id: '011', roadIdKey: 'road_id', opmaManageIdKey: { 'opma_manag': 'opmaManagerCorpIds', 'opma_man_2': 'opmaManagerIds' } },
      { label: '路管机构', layerName: 'organizationLayer', organizationLayer: true, id: '109', roadIdKey: 'road_id', opmaManageIdKey: { 'opma_manag': 'opmaManagerCorpIds', 'opma_man_2': 'opmaManagerIds' } },
      { label: '桥梁', layerName: 'bridgeLayer', bridgeLayer: true, id: '007', roadIdKey: 'road_id', opmaManageIdKey: { 'opma_manag': 'opmaManagerCorpIds', 'opma_man_2': 'opmaManagerIds' } },
      { label: '隧道', layerName: 'tunnelLayer', tunnelLayer: true, id: '105', roadIdKey: 'road_id', opmaManageIdKey: { 'opma_manag': 'opmaManagerCorpIds', 'opma_man_2': 'opmaManagerIds' } },
      { label: 'ETC门架', layerName: 'ETCLayer', ETCLayer: true, id: '106', roadIdKey: 'road_id', opmaManageIdKey: { 'opma_manag': 'opmaManagerCorpIds', 'opma_man_2': 'opmaManagerIds' } },
      { label: '道路线', layerName: 'roadlineLayer', roadlineLayer: true, id: '015', roadIdKey: 'ROADID', opmaManageIdKey: { 'opma_manag': 'opmaManagerCorpIds', 'opma_man_2': 'opmaManagerIds' } },
      { label: '行政区划', layerName: 'districtLayer', districtLayer: true, id: '100' },
      { label: '桩号', layerName: 'pileLayer', pileLayer: true, id: '006' },
      { label: '共享路段', layerName: 'shareRoadLayer', shareRoadLayer: true, id: '014', organizationSql: true },
      // { label: '建设工程', layerName: 'engineeringBuild', engineeringBuild: true, id: '016' },
    ],
    maintenance: [
      { label: '建设工程', layerName: 'engineeringBuild', engineeringBuild: true, id: '016', organizationSql: true },
      { label: '道路损坏', layerName: 'roadDamageLayer', roadDamageLayer: true, id: '017', organizationSql: true },
      { label: '路面磨耗', layerName: 'roadConsumeLayer', roadConsumeLayer: true, id: '022', organizationSql: true },
      { label: '沥青路面损坏', layerName: 'asphaltRoadDamageLayer', asphaltRoadDamageLayer: true, id: '023', organizationSql: true },
      { label: '路面跳车', layerName: 'roadBumpLayer', roadBumpLayer: true, id: '024', organizationSql: true },
      { label: '路面抗滑', layerName: 'roadSkidLayer', roadSkidLayer: true, id: '025', organizationSql: true },
      { label: '路面平整度', layerName: 'roadSmoothLayer', roadSmoothLayer: true, id: '026', organizationSql: true },
      { label: '路面车辙', layerName: 'roadTrackLayer', roadTrackLayer: true, id: '027', organizationSql: true },

      // { label: '交通流量', layerName: 'trafficFlowLayer', trafficFlowLayer: true, id: '018' },
    ],
  },
  //设备设施图片链接
  equipmentFacilitiesImgUrl: 'http://10.166.157.192:31315/file/getGisImg/',
  //服务跳转连接
  jumpService: [
    { label: '昌乐南服务区', jumpUrl: 'http://172.11.11.254:3003/serviceArea/sys/thirdLogin/oauth2/shangao/callback?code={authCode}&state=http://172.11.11.254:3003&redirect=/yun-amap?mcode=dp003' },
    { label: '坊安服务区', jumpUrl: 'http://10.7.58.2:3003/serviceArea/sys/thirdLogin/oauth2/shangao/callback?code={authCode}&state=http://10.7.58.2:3003&redirect=/yun-amap?mcode=dp001' }
  ],
  //设备跳转连接
  jumpRemoveSnow: "http://10.7.44.64:8080/HIMMS",
  jumpEquipmentService: [

  ],
  //感知事件获取时间类型及视频接口地址 -交通事件基本信息及附件相机情报板
  trafficMonitorUrl: "http://10.166.157.192:31315",//正式地址
  //trafficMonitorUrl: "http://10.166.154.213:30001",//测试环境
  trafficMonitorToken: "28618fac-5eb0-4349-bde0-5cf65eb1e4f8",//对应token
  trafficMonitorType: "PROD",//PROD 正式，TEST 测试

  //共用变量
  //mqttApi: 'ws://10.166.157.191:8083/mqtt',  //正式环境
  mqttApi: 'ws://10.166.154.124:8083/mqtt',  //正式环境
  //mqttApi: 'ws://10.166.154.211:8083/mqtt', //测试环境
  //数据中台接口地址：
  masterdataUrl: "http://10.166.154.106:32725",
  masterdataToken: "1dde1bc2-a706-4a53-8a70-b8eadb2506a1",
  /*图层自选展示   true为展示  false为隐藏*/
  mecLayer: true, //mec图层
  radioLayer: true, //广播图层
  CMLayer: true, //交调图层
  visibilityLayer: true, //能见度图层
  roadstateLayer: false, //路面状态图层
  meteorologicalLayer: true, //气象图层
  mountLayer: false, //卡口相机图层
  lightSignLayer: true, // 主动发光标志图层
  rippleLayer: true, // 微波车检器图层
  hmbRadarLayer: true, // 雷达图层
  videoLayer: true, // 摄像机图层
  jqvideoLayer: true,
  boardLayer: true, // 情报板图层
  fogGuideLayer: true,//雾区诱导图层
  fogGuideTextLayer: true,
  boardAllLayer: true,
  moveStaticLayer: true,//动静结合一体机图层
  coneBarrelLayer: false,//智慧锥桶
  radarAndvideoLayer: true,//雷视一体机
  smartBoxLayer: true,//智能机箱
  parkingSignLayer: false,//车位指示屏
  baseStationLayer: true,//北斗基站
  barrierCollisionWarningLayer: true, // 护栏碰撞预警
  barrierCollisionWarningTextLayer: true, // 护栏碰撞预警
  bridgeHealthLayer: true, // 桥梁健康监测
  rsuLayer: true, // RSU
  // awakenLayer: false,//唤醒疲劳
  // spikeLayer: false,//智慧道钉
  confluenceLayer: true,//合流预警
  removeSnowLayer: true,//融冰除雪
  removeSnowTextLayer: true,//融冰除雪
  distanceMarkerLayer: false,//距离标志
  sectionRadioLayer: false,//路段广播
  beidouLayer: false,//北斗高精度
  iconTestlLayer: false,//图标测试
  // 路网设施
  serviceLayer: true, //服务区图层
  pivotLayer: true, //枢纽立交图层
  interchangeLayer: true,// 互通立交图层
  stationLayer: true,//收费站图层
  roadlineLayer: true,// 道路线图层
  bridgeLayer: true,//桥梁图层
  tunnelLayer: true,//隧道图层
  lineTunnelLayer: true,//线隧道图层
  pileLayer: true,//桩号图层
  organizationLayer: true,//路管机构图层
  shareRoadLayer: true,//共享路段图层
  districtLayer: true,// 行政区划
  jiqingHLLayer: true, // 济青全线高亮
  jiqingMidLayer: true, // 济青连接线高亮
  testBaseLayer: false, // 网联测试基地道路线
  ETCLayer: true,//ETC
  serviceLayerJQ: true, //济青服务区图层
  pivotLayerJQ: true, //济青枢纽立交图层
  interchangeLayerJQ: true,//济青互通立交图层
  stationLayerJQ: false,//济青收费站图层
  bridgeLayerJQ: true,//济青桥梁图层
  rasterArrayLayer: true, //光栅阵列
  //交通事件
  serviceAreaExceptionLayer: true,//服务区异常
  constructionLayer: true,//施工建设
  sbssyhLayer: true,//设施设备隐患
  roadblockRemovalLayer: true,//路障清除
  breakIntoShLayer: true,//非法上路
  trafficJamLayer: true,//交通拥堵
  trafficControlLayer: true,//交通管制
  vehicleFaultLayer: true,//车辆故障
  trafficAccidentLayer: true,//交通事故
  abnormalWeatherLayer: true,//异常天气
  roadLayer: false,
  roadPondingLayer: true,
  roadMassFogLayer: true,
  galeLayer: true,
  denseFogLayer: true,
  roadWetSlipperyLayer: true,
  roadConstructionLayer: true,
  speedingLayer: true,
  queueOverrunLayer: true,
  otherLayer: true,//其他事件
  //感知事件
  trafficJamGzLayer: true,//交通庸俗
  pedestrianCrossingGzLayer: true,//行人
  nonMotorVehicletGzLayer: true,//非机动车
  parkingGzLayer: true,//停车
  trafficRetrogradeLayer: true,//倒车
  fireworksGzLayer: true,//烟火
  throwGzLayer: true,//撒落物
  slowDownGzLayer: true,//车辆慢行
  speedingGzLayer: true,//超速
  emergencyLanesOccupancyGzLayer: true,//占用应急车道
  abnormalLaneChangeGzLayer: true,//异常变道
  otherGzLayer: true,//其他

  //建设养护
  roadDamageLayer: true,//道路损坏
  roadConsumeLayer: true,//路面磨耗
  asphaltRoadDamageLayer: true,//沥青路面损坏
  roadBumpLayer: true,//路面跳车
  roadSkidLayer: true,//路面抗滑
  roadSmoothLayer: true,//路面平整度
  roadTrackLayer: true,//路面车辙
  engineeringBuild: true,//建设工程图层
  emergencyLayer: true,//物资装备图层
  // trafficFlowLayer: false,//交通流量
  trafficSituationLayer: true,//交通态势
  patrolAccidentLayer: true,//应急车辆（安装定位设备）
  gaodeTrafficLayer: false,//高德路况
  congestionSectionLayer: false,// 自有交通路况
  trafficSafetyLayer: true,//交安标识图层
  meteorologicaldataLayer: true,//气象数据图层
  /*深色图层服务地址*/
  // BaseMapUrl: "http://10.166.154.217:6080/arcgis/rest/services/BaseService/darkColor/MapServer",
  BaseMapUrl: "http://10.166.154.101:6080/arcgis/rest/services/BaseService/darkColor/MapServer",
  /*浅色图层服务地址*/
  BaseMapUrl_L: "http://10.166.154.217:6080/arcgis/rest/services/BaseService/lightColor/MapServer",
  /*济青道路中心线*/
  // jiqingRoadUrl_dark: 'http://10.166.154.217:6080/arcgis/rest/services/Facility/jq_road/MapServer',
  // jiqingRoadUrl_dark: 'http://10.166.154.217:6080/arcgis/rest/services/BaseService/roadTile/MapServer',
  jiqingRoadUrl_dark: 'http://10.166.154.217:6080/arcgis/rest/services/BaseService/roadCache/MapServer',
  jiqingRoadUrl_light: 'http://10.166.154.217:6080/arcgis/rest/services/BaseService/jq_road_light/MapServer',
  /*济青高亮线*/
  jiqingHLUrl: 'http://10.166.154.101:6080/arcgis/rest/services/Facility/jq_all/MapServer',
  /*京台高亮线*/
  jingtaiHLUrl: 'http://10.166.154.217:6080/arcgis/rest/services/Facility/jt_road/MapServer',
  /*济青连接线*/
  jiqingMidUrl: 'http://10.166.154.217:6080/arcgis/rest/services/Facility/jq_middle/MapServer',
  /*网联测试基地道路线 */
  testBaseUrl: 'http://10.166.154.217:6080/arcgis/rest/services/Facility/jidi_road/MapServer',

  /* 路侧设备*/
  hmbRadarLayerUrl: "http://10.166.154.217:6080/arcgis/rest/services/Device/LD/MapServer", // 雷达
  radioLayerUrl: "http://10.166.154.217:6080/arcgis/rest/services/Device/GB/MapServer", // 广播
  barrierCollisionWarningLayerUrl: "http://10.166.154.101:6080/arcgis/rest/services/Maintain/hlpz_road/MapServer", // 护栏碰撞预警
  barrierCollisionWarningTextLayerUrl: "http://10.166.154.101:6080/arcgis/rest/services/Facility/HLPZ_Center/MapServer",// 护栏碰撞预警标签
  bridgeHealthLayerUrl: "http://10.166.154.217:6080/arcgis/rest/services/Device/QLJKJC/MapServer", // 桥梁健康监测
  rsuLayerUrl: "http://10.166.154.217:6080/arcgis/rest/services/Device/RSU/MapServer", // RSU
  MECLaryerUrl: "http://10.166.154.217:6080/arcgis/rest/services/Device/MEC/MapServer", // MEC
  CMLayerUrl: "http://10.166.154.217:6080/arcgis/rest/services/Device/JDSB/MapServer", // 交调
  visibilityLayerUrl: "http://10.166.154.217:6080/arcgis/rest/services/Device/NJD/MapServer", // 能见度检测
  meteorologicalLayerUrl: "http://10.166.154.217:6080/arcgis/rest/services/Device/QXJCQ/MapServer", // 气象
  mountLayerUrl: "http://10.166.154.217:6080/arcgis/rest/services/Device/KKZPXT/MapServer", // 卡口
  lightSignLayerUrl: "http://10.166.154.217:6080/arcgis/rest/services/Device/BZZHBGXT/MapServer", // 发光标志  智慧补光
  rippleLayerUrl: "http://10.166.154.217:6080/arcgis/rest/services/Device/WBCJ/MapServer", // 微波检测
  videoLayerUrl: "http://10.166.154.217:6080/arcgis/rest/services/Device/camera/MapServer", // 摄像机
  jqvideoLayerUrl: "http://10.166.154.217:6080/arcgis/rest/services/Facility/jq_sxj/MapServer", //济青摄像机
  boardLayerUrl: "http://10.166.154.217:6080/arcgis/rest/services/Device/KBXXBZ/MapServer", // 情报板
  fogGuideLayerUrl: 'http://10.166.154.101:6080/arcgis/rest/services/Maintain/yd_road/MapServer', // 雾区诱导
  fogGuideTextLayerUrl: 'http://10.166.154.101:6080/arcgis/rest/services/Facility/wqyd_Center/MapServer', // 雾区诱导标签
  radarAndvideoLayerUrl: 'http://10.166.154.217:6080/arcgis/rest/services/Device/LSYTJ/MapServer', // 雷视一体
  smartBoxLayerUrl: 'http://10.166.154.217:6080/arcgis/rest/services/Device/ZNPDG/MapServer', // 智能机箱
  parkingSignLayerUrl: 'http://10.166.154.217:6080/arcgis/rest/services/Device/FWQQCWZSP/MapServer', // 车位指示屏
  baseStationLayerUrl: 'http://10.166.154.217:6080/arcgis/rest/services/Device/LCTXJZ/MapServer', // 北斗基站
  awakenLayerUrl: 'http://10.166.154.217:6080/arcgis/rest/services/Device/HXPLSB/MapServer', // 疲劳唤醒
  confluenceLayerUrl: 'http://10.166.154.217:6080/arcgis/rest/services/Device/HLQYJSB/MapServer', // 合流预警
  removeSnowLayerUrl: 'http://10.166.154.101:6080/arcgis/rest/services/Maintain/RBCX_line/MapServer', // 融冰除雪
  removeSnowTextLayerUrl: "http://10.166.154.101:6080/arcgis/rest/services/Facility/RBCX_Center/MapServer",// 融冰除雪标签
  removeSnowPtLayerUrl: 'http://10.166.154.101:6080/arcgis/rest/services/Facility/RBCX_Center/MapServer',// 融冰除雪中心点
  spikeLayerUrl: 'http://10.166.154.217:6080/arcgis/rest/services/Device/ZHDD/MapServer', // 智慧道钉
  // distanceMarkerLayerUrl: 'http://10.166.154.217:6080/arcgis/rest/services/Device/DDJLBZ/MapServer', // 距离标志
  sectionRadioLayerUrl: 'http://10.166.154.217:6080/arcgis/rest/services/Device/LDGBYJ/MapServer', // 路段广播
  beidouLayerUrl: 'http://10.166.154.217:6080/arcgis/rest/services/Device/BDGJDSKFWXT/MapServer', // 北斗高精度

  // roadstateLayerUrl: "http://10.168.28.37:6080/arcgis/rest/services/yh/roadM/MapServer", // 路面状态
  moveStaticLayerUrl: 'http://10.166.154.217:6080/arcgis/rest/services/Device/YTHDJJHBZ/MapServer', // 一体化动静结合
  // coneBarrelLayerUrl: 'http://10.168.28.37:6080/arcgis/rest/services/yh/MECJQ/MapServer', // 智慧锥桶
  iconTestlLayerUrl: 'http://10.166.154.217:6080/arcgis/rest/services/Facility/ceshi/MapServer',//图标测试
  rasterArrayLayerUrl: 'http://10.166.154.101:6080/arcgis/rest/services/Maintain/GSZL/MapServer',//光栅阵列
  /* 路网设施 */
  serviceLayerUrl: 'http://10.166.154.217:6080/arcgis/rest/services/Facility/FWQ_T/MapServer', // 服务区
  stationLayerUrl: 'http://10.166.154.217:6080/arcgis/rest/services/Facility/SFZ/MapServer', // 收费站
  organizationLayerUrl: 'http://10.166.154.217:6080/arcgis/rest/services/Facility/LGJG/MapServer', // 机构
  interchangeLayerUrl: 'http://10.166.154.217:6080/arcgis/rest/services/Facility/HTLJ/MapServer', // 互通立交
  pivotLayerUrl: 'http://10.166.154.217:6080/arcgis/rest/services/Facility/SNLJ/MapServer',  // 枢纽立交
  bridgeLayerUrl: 'http://10.166.154.217:6080/arcgis/rest/services/Facility/QL/MapServer', // 桥梁
  tunnelLayerUrl: 'http://10.166.154.217:6080/arcgis/rest/services/Facility/SD/MapServer',// 隧道
  lineTunnelLayerUrl: 'http://10.166.154.101:6080/arcgis/rest/services/Maintain/sd_road_2/MapServer',// 线隧道
  pileLayerUrl: 'http://10.166.154.217:6080/arcgis/rest/services/Facility/ZH/MapServer', // 桩号
  roadlineLayerUrl: 'http://10.166.154.101:6080/arcgis/rest/services/Facility/road/MapServer',// 道路线
  selectRoadLayerUrl: 'http://10.166.154.101:6080/arcgis/rest/services/Maintain/distric_road/MapServer',// 道路线高亮
  shareRoadLayerUrl: 'http://10.166.154.101:6080/arcgis/rest/services/Facility/shareRoad/MapServer', // 共享路段
  administrativeAreaLayerUrl: "http://10.166.154.101:6080/arcgis/rest/services/Facility/District/MapServer", // 行政区划
  ETCLayerUrl: "http://10.166.154.217:6080/arcgis/rest/services/Facility/ETC/MapServer",//ETC
  serviceLayerJQUrl: 'http://10.166.154.217:6080/arcgis/rest/services/Facility/jq_fwq/MapServer', // 济青服务区
  stationLayerJQUrl: 'http://10.166.154.217:6080/arcgis/rest/services/Facility/jq_sfz/MapServer', // 济青收费站
  interchangeLayerJQUrl: 'http://10.166.154.217:6080/arcgis/rest/services/Facility/jq_htlj/MapServer', // 济青互通立交
  pivotLayerJQUrl: 'http://10.166.154.217:6080/arcgis/rest/services/Facility/jq_snlj/MapServer',  // 济青枢纽立交
  bridgeLayerJQUrl: 'http://10.166.154.217:6080/arcgis/rest/services/Facility/jq_ql/MapServer', // 济青桥梁
  /* 建设养护*/
  engineeringBuildUrl: "http://10.166.154.101:6080/arcgis/rest/services/Facility/GCJS/MapServer", //建设工程
  roadDamageLayerUrl: "http://10.166.154.101:6080/arcgis/rest/services/Maintain/cement_Road/MapServer",//道路损坏
  roadConsumeLayerUrl: "http://10.166.154.101:6080/arcgis/rest/services/Maintain/abrasion_Road/MapServer",//路面磨耗
  asphaltRoadDamageLayerUrl: "http://10.166.154.101:6080/arcgis/rest/services/Maintain/asphalt_Road/MapServer",//沥青路面损坏
  roadBumpLayerUrl: "http://10.166.154.101:6080/arcgis/rest/services/Maintain/bump_Road_2/MapServer",//路面跳车
  roadSkidLayerUrl: "http://10.166.154.101:6080/arcgis/rest/services/Maintain/skid_Road/MapServer",//路面抗滑
  roadSmoothLayerUrl: "http://10.166.154.101:6080/arcgis/rest/services/Maintain/smooth_Road/MapServer",//路面平整度
  roadTrackLayerUrl: "http://10.166.154.101:6080/arcgis/rest/services/Maintain/track_Road/MapServer",//路面车辙
  // roadTrackLayerUrl: "http://10.166.154.217:6080/arcgis/rest/services/Facility/laneCentor/MapServer",//路面车辙 测试token用
  emergencyLayerUrl: 'http://10.166.154.101:6080/arcgis/rest/services/Maintain/equip/MapServer',//物资装备
  trafficFlowLayerUrl: 'http://10.166.154.217:6080/arcgis/rest/services/Maintain/equip/MapServer',//交通流量
  trafficSafetyLayerUrl: 'http://10.166.154.217:6080/arcgis/rest/services/Device/JTBZ/MapServer',//交安标识
  selectDistrictLayerUrl: 'http://10.166.154.101:6080/arcgis/rest/services/BaseService/District_highlight/MapServer', //行政区划高亮
  selectDistrictBJLayerUrl: 'http://10.166.154.101:6080/arcgis/rest/services/BaseService/District_line_highlight/MapServer',
  danaoGreenLineLayerUrl: 'http://10.166.154.101:6080/arcgis/rest/services/Maintain/Green_road/MapServer'
}
export default {
  GlobalConfig,
}