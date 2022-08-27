import request from '@/utils/request'

// 查询应急资源列表
export function listMaterial(query) {
  return request({
    url: '/material/list',
    method: 'get',
    params: query
  })
}

// 查询应急资源详细
export function getMaterial(id) {
  return request({
    url: '/material/' + id,
    method: 'get'
  })
}
// 查询出入库记录详细
export function getCrkDetailById(data) {
  return request({
    url: '/material/materialId',
    method: 'get',
    params: data
  })
}

// 新增应急资源
export function addMaterial(data) {
  return request({
    url: '/material',
    method: 'post',
    data: data
  })
}

// 修改应急资源
export function updateMaterial(data) {
  return request({
    url: '/material',
    method: 'put',
    data: data
  })
}
// 应急资源出入库
export function updateMaterialCrk(data) {
  return request({
    url: '/material/material',
    method: 'put',
    data: data
  })
}



// 删除应急资源
export function delMaterial(id) {
  return request({
    url: '/material/' + id,
    method: 'delete'
  })
}
