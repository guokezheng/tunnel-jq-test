import request from '@/utils/request'

// 查询事件任务表列表
export function listEventtask(query) {
  return request({
    url: '/eventtask/list',
    method: 'get',
    params: query
  })
}

// 查询事件任务表详细
export function getEventtask(id) {
  return request({
    url: '/eventtask/' + id,
    method: 'get'
  })
}

// 新增事件任务表
export function addEventtask(data) {
  return request({
    url: '/eventtask',
    method: 'post',
    data: data
  })
}

// 修改事件任务表
export function updateEventtask(data) {
  return request({
    url: '/eventtask',
    method: 'put',
    data: data
  })
}

// 删除事件任务表
export function delEventtask(id) {
  return request({
    url: '/eventtask/' + id,
    method: 'delete'
  })
}

// 导出事件任务表
export function exportEventtask(query) {
  return request({
    url: '/eventtask/export',
    method: 'get',
    params: query
  })
}

// 增加车道检测区域、事件检测区域表
export function addTaskEvent(data) {
  return request({
    url: '/eventtask/addTaskEvent',
    method: 'post',
    data: data
  })
}

// 启动任务
export function startEventTask(id) {
  return request({
    url: '/eventtask/startEventTask/' + id,
    method: 'get',
  })
}
// 暂停任务
export function stopEventTask(id) {
  return request({
    url: '/eventtask/stopEventTask/' + id,
    method: 'get',
  })
}
// 增加车道检测区域、事件检测区域表
export function searchEventTask(id) {
  return request({
    url: '/eventtask/searchEventTask/' + id,
    method: 'get',
  })
}
  