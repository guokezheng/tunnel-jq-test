import request from '@/utils/request'

// 查询备品备件库列表
export function listWarehouse(query) {
  return request({
    url: '/system/warehouse/list',
    method: 'get',
    params: query
  })
}

// 查询备品备件库详细
export function getWarehouse(id) {
  return request({
    url: '/system/warehouse/' + id,
    method: 'get'
  })
}

// 新增备品备件库
export function addWarehouse(data) {
  return request({
    url: '/system/warehouse',
    method: 'post',
    data: data
  })
}

// 修改备品备件库
export function updateWarehouse(data) {
  return request({
    url: '/system/warehouse',
    method: 'put',
    data: data
  })
}

// 删除备品备件库
export function delWarehouse(id) {
  return request({
    url: '/system/warehouse/' + id,
    method: 'delete'
  })
}

// 导出备品备件库
export function exportWarehouse(query) {
  return request({
    url: '/system/warehouse/export',
    method: 'get',
    params: query
  })
}