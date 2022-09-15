import request from '@/utils/request'

// 查询事件管理列表
export function listEvent(query) {
  return request({
    url: '/event/list',
    method: 'get',
    params: query
  })
}

// 查询事件管理详细
export function getEvent(id) {
  return request({
    url: '/event/' + id,
    method: 'get'
  })
}

// 新增事件管理
export function addEvent(data) {
  return request({
    url: '/event',
    method: 'post',
    data: data
  })
}

// 修改事件管理
export function updateEvent(data) {
  return request({
    url: '/event',
    method: 'put',
    data: data
  })
}

// 删除事件管理
export function delEvent(id) {
  return request({
    url: '/event/' + id,
    method: 'delete'
  })
}

// 管理机构
export function toll() {
  return request({
    url: '/system/dept/toll',
    method: 'get'
  })
}
// 所属隧道
export function getTunnelList(query) {
  return request({
    url: '/tunnels/deptId',
    method: 'get',
    params: query
  })
}

// 查询方向
export function listDevices(query) {
  return request({
    url: '/devices/list',
    method: 'get',
    params: query
  })
}