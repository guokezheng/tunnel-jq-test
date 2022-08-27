import request from '@/utils/request'

// 查询养护管理列表
export function listManagement(query) {
  return request({
    url: '/system/management/list',
    method: 'get',
    params: query
  })
}

// 查询养护管理详细
export function getManagement(id) {
  return request({
    url: '/system/management/' + id,
    method: 'get'
  })
}

// 新增养护管理
export function addManagement(data) {
  return request({
    url: '/system/management/add',
    method: 'post',
    data: data
  })
}

// 修改养护管理
export function updateManagement(data) {
  return request({
    url: '/system/management/update',
    method: 'post',
    data: data
  })
}

// 删除养护管理
export function delManagement(id) {
  return request({
    url: '/system/management/' + id,
    method: 'delete'
  })
}

// 导出养护管理
export function exportManagement(query) {
  return request({
    url: '/system/management/export',
    method: 'get',
    params: query
  })
}
