import request from '@/utils/request'

// 查询预警信息列表
export function listWarningInfo(query) {
  return request({
    url: '/warningInfo/list',
    method: 'get',
    params: query
  })
}
export function listWarning(query) {
  return request({
    url: '/warningInfo/warningList',
    method: 'get',
    params: query
  })
}
// 查询预警信息列表
export function fireWeekList(query) {
  return request({
    url: '/warningInfo/fireWeekList',
    method: 'get'

  })
}

// 查询预警信息详细
export function getWarningInfo(id) {
  return request({
    url: '/warningInfo/' + id,
    method: 'get'
  })
}

// 查询策略信息列表
export function getStrategyList(id) {
  return request({
    url: '/warningInfo/getStrategyList/' + id,
    method: 'get'
  })
}
// 查询应急物资信息列表
export function emdeviceList(id) {
  return request({
    url: '/warningInfo/emdeviceList/' + id,
    method: 'get'
  })
}
// 查询应急人员信息列表
export function emergencyPerList(id) {
  return request({
    url: '/warningInfo/emergencyPerList/' + id,
    method: 'get'
  })
}
// 查询忽略告警信息
export function getIgnoreEvent(id) {
  return request({
    url: '/warningInfo/getIgnoreEvent/' + id,
    method: 'get'
  })
}
// 一键忽略告警信息
export function oneKeyIgnore() {
  return request({
    url: '/warningInfo/oneIgnore',
    method: 'get'
  })
}
// 查询环境参数信息列表
export function getDeviceInfo(tunnelId,holeDirection) {
  return request({
    url: '/warningInfo/deviceinfo?tunnelId=' + tunnelId+'&direction='+holeDirection,
    method: 'get'
  })
}
// 查询交通信息列表
export function getTrafficInfo(tunnelId,holeDirection) {
  return request({
    url: '/warningInfo/trafficinfo?tunnelId=' + tunnelId+'&direction='+holeDirection,
    method: 'get'
  })
}
export function fireAlarmCount(query) {
  return request({
    url: '/warningInfo/fireAlarmCount',
    method: 'get'
  })
}


// 新增预警信息
export function addWarningInfo(data) {
  return request({
    url: '/warningInfo',
    method: 'post',
    data: data
  })
}

// 修改预警信息
export function updateWarningInfo(data) {
  return request({
    url: '/warningInfo',
    method: 'put',
    data: data
  })
}

// 删除预警信息
export function delWarningInfo(id) {
  return request({
    url: '/warningInfo/' + id,
    method: 'delete'
  })
}

 // 手动控制策略
export function handleStrategy(ids,warId) {
  return request({
    url: '/warningInfo/handleStrategy/?ids=' + ids +'&warId='+warId,
    method: 'get'
  })
}
export function handleStrategyRollBack(warId) {
  return request({
    url: '/warningInfo/handleStrategyRollBack/?warId='+warId,
    method: 'get'
  })
}
export function ioToBase64(url) {
  return request({
    url: '/warningInfo/ioToBase64/?url=' + url,
    method: 'get'
  })
}
// 事件预警统计
export function getWarningInfoCount() {
  return request({
    url: '/warningInfo/getWarningInfoCount',
    method: 'get'
  })
}
