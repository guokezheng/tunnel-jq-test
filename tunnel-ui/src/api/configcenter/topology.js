import request from '@/utils/request'

// 查询电力拓扑图列表
export function listTopology(query) {
  return request({
    url: '/system/topology/list',
    method: 'get',
    params: query
  })
}

// 查询电力拓扑图详细
export function getTopology(id) {
  return request({
    url: '/system/topology/' + id,
    method: 'get'
  })
}

// 新增电力拓扑图
export function addTopology(data) {
  return request({
    url: '/system/topology',
    method: 'post',
    data: data
  })
}

// 修改电力拓扑图
export function updateTopology(data) {
  return request({
    url: '/system/topology',
    method: 'put',
    data: data
  })
}

// 删除电力拓扑图
export function delTopology(id) {
  return request({
    url: '/system/topology/' + id,
    method: 'delete'
  })
}

// 导出电力拓扑图
export function exportTopology(query) {
  return request({
    url: '/system/topology/export',
    method: 'get',
    params: query
  })
}

// 查询全部部门
export function selectAllDept() {
  return request({
    url: '/system/topology/selectAllDept',
    method: 'get'
  })
}
