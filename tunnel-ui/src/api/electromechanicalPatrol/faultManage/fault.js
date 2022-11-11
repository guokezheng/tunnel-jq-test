import request from '@/utils/request'

// 查询故障清单列表
export function listList(query) {
  return request({
    url: '/system/list/list',
    method: 'get',
    params: query
  })
}

// 查询故障清单详细
export function getList(id) {
  return request({
    url: '/system/list/' + id,
    method: 'get'
  })
}

// 新增故障清单
export function addList(data) {
  return request({
    url: '/system/list',
    method: 'post',
    data: data
  })
}

// 修改故障清单
export function updateList(data) {
  return request({
    url: '/system/list',
    method: 'put',
    data: data
  })
}

// 删除故障清单
export function delList(id) {
  return request({
    url: '/system/list/' + id,
    method: 'delete'
  })
}

// 导出故障清单
export function exportList(query) {
  return request({
    url: '/system/list/export',
    method: 'get',
    params: query
  })
}