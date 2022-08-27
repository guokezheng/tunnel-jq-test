import request from '@/utils/request'

// 查询应急物资信息列表
export function listSdEmergencyDevice(query) {
  return request({
    url: '/sdEmergencyDevice/list',
    method: 'get',
    params: query
  })
}

// 查询应急物资信息详细
export function getSdEmergencyDevice(id) {
  return request({
    url: '/sdEmergencyDevice/' + id,
    method: 'get'
  })
}

// 新增应急物资信息
export function addSdEmergencyDevice(data) {
  return request({
    url: '/sdEmergencyDevice',
    method: 'post',
    data: data
  })
}

// 修改应急物资信息
export function updateSdEmergencyDevice(data) {
  return request({
    url: '/sdEmergencyDevice',
    method: 'put',
    data: data
  })
}

// 删除应急物资信息
export function delSdEmergencyDevice(id) {
  return request({
    url: '/sdEmergencyDevice/' + id,
    method: 'delete'
  })
}

// 导出应急物资信息
export function exportSdEmergencyDevice(query) {
  return request({
    url: '/sdEmergencyDevice/export',
    method: 'get',
    params: query
  })
}