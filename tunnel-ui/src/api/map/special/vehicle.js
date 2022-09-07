import request from '@/utils/request'

// 查询重点车辆列表
export function listVehicle(query) {
  return request({
    url: '/special/vehicle/list',
    method: 'get',
    params: query
  })
}

// 查询重点车辆详细
export function getVehicle(id) {
  return request({
    url: '/special/vehicle/' + id,
    method: 'get'
  })
}

// 新增重点车辆
export function addVehicle(data) {
  return request({
    url: '/special/vehicle',
    method: 'post',
    data: data
  })
}

// 修改重点车辆
export function updateVehicle(data) {
  return request({
    url: '/special/vehicle',
    method: 'put',
    data: data
  })
}

// 删除重点车辆
export function delVehicle(id) {
  return request({
    url: '/special/vehicle/' + id,
    method: 'delete'
  })
}

// 导出重点车辆
export function exportVehicle(query) {
  return request({
    url: '/special/vehicle/export',
    method: 'get',
    params: query
  })
}