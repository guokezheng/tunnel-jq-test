import request from '@/utils/request'

// 查询摄像头配置列表
export function listCamera(query) {
  return request({
    url: '/configcenter/camera/list',
    method: 'get',
    params: query
  })
}

// 查询摄像头配置详细
export function getCamera(id) {
  return request({
    url: '/configcenter/camera/' + id,
    method: 'get'
  })
}

// 新增摄像头配置
export function addCamera(data) {
  return request({
    url: '/configcenter/camera',
    method: 'post',
    data: data
  })
}

// 修改摄像头配置
export function updateCamera(data) {
  return request({
    url: '/configcenter/camera',
    method: 'put',
    data: data
  })
}

// 删除摄像头配置
export function delCamera(id) {
  return request({
    url: '/configcenter/camera/' + id,
    method: 'delete'
  })
}

// 导出摄像头配置
export function exportCamera(query) {
  return request({
    url: '/configcenter/camera/export',
    method: 'get',
    params: query
  })
}

// 查询摄像头选项
export function dropDown(query) {
  return request({
    url: '/configcenter/camera/dropDown',
    method: 'get',
    params: query
  })
}

// 查询回放流
export function getBackData(query) {
  return request({
    url: '/configcenter/camera/getBackData',
    method: 'get',
    params: query
  })
}
// 查询预览流
export function getStream(query) {
  return request({
    url: '/configcenter/camera/getStream',
    method: 'get',
    params: query
  })
}

// 查询预览流
export function getStreamList(query) {
  return request({
    url: '/configcenter/camera/getStreamList',
    method: 'get',
    params: query
  })
}

