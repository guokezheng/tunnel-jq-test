import request from '@/utils/request'

// 昨日运营日流量
export function yesterdayFlow() {
  return request({
    url: '/operateData/yesterdayFlow',
    method: 'get',
  })
}

// 路况信息
export function event() {
    return request({
      url: '/operateData/event',
      method: 'get',
    })
}

  // 运营综合统计
export function allIndex() {
    return request({
        url: '/operateData/index',
        method: 'get',
    })
}