import request from '@/utils/request'

// 查询供配电单元列表
export function listPower(query) {
  return request({
    url: '/configcenter/power/list',
    method: 'get',
    params: query
  })
}

// 查询供配电单元详细
export function getPower(id) {
  return request({
    url: '/configcenter/power/' + id,
    method: 'get'
  })
}

// 新增供配电单元
export function addPower(data) {
  return request({
    url: '/configcenter/power',
    method: 'post',
    data: data
  })
}

// 修改供配电单元
export function updatePower(data) {
  return request({
    url: '/configcenter/power',
    method: 'put',
    data: data
  })
}

// 删除供配电单元
export function delPower(id) {
  return request({
    url: '/configcenter/power/' + id,
    method: 'delete'
  })
}

// 导出供配电单元
export function exportPower(query) {
  return request({
    url: '/configcenter/power/export',
    method: 'get',
    params: query
  })
}
// 下拉菜单
export function powerDropDown(query) {
  return request({
    url: '/configcenter/power/dropDown',
    method: 'get',
    params: query
  })
}

// 查询部门下拉树结构
export function powerTreeselect() {
  return request({
    url: '/configcenter/power/powerTreeselect',
    method: 'get'
  })
}


// 查询供配电单元列表
export function pagePowerByDeptIds(query) {
  return request({
    url: '/configcenter/power/pagePowerByDeptIds',
    method: 'get',
    params: query
  })
}
