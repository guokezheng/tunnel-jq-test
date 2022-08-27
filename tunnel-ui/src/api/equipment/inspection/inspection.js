import request from '@/utils/request'

// 查询巡视记录列表
export function listInspection(query) {
  return request({
    url: '/system/inspection/list',
    method: 'get',
    params: query
  })
}

// 查询巡视记录详细
export function getInspection(inspectionId) {
  return request({
    url: '/system/inspection/' + inspectionId,
    method: 'get'
  })
}

// 新增巡视记录
export function addInspection(data) {
  return request({
    url: '/system/inspection',
    method: 'post',
    data: data
  })
}

// 修改巡视记录
export function updateInspection(data) {
  return request({
    url: '/system/inspection',
    method: 'put',
    data: data
  })
}

// 删除巡视记录
export function delInspection(inspectionId) {
  return request({
    url: '/system/inspection/' + inspectionId,
    method: 'delete'
  })
}

// 导出巡视记录
export function exportInspection(query) {
  return request({
    url: '/system/inspection/export',
    method: 'get',
    params: query
  })
}