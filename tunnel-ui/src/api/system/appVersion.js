import request from '@/utils/request'

// 查询app版本管理列表
export function listAppVersion(query) {
  return request({
    url: '/system/appVersion/list',
    method: 'get',
    params: query
  })
}

// 查询app版本管理详细
export function getAppVersion(id) {
  return request({
    url: '/system/appVersion/' + id,
    method: 'get'
  })
}

// 新增app版本管理
export function addAppVersion(data) {
  return request({
    url: '/system/appVersion',
    method: 'post',
    data: data
  })
}

// 修改app版本管理
export function updateAppVersion(data) {
  return request({
    url: '/system/appVersion',
    method: 'put',
    data: data
  })
}

// 删除app版本管理
export function delAppVersion(id) {
  return request({
    url: '/system/appVersion/' + id,
    method: 'delete'
  })
}

// 导出app版本管理
export function exportAppVersion(query) {
  return request({
    url: '/system/appVersion/export',
    method: 'get',
    params: query
  })
}
