import request from '@/utils/request'


// 查询设备类型数据项列表
export function allListItem(query) {
  return request({
    url: '/eqType/item/allList',
    method: 'get',
    params: query
  })
}

// 查询设备类型数据项列表
export function listItem(query) {
  return request({
    url: '/eqType/item/list',
    method: 'get',
    params: query
  })
}

// 查询设备类型数据项详细
export function getItem(id) {
  return request({
    url: '/eqType/item/' + id,
    method: 'get'
  })
}

// 新增设备类型数据项
export function addItem(data) {
  return request({
    url: '/eqType/item',
    method: 'post',
    data: data
  })
}

// 修改设备类型数据项
export function updateItem(data) {
  return request({
    url: '/eqType/item',
    method: 'put',
    data: data
  })
}

// 删除设备类型数据项
export function delItem(id) {
  return request({
    url: '/eqType/item/' + id,
    method: 'delete'
  })
}

// 导出设备类型数据项
export function exportItem(query) {
  return request({
    url: '/eqType/item/export',
    method: 'get',
    params: query
  })
}

export function dataLogInfoList(query) {
  return request({
    url: '/system/data/dataLogInfoList',
    method: 'get',
    params: query
  })
}
//查询折线图
export function dataLogInfoLineList(query) {
  return request({
    url: '/system/data/dataLogInfoLineList',
    method: 'get',
    params: query
  })
}

//查询设备列表
export function dataDevicesLogInfoList(query) {
  return request({
    url: '/system/data/dataDevicesLogInfoList',
    method: 'get',
    params: query
  })
}




// 导出数据报表Tab
export function exportDatainforTab(query) {
  return request({
    url: '/system/data/exportDatainforTab',
    method: 'get',
    params: query
  })
}



// 导出详情数据报表
export function handleExportRecord(query) {
  return request({
    url: '/system/data/handleExportRecord',
    method: 'get',
    params: query
  })
}


