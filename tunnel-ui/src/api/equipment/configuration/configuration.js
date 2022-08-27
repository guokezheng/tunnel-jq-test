import request from '@/utils/request'

// 查询隧道环境配置列表
export function listConfiguration(query) {
  return request({
    url: '/system/configuration/list',
    method: 'get',
    params: query
  })
}

// 查询隧道环境配置详细
export function getConfiguration(id) {
  return request({
    url: '/system/configuration/' + id,
    method: 'get'
  })
}

// 新增隧道环境配置
export function addConfiguration(data) {
  return request({
    url: '/system/configuration',
    method: 'post',
    data: data
  })
}

// 修改隧道环境配置
export function updateConfiguration(data) {
  return request({
    url: '/system/configuration',
    method: 'put',
    data: data
  })
}

// 删除隧道环境配置
export function delConfiguration(id) {
  return request({
    url: '/system/configuration/' + id,
    method: 'delete'
  })
}

// 导出隧道环境配置
export function exportConfiguration(query) {
  return request({
    url: '/system/configuration/export',
    method: 'get',
    params: query
  })
}