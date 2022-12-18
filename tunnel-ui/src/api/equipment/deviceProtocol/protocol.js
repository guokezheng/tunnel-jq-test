import request from '@/utils/request'

// 查询设备协议列表
export function listProtocol(query) {
  return request({
    url: '/device/protocol/list',
    method: 'get',
    params: query
  })
}

// 查询设备协议详细
export function getProtocol(id) {
  return request({
    url: '/device/protocol/' + id,
    method: 'get'
  })
}

// 新增设备协议
export function addProtocol(data) {
  return request({
    url: '/device/protocol',
    method: 'post',
    data: data
  })
}

// 修改设备协议
export function updateProtocol(data) {
  return request({
    url: '/device/protocol',
    method: 'put',
    data: data
  })
}

// 删除设备协议
export function delProtocol(id) {
  return request({
    url: '/device/protocol/' + id,
    method: 'delete'
  })
}

// 导出设备协议
export function exportProtocol(query) {
  return request({
    url: '/device/protocol/export',
    method: 'get',
    params: query
  })
}
