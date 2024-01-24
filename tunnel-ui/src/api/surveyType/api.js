import request from '@/utils/request'

// 查询车辆类型配置列表
export function listType(query) {
  return request({
    url: '/system/type/list',
    method: 'get',
    params: query
  })
}

// 查询车辆类型配置详细
export function getType(id) {
  return request({
    url: '/system/type/' + id,
    method: 'get'
  })
}

// 新增车辆类型配置
export function addType(data) {
  return request({
    url: '/system/type',
    method: 'post',
    data: data
  })
}

// 修改车辆类型配置
export function updateType(data) {
  return request({
    url: '/system/type',
    method: 'put',
    data: data
  })
}

// 删除车辆类型配置
export function delType(id) {
  return request({
    url: '/system/type/' + id,
    method: 'delete'
  })
}

// 导出车辆类型配置
export function exportType(query) {
  return request({
    url: '/system/type/export',
    method: 'get',
    params: query
  })
}

// 校验车辆类型配置数据是否重复
export function checkData(data) {
  return request({
    url: '/system/type/checkData',
    method: 'post',
    data: data
  })
}

// 获取车辆类型数据下拉列表
export function getVehicleSelectList(data) {
  return request({
    url: '/system/type/getVehicleSelectList',
    method: 'post',
    data: data
  })
}

// 获取重点车辆列表
export function getFocusVehicleList(query) {
  return request({
    url: '/system/focusVehicle/list',
    method: 'get',
    params: query
  })
}
