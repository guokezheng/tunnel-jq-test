import request from '@/utils/request'

// 查询设备维修记录列表
export function listRecord(query) {
  return request({
    url: '/repairRecord/list',
    method: 'get',
    params: query
  })
}

// 查询设备维修记录详细
export function getRecord(repairId) {
  return request({
    url: '/repairRecord/' + repairId,
    method: 'get'
  })
}

// 新增设备维修记录
export function addRecord(data) {
  return request({
    url: '/repairRecord',
    method: 'post',
    data: data
  })
}

// 修改设备维修记录
export function updateRecord(data) {
  return request({
    url: '/repairRecord',
    method: 'put',
    data: data
  })
}

// 删除设备维修记录
export function delRecord(repairId) {
  return request({
    url: '/repairRecord/' + repairId,
    method: 'delete'
  })
}

export function exportRecord(query) {
  return request({
    url: '/system/record/export',
    method: 'get',
    params: query
  })
}

export function exportRepairRecord(query) {
  return request({
    url: '/repairRecord/export',
    method: 'get',
    params: query
  })
}
