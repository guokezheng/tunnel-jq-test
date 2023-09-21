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

// 照明波动开
export function openIllumination(data) {
  return request({
    url: '/illumination/openIllumination',
    method: 'post',
  })
}

// 照明波动关
export function closeIllumination(data) {
  return request({
    url: '/illumination/closeIllumination',
    method: 'post',
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


// 对外情报板控制
export function setWjModel(wjModel) {
  return request({
    url: '/parser/board/setWjModel/' + wjModel,
    method: 'get'
  })
}
