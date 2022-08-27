import request from '@/utils/request'

// 查询通知管理列表
export function listTrafficManagementUserMessage(query) {
  return request({
    url: '/business/trafficManagementUserMessage/list',
    method: 'get',
    params: query
  })
}

// 查询通知管理详细
export function getTrafficManagementUserMessage(id) {
  return request({
    url: '/business/trafficManagementUserMessage/' + id,
    method: 'get'
  })
}

// 新增通知管理
export function addTrafficManagementUserMessage(data) {
  return request({
    url: '/business/trafficManagementUserMessage',
    method: 'post',
    data: data
  })
}

// 修改通知管理
export function updateTrafficManagementUserMessage(data) {
  return request({
    url: '/business/trafficManagementUserMessage',
    method: 'put',
    data: data
  })
}

// 删除通知管理
export function delTrafficManagementUserMessage(id) {
  return request({
    url: '/business/trafficManagementUserMessage/' + id,
    method: 'delete'
  })
}

// 导出通知管理
export function exportTrafficManagementUserMessage(query) {
  return request({
    url: '/business/trafficManagementUserMessage/export',
    method: 'get',
    params: query
  })
}