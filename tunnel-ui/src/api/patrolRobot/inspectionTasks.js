import request from '@/utils/request'

// 查询巡检任务列表
export function getInspectionTasksList(query) {
  return request({
    url: '/system/inspection/list',
    method: 'get',
    params: query
  })
}
// 新增巡检任务
export function addInspectionTasks(query) {
  return request({
    url: '/system/inspection',
    method: 'post',
    data: query
  })
}
// 修改巡检任务
export function updateInspectionTasks(query) {
  return request({
    url: '/system/inspection',
    method: 'put',
    data: query
  })
}
