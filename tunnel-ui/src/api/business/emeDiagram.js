import request from '@/utils/request'

// 查询作战示意图列表
export function listEmeDiagram(query) {
  return request({
    url: '/business/emeDiagram/list',
    method: 'get',
    params: query
  })
}

// 查询作战示意图详细
export function getEmeDiagram(id) {
  return request({
    url: '/business/emeDiagram/' + id,
    method: 'get'
  })
}

// 新增作战示意图
export function addEmeDiagram(data) {
  return request({
    url: '/business/emeDiagram',
    method: 'post',
    data: data
  })
}

// 修改作战示意图
export function updateEmeDiagram(data) {
  return request({
    url: '/business/emeDiagram',
    method: 'put',
    data: data
  })
}

// 删除作战示意图
export function delEmeDiagram(id) {
  return request({
    url: '/business/emeDiagram/' + id,
    method: 'delete'
  })
}

// 导出作战示意图
export function exportEmeDiagram(query) {
  return request({
    url: '/business/emeDiagram/export',
    method: 'get',
    params: query
  })
}