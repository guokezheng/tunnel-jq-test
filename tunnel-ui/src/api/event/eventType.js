import request from '@/utils/request'

// 查询事件类型列表
export function listEventType(query) {
  return request({
    url: '/eventType/list',
    method: 'get',
    params: query
  })
}

// 查询事件类型详细
export function getEventType(id) {
  return request({
    url: '/eventType/' + id,
    method: 'get'
  })
}

// 新增事件类型
export function addEventType(data) {
  return request({
    url: '/eventType',
    method: 'post',
    data: data
  })
}

// 修改事件类型
export function updateEventType(data) {
  return request({
    url: '/eventType',
    method: 'put',
    data: data
  })
}

// 删除事件类型
export function delEventType(id) {
  return request({
    url: '/eventType/' + id,
    method: 'delete'
  })
}