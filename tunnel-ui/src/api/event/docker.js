import request from '@/utils/request'

// 查询指令码固定字段表列表
export function listDocker(query) {
  return request({
    url: '/docker/list',
    method: 'get',
    params: query
  })
}

// 查询指令码固定字段表详细
export function getDocker(id) {
  return request({
    url: '/docker/' + id,
    method: 'get'
  })
}

// 新增指令码固定字段表
export function addDocker(data) {
  return request({
    url: '/docker',
    method: 'post',
    data: data
  })
}

// 修改指令码固定字段表
export function updateDocker(data) {
  return request({
    url: '/docker',
    method: 'put',
    data: data
  })
}

// 修改指令码固定字段表
export function dockerInit(id) {
  return request({
    url: '/docker/dockerInit/' + id,
    method: 'get'
  })
}

// 删除指令码固定字段表
export function delDocker(id) {
  return request({
    url: '/docker/' + id,
    method: 'delete'
  })
}

// 导出指令码固定字段表
export function exportDocker(query) {
  return request({
    url: '/docker/export',
    method: 'get',
    params: query
  })
}
