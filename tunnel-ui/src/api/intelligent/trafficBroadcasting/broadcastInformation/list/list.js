import request from '@/utils/request'

// 查询广播信息列列表
export function listList(query) {
  return request({
    url: '/broadcastInformation/list/list',
    method: 'get',
    params: query
  })
}

// 查询广播信息列详细
export function getList(id) {
  return request({
    url: '/broadcastInformation/list/' + id,
    method: 'get'
  })
}

// 新增广播信息列
export function addList(data) {
  return request({
    url: '/broadcastInformation/list',
    method: 'post',
    data: data
  })
}

// 修改广播信息列
export function updateList(data) {
  return request({
    url: '/broadcastInformation/list',
    method: 'put',
    data: data
  })
}

// 删除广播信息列
export function delList(id) {
  return request({
    url: '/broadcastInformation/list/' + id,
    method: 'delete'
  })
}

// 导出广播信息列
export function exportList(query) {
  return request({
    url: '/broadcastInformation/list/export',
    method: 'get',
    params: query
  })
}
