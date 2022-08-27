import request from '@/utils/request'

export function listWarningType(query) {
  return request({
    url: '/warningType/list',
    method: 'get',
    params: query
  })
}


export function getWarningType(id) {
  return request({
    url: '/warningType/' + id,
    method: 'get'
  })
}


export function addWarningType(data) {
  return request({
    url: '/warningType',
    method: 'post',
    data: data
  })
}

// �޸�Ԥ������
export function updateWarningType(data) {
  return request({
    url: '/warningType',
    method: 'put',
    data: data
  })
}


export function delWarningType(id) {
  return request({
    url: '/warningType/' + id,
    method: 'delete'
  })
}


export function seePlanListById(id) {
  return request({
    url: '/warningInfo/seePlanListById/' + id,
    method: 'get'
  })
}
export function seeStrategyListById(id) {
  return request({
    url: '/warningInfo/seeStrategyListById/' + id,
    method: 'get'
  })
}
