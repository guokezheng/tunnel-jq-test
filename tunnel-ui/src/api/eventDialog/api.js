import request from '@/utils/request'

// 查询事件详情图片
export function image(query) {
  return request({
    url: "/traffic/image/Image",
    method: 'get',
    params: query
  })
}

// 查询事件详情视频
export function video(query) {
  return request({
    url: "/event/evntId",
    method: 'get',
    params: query
  })
}

// 三图一视点处理 让后台在处置流程中加上事件确认流程
export function userConfirm(query) {
  return request({
    url: "/eventFlow/userConfirm/" + query,
    method: 'get',
    params: query
  })
}

// 上行下行摄像机
export function getEventCamera(tunnelId,stakeNum,direction) {
  return request({
    url: "/event/getEventCamera?tunnelId="+tunnelId+"&stakeNum="+stakeNum+"&direction="+direction,
    method: 'get',
  })
}