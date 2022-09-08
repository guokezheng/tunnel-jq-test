import request from '@/utils/request'

// 查询控制策略列表
export function listStrategy(query) {
  return request({
    url: '/strategy/list',
    method: 'get',
    params: query
  })
}

// 查询控制策略详细
export function getStrategy(id) {
  return request({
    url: '/strategy/' + id,
    method: 'get'
  })
}

// 新增控制策略
export function addStrategy(data) {
  return request({
    url: '/strategy',
    method: 'post',
    data: data
  })
}

// 修改控制策略
export function updateStrategy(data) {
  return request({
    url: '/strategy',
    method: 'put',
    data: data
  })
}

// 删除控制策略
export function delStrategy(id) {
  return request({
    url: '/strategy/' + id,
    method: 'delete'
  })
}
// 新增控制策略
export function addStrategysInfo(data) {
  return request({
    url: '/strategy/addStrategysInfo',
    method: 'post',
    data: data
  })
}
// 修改控制策略
export function updateStrategysInfo(data) {
  return request({
    url: '/strategy/updateStrategysInfo',
    method: 'post',
    data: data
  })
}

// 获取guid
export function getGuid() {
  return request({
    url: '/strategy/getGuid',
    method: 'get'
  })
}

// 手动控制策略
export function handleStrategy(id) {
  return request({
    url: '/strategy/handleStrategy/' + id,
    method: 'get'
  })
}

// 返回控制策略的设备和状态
export function getRl(id) {
  return request({
    url: '/strategyRl/getStrategyRlByStrategyId/' + id,
    method: 'get'
  })
}
