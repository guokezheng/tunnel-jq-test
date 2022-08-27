import request from '@/utils/request'

// 查询应急处置列表
export function listResponse(query) {
  return request({
    url: '/response/list',
    method: 'get',
    params: query
  })
}

// 查询应急处置详细
export function getResponse(responseId) {
  return request({
    url: '/response/' + responseId,
    method: 'get'
  })
}

// 新增应急处置
export function addResponse(data) {
  return request({
    url: '/response',
    method: 'post',
    data: data
  })
}

// 修改应急处置
export function updateResponse(data) {
  return request({
    url: '/response',
    method: 'put',
    data: data
  })
}

// 删除应急处置
export function delResponse(responseId) {
  return request({
    url: '/response/' + responseId,
    method: 'delete'
  })
}