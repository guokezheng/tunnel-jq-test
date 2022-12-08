import request from '@/utils/request'

// 查询外部系统列表
export function listSystem(query) {
  return request({
    url: '/system/system/list',
    method: 'get',
    params: query
  })
}

// 查询外部系统详细
export function getSystem(id) {
  return request({
    url: '/system/system/' + id,
    method: 'get'
  })
}

// 新增外部系统
export function addSystem(data) {
  return request({
    url: '/system/system',
    method: 'post',
    data: data
  })
}

// 修改外部系统
export function updateSystem(data) {
  return request({
    url: '/system/system',
    method: 'put',
    data: data
  })
}

// 删除外部系统
export function delSystem(id) {
  return request({
    url: '/system/system/' + id,
    method: 'delete'
  })
}

// 导出外部系统
export function exportSystem(query) {
  return request({
    url: '/system/system/export',
    method: 'get',
    params: query
  })
}