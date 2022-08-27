import request from '@/utils/request'

// 查询火警信息列表
export function listFirealarminfo(query) {
  return request({
    url: '/firealarminfo/list',
    method: 'get',
    params: query
  })
}

// 查询火警信息详细
export function getFirealarminfo(id) {
  return request({
    url: '/firealarminfo/' + id,
    method: 'get'
  })
}

// 新增火警信息
export function addFirealarminfo(data) {
  return request({
    url: '/firealarminfo',
    method: 'post',
    data: data
  })
}

// 修改火警信息
export function updateFirealarminfo(data) {
  return request({
    url: '/firealarminfo',
    method: 'put',
    data: data
  })
}

// 删除火警信息
export function delFirealarminfo(id) {
  return request({
    url: '/firealarminfo/' + id,
    method: 'delete'
  })
}