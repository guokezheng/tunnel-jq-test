import request from '@/utils/request'

export function getList(query) {
  return request({
    url: '/trafficControl/info/getList',
    method: 'get',
    params: query
  })
}

// 获取交通管制修改页详情信息
export function getTrafficControlInfo(id) {
  return request({
    url: '/trafficControl/info/getTrafficControlInfo/' + id,
    method: 'get',
    // params: query
  })
}
// 新增交通管制事件
export function addControlInfo(data) {
  return request({
    url: '/trafficControl/info/addControlInfo',
    method: 'post',
    data: data
  })
}

// 修改交通管制事件
export function updateControlInfo(data) {
  return request({
    url: '/trafficControl/info/updateControlInfo',
    method: 'put',
    data: data
  })
}

// 删除交通管制事件
export function delControlInfo(id){
  return request({
    url: '/trafficControl/info/' + id,
    method: 'delete',
    // params:{id:id}
  })
}



//获取发布内容
export function getPublishContent(id){
  return request({
    url: '/trafficControl/info/getPublishContent/'+id,
    method: 'get'
  })
}
