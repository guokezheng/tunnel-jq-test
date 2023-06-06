import request from '@/utils/request'

// 查询【加强照明配置】列表
export function listConfig(query) {
  return request({
    url: '/enhancedLighting/config/list',
    method: 'get',
    params: query
  })
}

// 查询【加强照明配置】详细
export function getConfig(id) {
  return request({
    url: '/enhancedLighting/config/' + id,
    method: 'get'
  })
}

// 查询【加强照明配置】详细
export function getLightingConfigByParam(query) {
  return request({
    url: '/enhancedLighting/config/getLightingConfigByParam',
    method: 'get',
    params: query
  })
}

// 新增【加强照明配置】
export function addConfig(data) {
  return request({
    url: '/enhancedLighting/config',
    method: 'post',
    data: data
  })
}

// 修改【加强照明配置】
export function updateConfig(data) {
  return request({
    url: '/enhancedLighting/config',
    method: 'put',
    data: data
  })
}

// 删除【加强照明配置】
export function delConfig(id) {
  return request({
    url: '/enhancedLighting/config/' + id,
    method: 'delete'
  })
}

// 导出【加强照明配置】
export function exportConfig(query) {
  return request({
    url: '/enhancedLighting/config/export',
    method: 'get',
    params: query
  })
}
