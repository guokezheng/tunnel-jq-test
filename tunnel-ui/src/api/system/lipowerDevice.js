import request from '@/utils/request'

// 查询照明设备信息列表
export function listLipowerDevice(query) {
  return request({
    url: '/lipowerDevice/list',
    method: 'get',
    params: query
  })
}

// 查询照明设备信息列表
export function getLiPowerDevices() {
  return request({
    url: '/lipowerDevice/getLiPowerDevice',
    method: 'get'
  })
}
export function initLipowerDevice() {
  return request({
    url: '/lipowerDevice/initLipowerDevice',
    method: 'get'
  })
}

// 查询照明设备信息详细
export function getLiPowerDevice(id) {
  return request({
    url: '/lipowerDevice/' + id,
    method: 'get'
  })
}

// 新增照明设备信息
export function addLipowerDevice(data) {
  return request({
    url: '/lipowerDevice',
    method: 'post',
    data: data
  })
}

// 修改照明设备信息
export function updateLipowerDevice(data) {
  return request({
    url: '/lipowerDevice',
    method: 'put',
    data: data
  })
}

// 删除照明设备信息
export function delLipowerDevice(id) {
  return request({
    url: '/lipowerDevice/' + id,
    method: 'delete'
  })
}
