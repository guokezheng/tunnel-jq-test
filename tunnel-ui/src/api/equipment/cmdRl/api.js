import request from '@/utils/request'

// 查询plc 报文关联设备列表
export function listRl(query) {
  return request({
    url: '/rl/list',
    method: 'get',
    params: query
  })
}

// 查询plc 报文关联设备详细
export function getRl(cmdRlId) {
  return request({
    url: '/rl/' + cmdRlId,
    method: 'get'
  })
}

// 新增plc 报文关联设备
export function addRl(data) {
  return request({
    url: '/rl',
    method: 'post',
    data: data
  })
}

// 修改plc 报文关联设备
export function updateRl(data) {
  return request({
    url: '/rl',
    method: 'put',
    data: data
  })
}

// 删除plc 报文关联设备
export function delRl(cmdRlId) {
  return request({
    url: '/rl/' + cmdRlId,
    method: 'delete'
  })
}ssss
