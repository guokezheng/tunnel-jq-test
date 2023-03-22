import request from '@/utils/request'


export function getSystemWarningMsg() {
  return request({
    url: '/warningInfo/getSystemWarningMsg',
    method: 'get',
  })
}

export function getTrafficIncident() {
  return request({
    url: '/warningInfo/getTrafficIncident',
    method: 'get',
  })
}

// 设备类型占比
export function proportionOfEquipment(query) {
  return request({
    url: '/proportionOfEquipment/list',
    method: 'get',
    params: query
  })
}
// 车辆监测数据
export function vehicleMonitoringInRecent24Hours(data) {
  return request({
    url: '/workspace/vehicleMonitoringInRecent24Hours',
    method: 'post',
    data: data
  })
}


// 重点车辆监测数据
export function specialVehicleMonitoringInRecent24Hours(data) {
  return request({
    url: '/workspace/specialVehicleMonitoringInRecent24Hours',
    method: 'post',
    data: data
  })
}
// 重点车辆监测数据
export function special(query) {
  return request({
    url: '/special/vehicle/specialById',
    method: 'get',
    params: query
  })
}
// 获取情报板基础信息
export function getDeviceBase(deviceId) {
  return request({
    url: '/parser/board/getDeviceBase?deviceId=' + deviceId,
    method: 'get'
  })
}

// 获取情报板实时内容
export function getNewBoardEditInfo(deviceId) {
  return request({
    url: '/parser/board/getNewBoardEditInfo?deviceId=' + deviceId,
    method: 'get'
  })
}

// 模板列表查询
export function templateList(screenSize){
  return request({
    url:'/system/template/list?screenSize=' + screenSize,
    method: 'get'
  })
}

export function vocabularyList(word){
  return request({
    url:'/system/vocabulary/List',
    method: 'get',
    params: word
  })
}

// 模板发布
export function uploadBoardEditInfo(data){
  return request({
    url:'/parser/board/uploadBoardEditInfo',
    method: 'get',
    params: data
  })
}
// 交通流状况
export function trafficFlowInformation() {
  return request({
    url: '/trafficFlowData/hour/obtainNowTrafficFlowInformation',
    method: 'post',
  })
}

// covi弹窗数据
export function getTodayCOVIData(deviceId){
  return request({
    url:'/system/data/getTodayCOVIData/' + deviceId,
    method: 'get',
  })
}

// 风速风向弹窗数据
export function getTodayFSFXData(deviceId){
  return request({
    url:'/system/data/getTodayFSFXData/' + deviceId,
    method: 'get',
  })
}

// 亮度检测器弹窗数据
export function getTodayLDData(deviceId){
  return request({
    url:'/system/data/getTodayLDData/' + deviceId,
    method: 'get',
  })
}

//根据隧道id查询当前设备的监测状态、实时数据或状态
export function getDeviceData(query) {
  return request({
    url: '/system/data/getDeviceDataByTunnelId',
    method: 'get',
    params: query
  })
}

// 车指弹窗数据
export function controlDevice(data) {
  return request({
    url: '/workspace/controlDevice',
    method: 'post',
    data: data
  })
}

// 诱导灯弹窗数据
export function controlGuidanceLampDevice(data) {
  return request({
    url: '/workspace/controlGuidanceLampDevice',
    method: 'post',
    data: data
  })
}

// 疏散标志弹窗数据
export function controlEvacuationSignDevice(data) {
  return request({
    url: '/workspace/controlEvacuationSignDevice',
    method: 'post',
    data: data
  })
}

// 车指批量控制抽屉
export function batchControlCarFinger(data) {
  return request({
    url: '/workspace/batchControlCarFinger',
    method: 'post',
    data: data
  })
}

// 定时控制抽屉 获取数据
export function timeSharing(tunnelId) {
  return request({
    url: '/strategy/timeSharing/' + tunnelId,
    method: 'get',
  })
}

// 定时控制抽屉 修改控制状态
export function updateControlTime(strategyId,controlTime) {
  return request({
    url: '/strategy/timeSharing/updateControlTime?strategyId=' + strategyId + '&controlTime=' + controlTime,
    method: 'get',
  })
}

// 定时控制抽屉 控制开关
export function timeStrategySwitch(strategyId,change) {
  return request({
    url: '/strategy/switch?strategyId='+strategyId + '&change=' + change,
    method: 'get',
  })
}

// 批量控制弹窗
export function batchControlDevice(data) {
  return request({
    url: '/workspace/batchControlDevice',
    method: 'post',
    data: data
  })
}

// 情报板显示内容定时查询
export function addBoardContent(tunnelId) {
  return request({
    url: 'parser/board/getWorkBenchBoardContent/'+tunnelId,
    method: 'get',
  })
}

// 查询风机安全检测仪实时数据
export function getFanSafeData(tunnelId) {
  return request({
    url: '/system/data/getFanSafeData/'+tunnelId,
    method: 'get',
  })
}

// 微波车检表格
export function getStatisticsNewList(query) {
  return request({
    url: '/microwave/statistics/getStatisticsNewList',
    method: 'get',
    params: query
  })
}

// 微波车检echarts
export function getStatisticsRealList(query) {
  return request({
    url: '/microwave/statistics/getStatisticsRealList',
    method: 'get',
    params: query
  })
}

// 云台控制
export function PTZContro(eqId,cmdType) {
  return request({
    url: '/integrated/video/PTZControl?eqId='+ eqId + '&cmdType=' + cmdType + '&speed=1',
    method: 'get'
  })
}

// 指示灯带
export function controlWarningLightStripDevice(data) {
  return request({
    url: '/workspace/controlWarningLightStripDevice',
    method: 'post',
    data: data
  })
}

// 工作台树状搜索
export function getCategoryTree(tunnelId) {
  return request({
    url: '/bigType/getCategoryDeviceTree?tunnelId=' + tunnelId,
    method: 'get'
  })
}



//欧姆龙控制状态（测试接口  后期变为设备控制 实际接口）
export function setControlDeviceByParam(data) {
  return request({
    url: '/workspace/controlDeviceByParam',
    method: 'post',
    data: data
  })
}
