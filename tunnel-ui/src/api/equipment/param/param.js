import request from '@/utils/request'

// 查询设备控制状态参数列表
export function listParam(query) {
  return request({
    url: '/inductionlamp/statusParam/list',
    method: 'get',
    params: query
  })
}

// 查询设备控制状态参数详细
export function getParam(id) {
  return request({
    url: '/inductionlamp/statusParam/' + id,
    method: 'get'
  })
}

// 新增设备控制状态参数
export function addParam(data) {
  return request({
    url: '/inductionlamp/statusParam',
    method: 'post',
    data: data
  })
}

// 修改设备控制状态参数
export function updateParam(data) {
  return request({
    url: '/inductionlamp/statusParam',
    method: 'put',
    data: data
  })
}

// 删除设备控制状态参数
export function delParam(id) {
  return request({
    url: '/inductionlamp/statusParam/' + id,
    method: 'delete'
  })
}

// 导出设备控制状态参数
export function exportParam(query) {
  return request({
    url: '/inductionlamp/statusParam/export',
    method: 'get',
    params: query
  })
}
