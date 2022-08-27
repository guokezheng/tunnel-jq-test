import request from '@/utils/request'

// 查询事件监测记录列表
export function listDetection(query) {
  return request({
    url: '/system/detection/list',
    method: 'get',
    params: query
  })
}

// 查询事件监测记录详细
export function getDetection(id) {
  return request({
    url: '/system/detection/' + id,
    method: 'get'
  })
}

// 新增事件监测记录
export function addDetection(data) {
  return request({
    url: '/system/detection',
    method: 'post',
    data: data
  })
}

//加载图片
export function loadPicture(data) {
  return request({
    url: '/system/detection/loadPicture',
    method: 'post',
    params: data
  })
}

// 修改事件监测记录
export function updateDetection(data) {
  return request({
    url: '/system/detection',
    method: 'put',
    data: data
  })
}

// 删除事件监测记录
export function delDetection(id) {
  return request({
    url: '/system/detection/' + id,
    method: 'delete'
  })
}

// 导出事件监测记录
export function exportDetection(query) {
  return request({
    url: '/system/detection/export',
    method: 'get',
    params: query
  })
}
