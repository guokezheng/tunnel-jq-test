import request from '@/utils/request'

// 查询交通事件列表
export function listTrafficevent(query) {
  return request({
    url: '/trafficevent/list',
    method: 'get',
    params: query
  })
}

// 查询交通事件详细
export function getTrafficevent(id) {
  return request({
    url: '/trafficevent/' + id,
    method: 'get'
  })
}
// 查看视频
export function showVideoInfo(id) {
  return request({
    url: '/trafficevent/video/' + id,
    method: 'get'
  })
}

// 获取视频
export function getVideoInfo(id) {
  return request({
    url: '/trafficevent/getVideo/' + id,
    method: 'get'
  })
}

// 新增交通事件
export function addTrafficevent(data) {
  return request({
    url: '/trafficevent',
    method: 'post',
    data: data
  })
}

// 修改交通事件
export function updateTrafficevent(data) {
  return request({
    url: '/trafficevent',
    method: 'put',
    data: data
  })
}

// 删除交通事件
export function delTrafficevent(id) {
  return request({
    url: '/trafficevent/' + id,
    method: 'delete'
  })
}
