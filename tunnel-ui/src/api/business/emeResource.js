import request from '@/utils/request'

// 查询周边资源列表
export function listEmeResource(query) {
  return request({
    url: '/business/emeResource/list',
    method: 'get',
    params: query
  })
}

// 查询周边资源详细
export function getEmeResource(id) {
  return request({
    url: '/business/emeResource/' + id,
    method: 'get'
  })
}

// 新增周边资源
export function addEmeResource(data) {
  return request({
    url: '/business/emeResource',
    method: 'post',
    data: data
  })
}

// 修改周边资源
export function updateEmeResource(data) {
  return request({
    url: '/business/emeResource',
    method: 'put',
    data: data
  })
}

// 删除周边资源
export function delEmeResource(id) {
  return request({
    url: '/business/emeResource/' + id,
    method: 'delete'
  })
}

// 导出周边资源
export function exportEmeResource(query) {
  return request({
    url: '/business/emeResource/export',
    method: 'get',
    params: query
  })
}