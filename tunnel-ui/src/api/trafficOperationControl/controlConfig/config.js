import request from '@/utils/request'

// 查询管控等级配置列表
export function listConfig(query) {
  return request({
    url: '/controlConfig/config/list',
    method: 'get',
    params: query
  })
}

// 查询管控等级配置详细
export function getConfig(id) {
  return request({
    url: '/controlConfig/config/' + id,
    method: 'get'
  })
}

// 新增管控等级配置
export function addConfig(data) {
  return request({
    url: '/controlConfig/config',
    method: 'post',
    data: data
  })
}

// 修改管控等级配置
export function updateConfig(data) {
  return request({
    url: '/controlConfig/config',
    method: 'put',
    data: data
  })
}

// 删除管控等级配置
export function delConfig(id) {
  return request({
    url: '/controlConfig/config/' + id,
    method: 'delete'
  })
}

// 导出管控等级配置
export function exportConfig(query) {
  return request({
    url: '/controlConfig/config/export',
    method: 'get',
    params: query
  })
}
// 获取配置措施
export function getControlMeasure(query) {
  return request({
    url: '/controlConfig/config/getControlMeasure',
    method: 'get',
    params: query
  })
}
// 提交 配置措施 弹窗 确定按钮
export function saveControlMeasure(data) {
  return request({
    url: '/controlConfig/config/saveControlMeasure',
    method: 'post',
    data: data
  })
}

