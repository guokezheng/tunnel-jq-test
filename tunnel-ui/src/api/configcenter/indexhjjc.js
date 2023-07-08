import request from '@/utils/request'

// 查询列表
export function listHumtre(query) {
  return request({
    url: '/environmentalMonitoring/homePage/list',
    method: 'get',
    params: query
  })
}
//查询图表
export function listEcharts(query) {
  return request({
    url: '/environmentalMonitoring/homePage/echarts',
    method: 'get',
    params: query
  })
}
//查询实时温度
export function realTimeTemperature(query) {
  return request({
    url: '/environmentalMonitoring/homePage/echartsHome',
    method: 'get',
    params: query
  })
}

// 根据场站编号及设备编号实时数据
export function getRealDataByCode(query) {
  return request({
    url: '/environmentalMonitoring/homePage/getRealDataByCode/'+query.powerCode+'/'+query.deviceCode,
    method: 'get'
  })
}

//查询环境监测设备状态
export function environmentalMonitoringStatus(query) {
  return request({
    url: '/environmentalMonitoring/homePage/selectEnvironmentalMonitoringStatus',
    method: 'get',
    params: query
  })
}
//查询环境设备状态
export function selectEquipmentStatus(query) {
  return request({
    url: '/environmentalMonitoring/homePage/selectEquipmentStatus',
    method: 'get',
    params: query
  })
}
//更多页下拉树
export function treeselect(query) {
  return request({
    url: '/environmentalMonitoring/homePage/equipmentTreeselect',
    method: 'get',
    params: query
  })
}
//设备下拉
export function equipmentSelect(query) {
  return request({
    url: '/environmentalMonitoring/homePage/equipmentSelect',
    method: 'get',
    params: query
  })
}
