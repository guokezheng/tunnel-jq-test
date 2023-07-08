import request from '@/utils/request'

// 查询一次系统图配置列表
export function listGraph(query) {
  return request({
    url: '/configcenter/graph/list',
    method: 'get',
    params: query
  })
}

// 查询一次系统图配置详细
export function getGraph(id) {
  return request({
    url: '/configcenter/graph/' + id,
    method: 'get'
  })
}

// 新增一次系统图配置
export function addGraph(data) {
  return request({
    url: '/configcenter/graph',
    method: 'post',
    data: data
  })
}

// 修改一次系统图配置
export function updateGraph(data) {
  return request({
    url: '/configcenter/graph',
    method: 'put',
    data: data
  })
}

// 删除一次系统图配置
export function delGraph(id) {
  return request({
    url: '/configcenter/graph/' + id,
    method: 'delete'
  })
}

// 导出一次系统图配置
export function exportGraph(query) {
  return request({
    url: '/configcenter/graph/export',
    method: 'get',
    params: query
  })
}

// 导入路侧单元
export function importRoadPower() {
  return request({
    url: '/configcenter/graph/importRoadPower',
    method: 'get',
  })
}
