import request from '@/utils/request'

// 查询白名单车辆通行记录列表
export function listVehicleWhiteListRecord(query) {
  return request({
    url: '/business/vehicleWhiteListRecord/list',
    method: 'get',
    params: query
  })
}

// 查询白名单车辆通行记录详细
export function getVehicleWhiteListRecord(id) {
  return request({
    url: '/business/vehicleWhiteListRecord/' + id,
    method: 'get'
  })
}

// 新增白名单车辆通行记录
export function addVehicleWhiteListRecord(data) {
  return request({
    url: '/business/vehicleWhiteListRecord',
    method: 'post',
    data: data
  })
}

// 修改白名单车辆通行记录
export function updateVehicleWhiteListRecord(data) {
  return request({
    url: '/business/vehicleWhiteListRecord',
    method: 'put',
    data: data
  })
}

// 删除白名单车辆通行记录
export function delVehicleWhiteListRecord(id) {
  return request({
    url: '/business/vehicleWhiteListRecord/' + id,
    method: 'delete'
  })
}

// 导出白名单车辆通行记录
export function exportVehicleWhiteListRecord(query) {
  return request({
    url: '/business/vehicleWhiteListRecord/export',
    method: 'get',
    params: query
  })
}