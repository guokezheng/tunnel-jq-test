import request from '@/utils/request'

// 查询紧急电话记录列表
export function listRecord(query) {
  return request({
    url: '/record/list',
    method: 'get',
    params: query
  })
}

// 查询紧急电话记录列表
export function positionRecord() {
  return request({
    url: '/record/positionList',
    method: 'get'
  })
}

// 查询紧急电话记录详细
export function getRecord(id) {
  return request({
    url: '/record/' + id,
    method: 'get'
  })
}

// 新增紧急电话记录
export function addRecord(data) {
  return request({
    url: '/record',
    method: 'post',
    data: data
  })
}

// 修改紧急电话记录
export function updateRecord(data) {
  return request({
    url: '/record',
    method: 'put',
    data: data
  })
}

// 删除紧急电话记录
export function delRecord(id) {
  return request({
    url: '/record/' + id,
    method: 'delete'
  })
}
