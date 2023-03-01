import request from '@/utils/request'

// 查询产品列表
export function listPoint(query) {
  return request({
    url: '/point/list',
    method: 'get',
    params: query
  })
}

// 查询产品详细
export function getPoint(id) {
  return request({
    url: '/point/' + id,
    method: 'get'
  })
}

// 新增产品
export function addPoint(data) {
  return request({
    url: '/point',
    method: 'post',
    data: data
  })
}

// 修改产品
export function updatePoint(data) {
  return request({
    url: '/point',
    method: 'put',
    data: data
  })
}

// 删除产品
export function delPoint(id) {
  return request({
    url: '/point/' + id,
    method: 'delete'
  })
}

// 导出产品
export function exportPoint(query) {
  return request({
    url: '/point/export',
    method: 'get',
    params: query
  })
}
