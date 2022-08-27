import request from '@/utils/request'

// 查询事件数据列表
export function listEvent(query) {
  return request({
    url: '/yytarget/event/list',
    method: 'get',
    params: query
  })
}

// 查询事件数据详细
export function getEvent(id) {
  return request({
    url: '/yytarget/event/' + id,
    method: 'get'
  })
}

// 新增事件数据
export function addEvent(data) {
  return request({
    url: '/yytarget/event',
    method: 'post',
    data: data
  })
}

// 修改事件数据
export function updateEvent(data) {
  return request({
    url: '/yytarget/event',
    method: 'put',
    data: data
  })
}

// 删除事件数据
export function delEvent(id) {
  return request({
    url: '/yytarget/event/' + id,
    method: 'delete'
  })
}

// 导出事件数据
export function exportEvent(query) {
  return request({
    url: '/yytarget/event/export',
    method: 'get',
    params: query
  })
}
