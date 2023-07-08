import request from '@/utils/request'

// 查询机柜列表
export function listCabinet(query) {
  return request({
    url: '/configcenter/cabinet/list',
    method: 'get',
    params: query
  })
}

// 查询机柜详细
export function getCabinet(id) {
  return request({
    url: '/configcenter/cabinet/' + id,
    method: 'get'
  })
}

// 新增机柜
export function addCabinet(data) {
  return request({
    url: '/configcenter/cabinet',
    method: 'post',
    data: data
  })
}

// 修改机柜
export function updateCabinet(data) {
  return request({
    url: '/configcenter/cabinet',
    method: 'put',
    data: data
  })
}

// 删除机柜
export function delCabinet(id) {
  return request({
    url: '/configcenter/cabinet/' + id,
    method: 'delete'
  })
}

// 导出机柜
export function exportCabinet(query) {
  return request({
    url: '/configcenter/cabinet/export',
    method: 'get',
    params: query
  })
}
// 查询下拉选项+条件查询
export function cabinetDropDown(query) {
  return request({
    url: '/configcenter/cabinet/dropDown',
    method: 'get',
    params: query
  })
}
