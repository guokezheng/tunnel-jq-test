import request from '@/utils/request'

// 查询气象采集器信息列表
export function listReport(query) {
  return request({
    url: '/intelligent/report/list',
    method: 'get',
    params: query
  })
}

// 查询气象采集器信息详细
export function getReport(reportId) {
  return request({
    url: '/intelligent/report/' + reportId,
    method: 'get'
  })
}

// 新增气象采集器信息
export function addReport(data) {
  return request({
    url: '/intelligent/report',
    method: 'post',
    data: data
  })
}

// 修改气象采集器信息
export function updateReport(data) {
  return request({
    url: '/intelligent/report',
    method: 'put',
    data: data
  })
}

// 删除气象采集器信息
export function delReport(reportId) {
  return request({
    url: '/intelligent/report/' + reportId,
    method: 'delete'
  })
}

// 导出气象采集器信息
export function exportReport(query) {
  return request({
    url: '/intelligent/report/export',
    method: 'get',
    params: query
  })
}