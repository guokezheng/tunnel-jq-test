import request from '@/utils/request'

// 查询预案文件列表
export function listReservePlanFile(query) {
  return request({
    url: '/reservePlanFile/list',
    method: 'get',
    params: query
  })
}

// 查询预案文件详细
export function getReservePlanFile(id) {
  return request({
    url: '/reservePlanFile/' + id,
    method: 'get'
  })
}

// 新增预案文件
export function addReservePlanFile(data) {
  return request({
    url: '/reservePlanFile',
    method: 'post',
    data: data
  })
}

// 修改预案文件
export function updateReservePlanFile(data) {
  return request({
    url: '/reservePlanFile',
    method: 'put',
    data: data
  })
}

// 删除预案文件
export function delReservePlanFile(id) {
  return request({
    url: '/reservePlanFile/' + id,
    method: 'delete'
  })
}