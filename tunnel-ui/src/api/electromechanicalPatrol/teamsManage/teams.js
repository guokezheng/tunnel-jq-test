import request from '@/utils/request'

// 查询班组列表
export function listTeams(query) {
  return request({
    url: '/teams/list/list',
    method: 'get',
    params: query
  })
}

// 新增班组
export function addTeams(data) {
  return request({
    url: '/teams/list',
    method: 'post',
    data: data
  })
}

// 修改班组
export function updateTeams(data) {
  return request({
    url: '/teams/list',
    method: 'put',
    data: data
  })
}

// 查询班组详细
export function getTeams(deptId) {
  return request({
    url: '/teams/list/' + deptId,
    method: 'get'
  })
}

// 导出班组
export function exportTeams(query) {
  return request({
    url: '/teams/list/export',
    method: 'get',
    params: query
  })
}

// 删除部门
export function delTeams(deptId) {
  return request({
    url: '/teams/list/' + deptId,
    method: 'delete'
  })
}

// 查询班组已包含用户列表
export function teamsUserList(query) {
  return request({
    url: '/teams/list/teamsUserList',
    method: 'get',
    params: query
  })
}


// 查询班组未包含用户列表
export function unTeamsUserList(query) {
  return request({
    url: '/teams/list/unTeamsUserList',
    method: 'get',
    params: query
  })
}

//
// 班组授权用户选择
export function teamsUserSelectAll(data) {
  return request({
    url: '/teams/list/teamsUserSelectAll',
    method: 'put',
    params: data
  })
}



// 取消班组中的用户
export function deleteTeamsUserCancel(data) {
  return request({
    url: '/teams/list/deleteTeamsUserCancel',
    method: 'put',
    data: data
  })
}

// 批量取消班组中的用户
export function deleteTeamsUserCancelAll(data) {
  return request({
    url: '/teams/list/deleteTeamsUserCancelAll',
    method: 'put',
    params: data
  })
}


