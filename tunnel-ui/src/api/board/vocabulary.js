import request from '@/utils/request'

// 查询情报板敏感字管理列表
export function listVocabulary(query) {
  return request({
    url: '/system/vocabulary/list',
    method: 'get',
    params: query
  })
}

// 查询情报板敏感字管理详细
export function getVocabulary(id) {
  return request({
    url: '/system/vocabulary/' + id,
    method: 'get'
  })
}

// 新增情报板敏感字管理
export function addVocabulary(data) {
  return request({
    url: '/system/vocabulary',
    method: 'post',
    data: data
  })
}

// 修改情报板敏感字管理
export function updateVocabulary(data) {
  return request({
    url: '/system/vocabulary',
    method: 'put',
    data: data
  })
}

// 删除情报板敏感字管理
export function delVocabulary(id) {
  return request({
    url: '/system/vocabulary/' + id,
    method: 'delete'
  })
}

// 导出情报板敏感字管理
export function exportVocabulary(query) {
  return request({
    url: '/system/vocabulary/export',
    method: 'get',
    params: query
  })
}