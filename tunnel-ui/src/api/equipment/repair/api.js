import request from '@/utils/request'

// 查询设备巡检修列表
export function listRepair(query) {
  return request({
    url: '/repair/list',
    method: 'get',
    params: query
  })
}

// 查询设备巡检修详细
export function getRepair(repairId) {
  return request({
    url: '/repair/' + repairId,
    method: 'get'
  })
}

// 新增设备巡检修
export function addRepair(data) {
  return request({
    url: '/repair',
    method: 'post',
    data: data
  })
}

// 修改设备巡检修
export function updateRepair(data) {
  return request({
    url: '/repair',
    method: 'put',
    data: data
  })
}

// 删除设备巡检修
export function delRepair(repairId) {
  return request({
    url: '/repair/' + repairId,
    method: 'delete'
  })
}
