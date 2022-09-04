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