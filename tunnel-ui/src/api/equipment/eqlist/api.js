import request from '@/utils/request'

// 查询设备列表
export function listDevices(query) {
  return request({
    url: '/devices/list',
    method: 'get',
    params: query
  })
}
// 查询传感器设备列表
export function sensorListDevices(query) {
  return request({
    url: '/devices/sensorList',
    method: 'get',
    params: query
  })
}

// 查询设备详细
export function getDevices(eqId) {
  return request({
    url: '/devices/' + eqId,
    method: 'get'
  })
}

//通过隧道查询设备类型信息
export function getEquipmentTypeName(tunnelId){
  return request({
    url: '/devices/' + tunnelId,
    method: 'get'
  })
}

// 新增设备
export function addDevices(data) {
  return request({
    url: '/devices',
    method: 'post',
    data: data
  })
}

// 修改设备
export function updateDevices(data) {
  return request({
    url: '/devices',
    method: 'put',
    data: data
  })
}

// 删除设备
export function delDevices(eqId) {
  return request({
    url: '/devices/' + eqId,
    method: 'delete'
  })
}

export function exportDevices(query) {
  return request({
    url: '/devices/export',
    method: 'post',
    params: query
  })
}

// 生成控制指令
export function createDmcontrolSeat(data) {
  return request({
    url: '/devices/createDmcontrolSeat',
    method: 'post',
    data: data
  })
}
// 生成查询指令
export function createInstruction(data) {
  return request({
    url: '/devices/createInstruction',
    method: 'post',
    data: data
  })
}
// 生成设备ID
export function autoId(data) {
  return request({
    url: '/devices/autoId?tunnelId='+ data.tunnelId + '&typeId=' + data.typeId,
    method: 'post',
  })
}

export function checkInstruction(query) {
  return request({
    url: '/devices/checkInstruction',
    method: 'get',
    params: query
  })
}

export function exportDevicesTemplate(query) {
  return request({
    url: '/devices/importTemplate',
    method: 'post',
    params: query
  })
}
