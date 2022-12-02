import request from '@/utils/request'

// 查询物联设备厂商列表
export function listBrand(query) {
  return request({
    url: '/device/brand/list',
    method: 'get',
    params: query
  })
}

// 查询物联设备厂商详细
export function getBrand(supplierId) {
  return request({
    url: '/device/brand/' + supplierId,
    method: 'get'
  })
}

// 新增物联设备厂商
export function addBrand(data) {
  return request({
    url: '/device/brand',
    method: 'post',
    data: data
  })
}

// 修改物联设备厂商
export function updateBrand(data) {
  return request({
    url: '/device/brand',
    method: 'put',
    data: data
  })
}

// 删除物联设备厂商
export function delBrand(supplierId) {
  return request({
    url: '/device/brand/' + supplierId,
    method: 'delete'
  })
}

// 导出物联设备厂商
export function exportBrand(query) {
  return request({
    url: '/device/brand/export',
    method: 'get',
    params: query
  })
}
