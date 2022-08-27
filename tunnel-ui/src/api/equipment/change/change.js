import request from '@/utils/request'

// 查询设备变更列表
export function listChange(query) {
  return request({
    url: '/system/change/list',
    method: 'get',
    params: query
  })
}

// 查询设备变更详细
export function getChange(id) {
  return request({
    url: '/system/change/' + id,
    method: 'get'
  })
}

// 新增设备变更
export function addChange(data) {
  return request({
    url: '/system/change',
    method: 'post',
    data: data
  })
}

// 修改设备变更
export function updateChange(data) {
  return request({
    url: '/system/change',
    method: 'put',
    data: data
  })
}

// 删除设备变更
export function delChange(id) {
  return request({
    url: '/system/change/' + id,
    method: 'delete'
  })
}

// 导出设备变更
export function exportChange(query) {
  return request({
    url: '/system/change/export',
    method: 'get',
    params: query
  })
}