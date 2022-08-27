import request from '@/utils/request'

// 查询设备运行记录列表
export function listRecord(query) {
  return request({
    url: '/system/equipmentOperationRecord/list',
    method: 'get',
    params: query
  })
}

// 查询设备运行记录详细
export function getRecord(id) {
  return request({
    url: '/system/equipmentOperationRecord/' + id,
    method: 'get'
  })
}

// 新增设备运行记录
export function addRecord(data) {
  return request({
    url: '/system/equipmentOperationRecord',
    method: 'post',
    data: data
  })
}

// 修改设备运行记录
export function updateRecord(data) {
  return request({
    url: '/system/equipmentOperationRecord',
    method: 'put',
    data: data
  })
}

// 删除设备运行记录
export function delRecord(id) {
  return request({
    url: '/system/equipmentOperationRecord/' + id,
    method: 'delete'
  })
}

// 导出设备运行记录
export function exportRecord(query) {
  return request({
    url: '/system/equipmentOperationRecord/export',
    method: 'get',
    params: query
  })
}
