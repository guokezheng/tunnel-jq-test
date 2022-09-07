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
export function vehicleMonitoring(query) {
  return request({
    url: '/radar/data/byId',
    method: 'get',
    params: query
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
