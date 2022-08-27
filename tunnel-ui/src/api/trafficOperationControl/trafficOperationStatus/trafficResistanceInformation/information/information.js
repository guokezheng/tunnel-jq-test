import request from '@/utils/request'

// 查询交通通阻信息列表
export function listInformation(query) {
  return request({
    url: '/trafficResistanceInformation/information/list',
    method: 'get',
    params: query
  })
}

// 查询交通通阻信息详细
export function getInformation(id) {
  return request({
    url: '/trafficResistanceInformation/information/' + id,
    method: 'get'
  })
}

// 新增交通通阻信息
export function addInformation(data) {
  return request({
    url: '/trafficResistanceInformation/information',
    method: 'post',
    data: data
  })
}

// 修改交通通阻信息
export function updateInformation(data) {
  return request({
    url: '/trafficResistanceInformation/information',
    method: 'put',
    data: data
  })
}

// 删除交通通阻信息
export function delInformation(id) {
  return request({
    url: '/trafficResistanceInformation/information/' + id,
    method: 'delete'
  })
}

// 导出交通通阻信息
export function exportInformation(query) {
  return request({
    url: '/trafficResistanceInformation/information/export',
    method: 'get',
    params: query
  })
}