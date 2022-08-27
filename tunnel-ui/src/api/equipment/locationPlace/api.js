import request from '@/utils/request'

// 查询安装场所列表
export function listLocation(query) {
  return request({
    url: '/location/list',
    method: 'get',
    params: query
  })
}

// 查询安装场所详细
export function getLocation(placeId) {
  return request({
    url: '/location/' + placeId,
    method: 'get'
  })
}

// 新增安装场所
export function addLocation(data) {
  return request({
    url: '/location',
    method: 'post',
    data: data
  })
}

// 修改安装场所
export function updateLocation(data) {
  return request({
    url: '/location',
    method: 'put',
    data: data
  })
}

// 删除安装场所
export function delLocation(placeId) {
  return request({
    url: '/location/' + placeId,
    method: 'delete'
  })
}
