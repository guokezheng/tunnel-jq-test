import request from '@/utils/request'

// 查询紧急电话推送记录列表
export function listPushCall(query) {
  return request({
    url: '/pushCall/list',
    method: 'get',
    params: query
  })
}

// 查询紧急电话推送记录详细
export function getPushCall(id) {
  return request({
    url: '/pushCall/' + id,
    method: 'get'
  })
}

// 新增紧急电话推送记录
export function addPushCall(data) {
  return request({
    url: '/pushCall',
    method: 'post',
    data: data
  })
}

// 修改紧急电话推送记录
export function updatePushCall(data) {
  return request({
    url: '/pushCall',
    method: 'put',
    data: data
  })
}

// 删除紧急电话推送记录
export function delPushCall(id) {
  return request({
    url: '/pushCall/' + id,
    method: 'delete'
  })
}
