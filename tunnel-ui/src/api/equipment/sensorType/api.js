import request from '@/utils/request'

// 查询传感器类型列表
export function listType(query) {
  return request({
    url: '/sensorType/list',
    method: 'get',
    params: query
  })
}

// 查询传感器类型详细
export function getType(id) {
  return request({
    url: '/sensorType/' + id,
    method: 'get'
  })
}

// 新增传感器类型
export function addType(data) {
  return request({
    url: '/sensorType',
    method: 'post',
    data: data
  })
}

// 修改传感器类型
export function updateType(data) {
  return request({
    url: '/sensorType',
    method: 'put',
    data: data
  })
}

// 删除传感器类型
export function delType(id) {
  return request({
    url: '/sensorType/' + id,
    method: 'delete'
  })
}
