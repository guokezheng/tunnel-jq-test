import request from '@/utils/request'

// 查询车辆通行白名单列表
export function listVehicleWhiteList(query) {
  return request({
    url: '/business/vehicleWhiteList/list',
    method: 'get',
    params: query
  })
}

// 查询车辆通行白名单详细
export function getVehicleWhiteList(id) {
  return request({
    url: '/business/vehicleWhiteList/' + id,
    method: 'get'
  })
}

// 新增车辆通行白名单
export function addVehicleWhiteList(data) {
  return request({
    url: '/business/vehicleWhiteList',
    method: 'post',
    data: data
  })
}

// 修改车辆通行白名单
export function updateVehicleWhiteList(data) {
  return request({
    url: '/business/vehicleWhiteList',
    method: 'put',
    data: data
  })
}

// 删除车辆通行白名单
export function delVehicleWhiteList(id) {
  return request({
    url: '/business/vehicleWhiteList/' + id,
    method: 'delete'
  })
}

// 导出车辆通行白名单
export function exportVehicleWhiteList(query) {
  return request({
    url: '/business/vehicleWhiteList/export',
    method: 'get',
    params: query
  })
}