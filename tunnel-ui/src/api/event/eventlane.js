import request from '@/utils/request'

// 查询车道信息列表
export function listEventlane(query) {
  return request({
    url: '/eventlane/list',
    method: 'get',
    params: query
  })
}

// 查询车道信息详细
export function getEventlane(id) {
  return request({
    url: '/eventlane/' + id,
    method: 'get'
  })
}
// 根据任务查询车道信息详细
export function getEventlaneByTaskId(id) {
  return request({
    url: '/eventlane/getEventlaneByTaskId/' + id,
    method: 'get'
  })
}
// 新增车道信息
export function addEventlane(data) {
  return request({
    url: '/eventlane',
    method: 'post',
    data: data
  })
}

// 修改车道信息
export function updateEventlane(data) {
  return request({
    url: '/eventlane',
    method: 'put',
    data: data
  })
}

// 删除车道信息
export function delEventlane(id) {
  return request({
    url: '/eventlane/' + id,
    method: 'delete'
  })
}

// 导出车道信息
export function exportEventlane(query) {
  return request({
    url: '/eventlane/export',
    method: 'get',
    params: query
  })
}
