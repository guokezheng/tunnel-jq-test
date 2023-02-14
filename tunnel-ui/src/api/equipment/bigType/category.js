import request from '@/utils/request'

// 查询设备类型列表
export function listCategory(query) {
  return request({
    url: '/bigType/list',
    method: 'get',
    params: query
  })
}

// 查询设备类型详细
export function getCategory(id) {
  return request({
    url: '/bigType/' + id,
    method: 'get'
  })
}

// 新增设备类型
export function addCategory(data) {
  return request({
    url: '/bigType',
    method: 'post',
    data: data
  })
}

// 修改设备类型
export function updateCategory(data) {
  return request({
    url: '/bigType',
    method: 'put',
    data: data
  })
}

// 删除设备类型
export function delCategory(id) {
  return request({
    url: '/bigType/' + id,
    method: 'delete'
  })
}

// 导出设备类型
export function exportCategory(query) {
  return request({
    url: '/bigType/export',
    method: 'get',
    params: query
  })
}

// 构建前端所需要树结构
export function getCategoryTree(query) {
  return request({
    url: '/bigType/getCategoryTree',
    method: 'get',
    params: query
  })
}
