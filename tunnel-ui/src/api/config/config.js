import request from '@/utils/request'

// 查询数字孪生页面配置列表
export function listConfig(query) {
  return request({
    url: '/config/list',
    method: 'get',
    params: query
  })
}

// 查询数字孪生页面配置详细
export function getConfig(id) {
  return request({
    url: '/config/' + id,
    method: 'get'
  })
}

// 新增数字孪生页面配置
export function addConfig(data) {
  return request({
    url: '/config',
    method: 'post',
    data: data
  })
}

// 修改数字孪生页面配置
export function updateConfig(data) {
  return request({
    url: '/config',
    method: 'put',
    data: data
  })
}

// 删除数字孪生页面配置
export function delConfig(id) {
  return request({
    url: '/config/' + id,
    method: 'delete'
  })
}

// 导出数字孪生页面配置
export function exportConfig(query) {
  return request({
    url: '/config/export',
    method: 'get',
    params: query
  })
}
