import request from '@/utils/request'

// 查询设备指令列表
export function listDevcmd(query) {
  return request({
    url: '/devCmd/list',
    method: 'get',
    params: query
  })
}

// 查询设备指令详细
export function getDevcmd(codeId) {
  return request({
    url: '/devCmd/' + codeId,
    method: 'get'
  })
}

// 新增设备指令
export function addDevcmd(data) {
  return request({
    url: '/devCmd',
    method: 'post',
    data: data
  })
}

// 修改设备指令
export function updateDevcmd(data) {
  return request({
    url: '/devCmd',
    method: 'put',
    data: data
  })
}

// 删除设备指令
export function delDevcmd(codeId) {
  return request({
    url: '/devCmd/' + codeId,
    method: 'delete'
  })
}
