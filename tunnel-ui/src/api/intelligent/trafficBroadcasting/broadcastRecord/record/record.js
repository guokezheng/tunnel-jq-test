import request from '@/utils/request'

// 查询广播记录列表
export function listRecord(query) {
  return request({
    url: '/broadcastRecord/record/list',
    method: 'get',
    params: query
  })
}

// 查询广播记录详细
export function getRecord(id) {
  return request({
    url: '/broadcastRecord/record/' + id,
    method: 'get'
  })
}

// 新增广播记录
export function addRecord(data) {
  return request({
    url: '/broadcastRecord/record',
    method: 'post',
    data: data
  })
}

// 修改广播记录
export function updateRecord(data) {
  return request({
    url: '/broadcastRecord/record',
    method: 'put',
    data: data
  })
}

// 删除广播记录
export function delRecord(id) {
  return request({
    url: '/broadcastRecord/record/' + id,
    method: 'delete'
  })
}

// 导出广播记录
export function exportRecord(query) {
  return request({
    url: '/broadcastRecord/record/export',
    method: 'get',
    params: query
  })
}
