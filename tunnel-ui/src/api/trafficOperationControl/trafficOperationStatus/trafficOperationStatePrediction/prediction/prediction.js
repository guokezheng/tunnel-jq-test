import request from '@/utils/request'

// 获取交通事故和清障信息 修改按钮 传row.id
export function getTrafficAccidentInfo(query) {
  return request({
    url: '/trafficAccident/info/getTrafficAccidentInfo',
    method: 'get',
    params: query
  })
}

// 查询交通突发事件列表
export function getList(query) {
  return request({
    url: '/trafficAccident/info/getList',
    method: 'get',
    params: query
  })
}

// 新增交通事故和清障信息
export function addAccident(data) {
  return request({
    url: '/trafficAccident/info/addAccident',
    method: 'post',
    data: data
  })
}

// 获取单个突发事件信息
export function getAccident(id) {
  return request({
    url: '/trafficAccident/info/getAccident/' + id,
    method: 'get'
  })
}

// 修改交通事故和清障信息
export function updateAccident(data) {
  return request({
    url: '/trafficAccident/info/updateAccident',
    method: 'post',
    data: data
  })
}

// 删除交通事故和清障信息
export function delInformation(id) {
  return request({
    url: '/trafficAccident/info/' + id,
    method: 'delete'
  })
}

// 上传图片
export function uploadImg(data) {
  return request({
    url: '/traffic/image/uploadImg2',
    method: 'post',
    data: data
  })
}

// 保存处理流程列表
export function saveProcessList(data) {
  return request({
    url: '/trafficIncident/process/saveProcessList',
    method: 'post',
    data: data
  })
}

// 获取交通事件-处理流程列表
export function getProcessList(query) {
  return request({
    url: '/trafficIncident/process/getProcessList',
    method: 'get',
    params: query
  })
}

//获取发布内容
export function getPublishContent(id){
  return request({
    url: '/trafficAccident/info/getPublishContent/'+id,
    method: 'get'
  })
}
