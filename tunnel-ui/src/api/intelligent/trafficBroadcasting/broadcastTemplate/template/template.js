import request from '@/utils/request'

// 查询广播模板列表
export function listTemplate(query) {
  return request({
    url: '/broadcastTemplate/template/list',
    method: 'get',
    params: query
  })
}

// 查询广播模板详细
export function getTemplate(id) {
  return request({
    url: '/broadcastTemplate/template/' + id,
    method: 'get'
  })
}

// 新增广播模板
export function addTemplate(data) {
  return request({
    url: '/broadcastTemplate/template',
    method: 'post',
    data: data
  })
}

// 修改广播模板
export function updateTemplate(data) {
  return request({
    url: '/broadcastTemplate/template',
    method: 'put',
    data: data
  })
}

// 删除广播模板
export function delTemplate(id) {
  return request({
    url: '/broadcastTemplate/template/' + id,
    method: 'delete'
  })
}

// 导出广播模板
export function exportTemplate(query) {
  return request({
    url: '/broadcastTemplate/template/export',
    method: 'get',
    params: query
  })
}
