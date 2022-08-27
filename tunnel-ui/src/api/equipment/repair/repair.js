import request from '@/utils/request'

// 查询设备巡检修列表
export function listRepair(query) {
  return request({
    url: '/system/repair/list',
    method: 'get',
    params: query
  })
}

// 查询设备巡检修详细
export function getRepair(repairId) {
  return request({
    url: '/system/repair/' + repairId,
    method: 'get'
  })
}

// 新增设备巡检修
export function addRepair(data) {
  return request({
    url: '/system/repair',
    method: 'post',
    data: data
  })
}

// 修改设备巡检修
export function updateRepair(data) {
  return request({
    url: '/system/repair',
    method: 'put',
    data: data
  })
}

// 删除设备巡检修
export function delRepair(repairId) {
  return request({
    url: '/system/repair/' + repairId,
    method: 'delete'
  })
}

// 导出设备巡检修
export function exportRepair(query) {
  return request({
    url: '/system/repair/export',
    method: 'get',
    params: query
  })
}