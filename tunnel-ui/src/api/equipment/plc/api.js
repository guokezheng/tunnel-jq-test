import request from '@/utils/request'

// 查询plc 主机列表
export function listHosts(query) {
  return request({
    url: '/hosts/list',
    method: 'get',
    params: query
  })
}

// 查询plc 主机详细
export function getHosts(plcId) {
  return request({
    url: '/hosts/' + plcId,
    method: 'get'
  })
}

// 新增plc 主机
export function addHosts(data) {
  return request({
    url: '/hosts',
    method: 'post',
    data: data
  })
}

// 修改plc 主机
export function updateHosts(data) {
  return request({
    url: '/hosts',
    method: 'put',
    data: data
  })
}

// 删除plc 主机
export function delHosts(plcId) {
  return request({
    url: '/hosts/' + plcId,
    method: 'delete'
  })
}
