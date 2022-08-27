import request from '@/utils/request'

// 查询路面监测信息列表
export function listRoadState(query) {
  return request({
    url: '/business/roadState/list',
    method: 'get',
    params: query
  })
}

// 查询路面监测信息详细
export function getRoadState(id) {
  return request({
    url: '/business/roadState/' + id,
    method: 'get'
  })
}

// 新增路面监测信息
export function addRoadState(data) {
  return request({
    url: '/business/roadState',
    method: 'post',
    data: data
  })
}

// 修改路面监测信息
export function updateRoadState(data) {
  return request({
    url: '/business/roadState',
    method: 'put',
    data: data
  })
}

// 删除路面监测信息
export function delRoadState(id) {
  return request({
    url: '/business/roadState/' + id,
    method: 'delete'
  })
}

// 导出路面监测信息
export function exportRoadState(query) {
  return request({
    url: '/business/roadState/export',
    method: 'get',
    params: query
  })
}

// 获取路面监测统计数据
export function statisticsRoadMonitoring() {
  return request({
    url: '/business/roadState/statisticsRoadMonitoring',
    method: 'get'
  })
}

// 获取路面监测统计数据
export function statisticsWarningTrend(query) {
  return request({
    url: '/business/roadState/statisticsWarningTrend',
    method: 'get',
    params: query
  })
}

// 查询路面监测统计数据
export function statisticsWarningTrendSearch(query) {
  return request({
    url: '/business/roadState/statisticsWarningTrendSearch',
    method: 'get',
    params: query
  })
}

// 根据隧道和年、月、日查询路面温度、路面温度预警信息、积水厚度和积雪厚度
export function statisticsSearch(query) {
  return request({
    url: '/business/roadState/statisticsSearch',
    method: 'get',
    params: query
  })
}
// 根据隧道和年、月、日查询路预警次数
export function statisticsSearcWarningInfo(query) {
  return request({
    url: '/business/roadState/statisticsSearcWarningInfo',
    method: 'get',
    params: query
  })
}

// 根据隧道和年、月、日查询路面状态
export function statisticsSearchPavementstatus(query) {
  return request({
    url: '/business/roadState/statisticsSearchPavementstatus',
    method: 'get',
    params: query
  })
}

// 获取当年度最高路面温度
export function statisticsSearcMaxPavementtemp(query) {
  return request({
    url: '/business/roadState/statisticsSearcMaxPavementtemp',
    method: 'get',
    params: query
  })
}

// 获取当年度最大预警吃书
export function statisticsSearcMaxpWarningInfo(query) {
  return request({
    url: '/business/roadState/statisticsSearcMaxpWarningInfo',
    method: 'get',
    params: query
  })
}

// 根据设备编号查询最新数据信息
export function selectByEqDeno(query) {
  return request({
    url: '/business/roadState/selectByEqDeno',
    method: 'get',
    params: query
  })
}
