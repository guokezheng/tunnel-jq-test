import request from '@/utils/request'

// 查询产品列表
export function listProduct(query) {
  return request({
    url: '/product/list',
    method: 'get',
    params: query
  })
}

// 查询产品详细
export function getProduct(id) {
  return request({
    url: '/product/' + id,
    method: 'get'
  })
}

// 新增产品
export function addProduct(data) {
  return request({
    url: '/product',
    method: 'post',
    data: data
  })
}

// 修改产品
export function updateProduct(data) {
  return request({
    url: '/product',
    method: 'put',
    data: data
  })
}

// 删除产品
export function delProduct(id) {
  return request({
    url: '/product/' + id,
    method: 'delete'
  })
}

// 导出产品
export function exportProduct(query) {
  return request({
    url: '/product/export',
    method: 'get',
    params: query
  })
}
