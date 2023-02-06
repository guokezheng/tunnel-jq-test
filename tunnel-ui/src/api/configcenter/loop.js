import request from '@/utils/request'

// 查询回路列表
export function listLoop(query) {
  return request({
    url: '/configcenter/loop/list',
    method: 'get',
    params: query
  })
}

// 查询回路详细
export function getLoop(id) {
  return request({
    url: '/configcenter/loop/' + id,
    method: 'get'
  })
}

// 新增回路
export function addLoop(data) {
  return request({
    url: '/configcenter/loop',
    method: 'post',
    data: data
  })
}

// 修改回路
export function updateLoop(data) {
  return request({
    url: '/configcenter/loop',
    method: 'put',
    data: data
  })
}

// 删除回路
export function delLoop(id) {
  return request({
    url: '/configcenter/loop/' + id,
    method: 'delete'
  })
}

// 导出回路
export function exportLoop(query) {
  return request({
    url: '/configcenter/loop/export',
    method: 'get',
    params: query
  })
}

// 查询回路下拉选项+条件查询
export function loopDropDown(query) {
  return request({
    url: '/configcenter/loop/dropDown',
    method: 'get',
    params: query
  })
}
//导入
export function importTemplate() {
  return request({
    url: '/configcenter/loop/importTemplate',
    method: 'get'
  })
}
// 回路树
export function getLoopTree(powerCode) {
  return request({
    url: '/configcenter/loop/getLoopTree/'+powerCode,
    method: 'get',
  })
}
// 修改回路分项
export function updateLoopByIdList(query) {
  return request({
    url: '/configcenter/loop/updateLoopByIdList',
    method: 'get',
    params: query
  })
}
