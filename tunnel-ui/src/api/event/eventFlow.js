import request from '@/utils/request'

// 查询事件处理流程列表
export function listEventFlow(query) {
  return request({
    url: '/eventFlow/list',
    method: 'get',
    params: query
  })
}

// 查询事件处理流程详细
export function getEventFlow(id) {
  return request({
    url: '/eventFlow/' + id,
    method: 'get'
  })
}

// 新增事件处理流程
export function addEventFlow(data) {
  return request({
    url: '/eventFlow',
    method: 'post',
    data: data
  })
}

// 修改事件处理流程
export function updateEventFlow(data) {
  return request({
    url: '/eventFlow',
    method: 'put',
    data: data
  })
}

// 删除事件处理流程
export function delEventFlow(id) {
  return request({
    url: '/eventFlow/' + id,
    method: 'delete'
  })
}