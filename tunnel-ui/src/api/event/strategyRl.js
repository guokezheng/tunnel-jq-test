import request from '@/utils/request'

// 查询策略关联设备信息列表
export function listRl(query) {
  return request({
    url: '/strategyRl/list',
    method: 'get',
    params: query
  })
}

// 查询策略关联设备信息详细
export function getRl(id) {
  return request({
    url: '/strategyRl/' + id,
    method: 'get'
  })
}

// 新增策略关联设备信息
export function addRl(data) {
  return request({
    url: '/strategyRl',
    method: 'post',
    data: data
  })
}

// 修改策略关联设备信息
export function updateRl(data) {
  return request({
    url: '/strategyRl',
    method: 'put',
    data: data
  })
}

// 删除策略关联设备信息
export function delRl(id) {
  return request({
    url: '/strategyRl/' + id,
    method: 'delete'
  })
}