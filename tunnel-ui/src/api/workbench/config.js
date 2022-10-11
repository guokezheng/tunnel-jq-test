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

// 车指批量控制抽屉
export function batchControlCarFinger(data) {
  return request({
    url: '/workspace/batchControlCarFinger',
    method: 'post',
    data: data
  })
}