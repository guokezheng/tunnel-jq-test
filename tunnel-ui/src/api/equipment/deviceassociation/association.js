import request from '@/utils/request'

// 查询隧道关联关系列表
export function listAssociation(query) {
  return request({
    url: '/system/association/list',
    method: 'get',
    params: query
  })
}

// 查询隧道关联关系详细
export function getAssociation(id) {
  return request({
    url: '/system/association/' + id,
    method: 'get'
  })
}

export function getAssociationByTunnelId(tunnelId) {
  return request({
    url: '/system/association/getDetail/' + tunnelId,
    method: 'get'
  })
}

// 新增隧道关联关系
export function addAssociation(data) {
  return request({
    url: '/system/association',
    method: 'post',
    data: data
  })
}

// 修改隧道关联关系
export function updateAssociation(data) {
  return request({
    url: '/system/association',
    method: 'put',
    data: data
  })
}

// 删除隧道关联关系
export function delAssociation(id) {
  return request({
    url: '/system/association/' + id,
    method: 'delete'
  })
}

export function delAssociationByTunnelIds(tunnelId) {
  return request({
    url: '/system/association/delDetials/' + tunnelId,
    method: 'delete'
  })
}

// 导出隧道关联关系
export function exportAssociation(query) {
  return request({
    url: '/system/association/export',
    method: 'get',
    params: query
  })
}
