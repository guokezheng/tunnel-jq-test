import request from '@/utils/request'

// 查询发布记录列表
export function listRecord(query) {
  return request({
    url: '/system/log/list',
    method: 'get',
    params: query
  })
}

// 查询发布记录详细
export function getRecord(id) {
  return request({
    url: '/system/record/' + id,
    method: 'get'
  })
}

// 新增发布记录
export function addRecord(data) {
  return request({
    url: '/system/record',
    method: 'post',
    data: data
  })
}

// 修改发布记录
export function updateRecord(data) {
  return request({
    url: '/system/record',
    method: 'put',
    data: data
  })
}

// 删除发布记录
export function delRecord(id) {
  return request({
    url: '/system/record/' + id,
    method: 'delete'
  })
}

// 导出发布记录
export function exportRecord(query) {
  return request({
    url: '/system/log/export',
    method: 'get',
    params: query
  })
}
