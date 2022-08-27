import request from '@/utils/request'

// 查询预案类型列表
export function listPlanType(query) {
  return request({
    url: '/planType/list',
    method: 'get',
    params: query
  })
}

// 查询预案类型详细
export function getPlanType(id) {
  return request({
    url: '/planType/' + id,
    method: 'get'
  })
}

// 新增预案类型
export function addPlanType(data) {
  return request({
    url: '/planType',
    method: 'post',
    data: data
  })
}

// 修改预案类型
export function updatePlanType(data) {
  return request({
    url: '/planType',
    method: 'put',
    data: data
  })
}

// 删除预案类型
export function delPlanType(id) {
  return request({
    url: '/planType/' + id,
    method: 'delete'
  })
}
