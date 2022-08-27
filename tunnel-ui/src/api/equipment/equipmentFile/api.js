import request from '@/utils/request'

// 查询设备档案文件列表
export function listFile(query) {
  return request({
    url: '/file/list',
    method: 'get',
    params: query
  })
}

// 查询设备档案文件详细
export function getFile(id) {
  return request({
    url: '/file/' + id,
    method: 'get'
  })
}

// 新增设备档案文件
export function addFile(data) {
  return request({
    url: '/file',
    method: 'post',
    data: data
  })
}

// 修改设备档案文件
export function updateFile(data) {
  return request({
    url: '/file',
    method: 'put',
    data: data
  })
}

// 删除设备档案文件
export function delFile(id) {
  return request({
    url: '/file/' + id,
    method: 'delete'
  })
}
