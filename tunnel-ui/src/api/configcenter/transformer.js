import request from '@/utils/request'

// 查询变压器列表
export function listTransformer(query) {
  return request({
    url: '/configcenter/transformer/list',
    method: 'get',
    params: query
  })
}

// 查询变压器详细
export function getTransformer(id) {
  return request({
    url: '/configcenter/transformer/' + id,
    method: 'get'
  })
}

// 新增变压器
export function addTransformer(data) {
  return request({
    url: '/configcenter/transformer',
    method: 'post',
    data: data
  })
}

// 修改变压器
export function updateTransformer(data) {
  return request({
    url: '/configcenter/transformer',
    method: 'put',
    data: data
  })
}

// 删除变压器
export function delTransformer(id) {
  return request({
    url: '/configcenter/transformer/' + id,
    method: 'delete'
  })
}

// 导出变压器
export function exportTransformer(query) {
  return request({
    url: '/configcenter/transformer/export',
    method: 'get',
    params: query
  })
}

// 变压器选项
export function dropDownTransformer(query) {
  return request({
    url: '/configcenter/transformer/dropDown',
    method: 'get',
    params: query
  })
}
