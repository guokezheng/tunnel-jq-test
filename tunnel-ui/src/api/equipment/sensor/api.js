import request from '@/utils/request'

// 查询传感器列列表
export function listSensor(query) {
  return request({
    url: '/sensor/list',
    method: 'get',
    params: query
  })
}

// 查询传感器列详细
export function getSensor(id) {
  return request({
    url: '/sensor/' + id,
    method: 'get'
  })
}

// 新增传感器列
export function addSensor(data) {
  return request({
    url: '/sensor',
    method: 'post',
    data: data
  })
}

// 修改传感器列
export function updateSensor(data) {
  return request({
    url: '/sensor',
    method: 'put',
    data: data
  })
}

// 删除传感器列
export function delSensor(id) {
  return request({
    url: '/sensor/' + id,
    method: 'delete'
  })
}

export function exportSensorList(query) {
  return request({
    url: '/business/exportSensorList/export',
    method: 'get',
    params: query
  })
}
