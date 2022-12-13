import request from '@/utils/request'

// 查询事件类型预案流程关联列表
export function listFlow(query) {
  return request({
    url: '/system/flow/list',
    method: 'get',
    params: query
  })
}

// 查询事件类型预案流程关联详细
export function getFlow(id) {
  return request({
    url: '/system/flow/' + id,
    method: 'get'
  })
}

// 新增事件类型预案流程关联
export function addFlow(data) {
  return request({
    url: '/system/flow',
    method: 'post',
    data: data
  })
}

// 修改事件类型预案流程关联
export function updateFlow(data) {
  return request({
    url: '/system/flow',
    method: 'put',
    data: data
  })
}

// 删除事件类型预案流程关联
export function delFlow(id) {
  return request({
    url: '/system/flow/' + id,
    method: 'delete'
  })
}

// 导出事件类型预案流程关联
export function exportFlow(query) {
  return request({
    url: '/system/flow/export',
    method: 'get',
    params: query
  })
}

// 查询预案流程树
export function getTypeFlowList() {
  return request({
    url: '/system/flow/getTypeFlowList',
    method: 'get'
  })
}
