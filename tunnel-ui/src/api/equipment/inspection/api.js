import request from '@/utils/request'

// 查询巡视管理列表
export function listInspection(query) {
  return request({
    url: '/inspection/list',
    method: 'get',
    params: query
  })
}

// 查询巡视管理详细
export function getInspection(inspectionId) {
  return request({
    url: '/inspection/' + inspectionId,
    method: 'get'
  })
}

// 新增巡视管理
export function addInspection(data) {
  return request({
    url: '/inspection',
    method: 'post',
    data: data
  })
}

// 修改巡视管理
export function updateInspection(data) {
  return request({
    url: '/inspection',
    method: 'put',
    data: data
  })
}

// 删除巡视管理
export function delInspection(inspectionId) {
  return request({
    url: '/inspection/' + inspectionId,
    method: 'delete'
  })
}
