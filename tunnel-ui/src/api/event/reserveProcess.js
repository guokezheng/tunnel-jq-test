import request from '@/utils/request'

// 查询预案流程节点列表
export function listProcess(query) {
  return request({
    url: '/plan/process/list',
    method: 'get',
    params: query
  })
}

// 查询预案流程节点详细
export function getProcess(id) {
  return request({
    url: '/plan/process/' + id,
    method: 'get'
  })
}

// 新增预案流程节点
export function addProcess(data) {
  return request({
    url: '/plan/process',
    method: 'post',
    data: data
  })
}

// 修改预案流程节点
export function updateProcess(data) {
  return request({
    url: '/plan/process',
    method: 'put',
    data: data
  })
}

// 删除预案流程节点
export function delProcess(id) {
  return request({
    url: '/system/process/' + id,
    method: 'delete'
  })
}

// 导出预案流程节点
export function exportProcess(query) {
  return request({
    url: '/system/process/export',
    method: 'get',
    params: query
  })
}
