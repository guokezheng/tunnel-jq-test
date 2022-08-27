import request from '@/utils/request'

// 查询安全指数列表
export function listIndex(query) {
  return request({
    url: '/system/index/list',
    method: 'get',
    params: query
  })
}

// 查询安全指数详细
export function getIndex(id) {
  return request({
    url: '/system/index/' + id,
    method: 'get'
  })
}

// 新增安全指数
export function addIndex(data) {
  return request({
    url: '/system/index',
    method: 'post',
    data: data
  })
}

// 修改安全指数
export function updateIndex(data) {
  return request({
    url: '/system/index',
    method: 'put',
    data: data
  })
}

// 删除安全指数
export function delIndex(id) {
  return request({
    url: '/system/index/' + id,
    method: 'delete'
  })
}

// 导出安全指数
export function exportIndex(query) {
  return request({
    url: '/system/index/export',
    method: 'get',
    params: query
  })
}