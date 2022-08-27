import request from '@/utils/request'

// 查询应急演练列表
export function listEmeDrill(query) {
  return request({
    url: '/business/emeDrill/list',
    method: 'get',
    params: query
  })
}

// 查询应急演练详细
export function getEmeDrill(id) {
  return request({
    url: '/business/emeDrill/' + id,
    method: 'get'
  })
}

// 新增应急演练
export function addEmeDrill(data) {
  return request({
    url: '/business/emeDrill',
    method: 'post',
    data: data
  })
}

// 修改应急演练
export function updateEmeDrill(data) {
  return request({
    url: '/business/emeDrill',
    method: 'put',
    data: data
  })
}

// 删除应急演练
export function delEmeDrill(id) {
  return request({
    url: '/business/emeDrill/' + id,
    method: 'delete'
  })
}

// 导出应急演练
export function exportEmeDrill(query) {
  return request({
    url: '/business/emeDrill/export',
    method: 'get',
    params: query
  })
}