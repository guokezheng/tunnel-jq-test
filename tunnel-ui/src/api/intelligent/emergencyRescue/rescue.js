import request from '@/utils/request'

// 查询应急救援列表
export function listRescue(query) {
  return request({
    url: '/emergencyRescue/rescue/list',
    method: 'get',
    params: query
  })
}

// 查询应急救援详细
export function getRescue(id) {
  return request({
    url: '/emergencyRescue/rescue/' + id,
    method: 'get'
  })
}

// 新增应急救援
export function addRescue(data) {
  return request({
    url: '/emergencyRescue/rescue',
    method: 'post',
    data: data
  })
}

// 修改应急救援
export function updateRescue(data) {
  return request({
    url: '/emergencyRescue/rescue',
    method: 'put',
    data: data
  })
}

// 删除应急救援
export function delRescue(id) {
  return request({
    url: '/emergencyRescue/rescue/' + id,
    method: 'delete'
  })
}

// 导出应急救援
export function exportRescue(query) {
  return request({
    url: '/emergencyRescue/rescue/export',
    method: 'get',
    params: query
  })
}
