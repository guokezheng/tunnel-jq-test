import request from '@/utils/request'

// 查询机动车信息列(目标类数据)列表
export function listCar(query) {
  return request({
    url: '/yytarget/car/list',
    method: 'get',
    params: query
  })
}

// 查询机动车信息列(目标类数据)详细
export function getCar(id) {
  return request({
    url: '/yytarget/car/' + id,
    method: 'get'
  })
}

// 新增机动车信息列(目标类数据)
export function addCar(data) {
  return request({
    url: '/yytarget/car',
    method: 'post',
    data: data
  })
}

// 修改机动车信息列(目标类数据)
export function updateCar(data) {
  return request({
    url: '/yytarget/car',
    method: 'put',
    data: data
  })
}

// 删除机动车信息列(目标类数据)
export function delCar(id) {
  return request({
    url: '/yytarget/car/' + id,
    method: 'delete'
  })
}

// 导出机动车信息列(目标类数据)
export function exportCar(query) {
  return request({
    url: '/yytarget/car/export',
    method: 'get',
    params: query
  })
}
