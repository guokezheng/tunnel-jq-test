import request from '@/utils/request'

// 查询交通事件列表
export function listOperating(query) {
  return request({
    url: '/system/operating/list',
    method: 'get',
    params: query
  })
}

// 查询交通事件详细
export function getOperating(id) {
  return request({
    url: '/system/operating/' + id,
    method: 'get'
  })
}

// 新增交通事件
export function addOperating(data) {
  return request({
    url: '/system/operating',
    method: 'post',
    data: data
  })
}

// 修改交通事件
export function updateOperating(data) {
  return request({
    url: '/system/operating',
    method: 'put',
    data: data
  })
}

// 删除交通事件
export function delOperating(id) {
  return request({
    url: '/system/operating/' + id,
    method: 'delete'
  })
}

// 导出交通事件
export function exportOperating(query) {
  return request({
    url: '/system/operating/export',
    method: 'get',
    params: query
  })
}