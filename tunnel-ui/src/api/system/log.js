import request from '@/utils/request'

// 查询操作日志列表
export function listLog(query) {
  return request({
    url: '/log/list',
    method: 'get',
    params: query
  })
}

// 查询操作日志详细
export function getLog(id) {
  return request({
    url: '/log/' + id,
    method: 'get'
  })
}

// 新增操作日志
export function addLog(data) {
  return request({
    url: '/log',
    method: 'post',
    data: data
  })
}

// 修改操作日志
export function updateLog(data) {
  return request({
    url: '/log',
    method: 'put',
    data: data
  })
}

// 删除操作日志
export function delLog(id) {
  return request({
    url: '/log/' + id,
    method: 'delete'
  })
}

// 导出操作日志
export function exportLogininfor1(query) {
  return request({
    url: '/log/export',
    method: 'get',
    params: query
  })
}


