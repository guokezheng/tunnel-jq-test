 import request from '@/utils/request'

// 查询传感器采集数据信息列表
export function listMessage(query) {
  return request({
    url: '/message/list',
    method: 'get',
    params: query
  })
}

// 查询传感器采集数据信息详细
export function getMessage(id) {
  return request({
    url: '/message/' + id,
    method: 'get'
  })
}

// 新增传感器采集数据信息
export function addMessage(data) {
  return request({
    url: '/message',
    method: 'post',
    data: data
  })
}

// 修改传感器采集数据信息
export function updateMessage(data) {
  return request({
    url: '/message',
    method: 'put',
    data: data
  })
}

// 删除传感器采集数据信息
export function delMessage(id) {
  return request({
    url: '/message/' + id,
    method: 'delete'
  })
}
 