import request from '@/utils/request'

// 查询故障清单列表
export function listList(query) {
  return request({
    url: '/fault/list/list',
    method: 'get',
    params: query
  })
}

// 查询故障清单详细
export function getList(id) {
  return request({
    url: '/fault/list/' + id,
    method: 'get'
  })
}

// 新增故障清单
export function addList(data) {
  return request({
    url: '/fault/list',
    method: 'post',
    data: data
  })
}

// 修改故障清单
export function updateList(data) {
  return request({
    url: '/fault/list',
    method: 'put',
    data: data
  })
}

// 删除故障清单
export function delList(id) {
  return request({
    url: '/fault/list/' + id,
    method: 'delete'
  })
}

// 导出故障清单
export function exportList(query) {
  return request({
    url: '/fault/list/export',
    method: 'get',
    params: query
  })
}


// 故障运行状态
export function getEquipmentInfo(query) {
  return request({
    url: '/fault/list/getEquipmentInfo',
    method: 'post',
    data: query
  })
}

// 故障详情
export function getRepairRecordList(data) {
  return request({
    url: '/fault/list/getFaultRepairInfo',
    method: 'post',
    data: data
  })
}


// 导出设备故障
export function exportFaultList(query) {
  return request({
    url: '/fault/list/export',
    method: 'get',
    params: query
  })
}
