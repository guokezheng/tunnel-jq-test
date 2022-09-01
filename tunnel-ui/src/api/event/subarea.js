import request from '@/utils/request'

// 查询隧道分区列表
export function listSubarea(query) {
  return request({
    url: '/tunnel/subarea/list',
    method: 'get',
    params: query
  })
}

// 查询隧道分区详细
export function getSubarea(sId) {
  return request({
    url: '/tunnel/subarea/' + sId,
    method: 'get'
  })
}

// 新增隧道分区
export function addSubarea(data) {
  return request({
    url: '/tunnel/subarea',
    method: 'post',
    data: data
  })
}

// 修改隧道分区
export function updateSubarea(data) {
  return request({
    url: '/tunnel/subarea',
    method: 'put',
    data: data
  })
}

// 删除隧道分区
export function delSubarea(sId) {
  return request({
    url: '/tunnel/subarea/' + sId,
    method: 'delete'
  })
}

// 导出隧道分区
export function exportSubarea(query) {
  return request({
    url: '/tunnel/subarea/export',
    method: 'get',
    params: query
  })
}