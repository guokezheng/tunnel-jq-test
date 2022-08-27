import request from '@/utils/request'

// 查询特种车辆通行管理列表
export function listTrafficManagement(query) {
  return request({
    url: '/business/trafficManagement/list',
    method: 'get',
    params: query
  })
}

// 查询特种车辆通行管理详细
export function getTrafficManagement(id) {
  return request({
    url: '/business/trafficManagement/' + id,
    method: 'get'
  })
}

// 新增特种车辆通行管理
export function addTrafficManagement(data) {
  return request({
    url: '/business/trafficManagement',
    method: 'post',
    data: data
  })
}

// 修改特种车辆通行管理
export function updateTrafficManagement(data) {
  return request({
    url: '/business/trafficManagement',
    method: 'put',
    data: data
  })
}

// 删除特种车辆通行管理
export function delTrafficManagement(id) {
  return request({
    url: '/business/trafficManagement/' + id,
    method: 'delete'
  })
}

// 导出特种车辆通行管理
export function exportTrafficManagement(query) {
  return request({
    url: '/business/trafficManagement/export',
    method: 'get',
    params: query
  })
}

// 下发特种车辆通行管理
export function assignTrafficManagement(data) {
  return request({
    url: '/business/trafficManagement/assign',
    method: 'post',
    data: data
  })
}

