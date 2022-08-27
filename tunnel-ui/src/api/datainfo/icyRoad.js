import request from '@/utils/request'

// 查询道路结冰记录列表
export function listIcyRoad(query) {
  return request({
    url: '/icyRoad/list',
    method: 'get',
    params: query
  })
}

// 查询道路结冰记录详细
export function getIcyRoad(id) {
  return request({
    url: '/icyRoad/' + id,
    method: 'get'
  })
}

// 新增道路结冰记录
export function addIcyRoad(data) {
  return request({
    url: '/icyRoad',
    method: 'post',
    data: data
  })
}

// 修改道路结冰记录
export function updateIcyRoad(data) {
  return request({
    url: '/icyRoad',
    method: 'put',
    data: data
  })
}

// 删除道路结冰记录
export function delIcyRoad(id) {
  return request({
    url: '/icyRoad/' + id,
    method: 'delete'
  })
}
