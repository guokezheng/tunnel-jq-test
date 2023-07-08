import request from '@/utils/request'

// 查询TCP通道列表
export function listChannel(query) {
  return request({
    url: '/configcenter/channel/list',
    method: 'get',
    params: query
  })
}

// 查询TCP通道详细
export function getChannel(id) {
  return request({
    url: '/configcenter/channel/' + id,
    method: 'get'
  })
}

// 新增TCP通道
export function addChannel(data) {
  return request({
    url: '/configcenter/channel',
    method: 'post',
    data: data
  })
}

// 修改TCP通道
export function updateChannel(data) {
  return request({
    url: '/configcenter/channel',
    method: 'put',
    data: data
  })
}

// 删除TCP通道
export function delChannel(id) {
  return request({
    url: '/configcenter/channel/' + id,
    method: 'delete'
  })
}

// 导出TCP通道
export function exportChannel(query) {
  return request({
    url: '/configcenter/channel/export',
    method: 'get',
    params: query
  })
}
