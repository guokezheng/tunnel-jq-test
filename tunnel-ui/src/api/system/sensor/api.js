import request from '@/utils/request'

// 查询传感信息列表
export function listSensorMessage(query) {
  return request({
    url: '/message/list',
    method: 'get',
    params: query
  })
}

// 查询传感信息详细
export function getSensorMessage(sensorMessageId) {
  return request({
    url: '/message/' + sensorMessageId,
    method: 'get'
  })
}

//查询传感信息详细
export function getSensor(query) {
  return request({
    url: '/message/select',
    method: 'get',
    params: query
  })
}

// 新增车流量信息
export function addSensorMessagge(data) {
  return request({
    url: '/message',
    method: 'post',
    data: data
  })
}

// 修改车流量信息
export function updateSensorMessage(data) {
  return request({
    url: '/message/update',
    method: 'put',
    data: data
  })
}

// 删除车流量信息
export function delSensorMessage(sensorMessageId) {
  return request({
    url: '/message/del/' + sensorMessageId,
    method: 'delete'
  })
}
// 查询环境参数信息列表
export function getDeviceInfo(tunnelId) {
  return request({
    url: '/message/eqs?tunnelId=' + tunnelId,
    method: 'get'
  })
}



