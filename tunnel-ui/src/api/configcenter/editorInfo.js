import request from '@/utils/request'

// 查询站点编辑器信息详细
export function selectEditorInfoByPowerCode(query) {
  return request({
    url: '/configCenter/editorInfo/selectEditorInfoByPowerCode',
    method: 'get',
    params: query
  })
}

// 新增编辑器信息
export function saveInfo(data) {
  return request({
    url: '/configCenter/editorInfo',
    method: 'post',
    data: data
  })
}

// 删除编辑器信息
export function delInfo(powerCode) {
  return request({
    url: '/configCenter/editorInfo/' + powerCode,
    method: 'delete'
  })
}

