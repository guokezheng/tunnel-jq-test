import request from '@/utils/request'

// 查询车道事件列表
export function listEventanalysis(query) {
  return request({
    url: '/eventanalysis/list',
    method: 'get',
    params: query
  })
}

// 查询车道事件详细
export function getEventanalysis(id) {
  return request({
    url: '/eventanalysis/' + id,
    method: 'get'
  })
}
// 根据任务查询车道信息详细
export function getEvId(id) {
  return request({
    url: '/eventanalysis/getEventAnalysisByTaskId/' + id,
    method: 'get'
  })
}
// 新增车道事件
export function addEventanalysis(data) {
  return request({
    url: '/eventanalysis',
    method: 'post',
    data: data
  })
}

// 修改车道事件
export function updateEventanalysis(data) {
  return request({
    url: '/eventanalysis',
    method: 'put',
    data: data
  })
}

// 删除车道事件
export function delEventanalysis(id) {
  return request({
    url: '/eventanalysis/' + id,
    method: 'delete'
  })
}

// 导出车道事件
export function exportEventanalysis(query) {
  return request({
    url: '/eventanalysis/export',
    method: 'get',
    params: query
  })
}
