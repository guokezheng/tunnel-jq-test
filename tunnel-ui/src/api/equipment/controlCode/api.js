import request from '@/utils/request'

// 查询控制码列表
export function listCode(query) {
  return request({
    url: '/code/list',
    method: 'get',
    params: query
  })
}

// 查询控制码详细
export function getCode(controlId) {
  return request({
    url: '/code/' + controlId,
    method: 'get'
  })
}

// 新增控制码
export function addCode(data) {
  return request({
    url: '/code',
    method: 'post',
    data: data
  })
}

// 修改控制码
export function updateCode(data) {
  return request({
    url: '/code',
    method: 'put',
    data: data
  })
}

// 删除控制码
export function delCode(controlId) {
  return request({
    url: '/code/' + controlId,
    method: 'delete'
  })
}