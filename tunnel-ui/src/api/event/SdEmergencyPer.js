import request from '@/utils/request'

// 查询应急人员信息列表
export function listSdEmergencyPer(query) {
  return request({
    url: '/SdEmergencyPer/list',
    method: 'get',
    params: query
  })
}

// 查询应急人员信息详细
export function getSdEmergencyPer(id) {
  return request({
    url: '/SdEmergencyPer/' + id,
    method: 'get'
  })
}

// 新增应急人员信息
export function addSdEmergencyPer(data) {
  return request({
    url: '/SdEmergencyPer',
    method: 'post',
    data: data
  })
}

// 修改应急人员信息
export function updateSdEmergencyPer(data) {
  return request({
    url: '/SdEmergencyPer',
    method: 'put',
    data: data
  })
}

// 删除应急人员信息
export function delSdEmergencyPer(id) {
  return request({
    url: '/SdEmergencyPer/' + id,
    method: 'delete'
  })
}

// 导出应急人员信息
export function exportSdEmergencyPer(query) {
  return request({
    url: '/SdEmergencyPer/export',
    method: 'get',
    params: query
  })
}