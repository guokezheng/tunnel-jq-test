import request from '@/utils/request'
// 全景数据 - 运行环境接口

// 查token
export function getToken() {
  return request({
    url: '/getToken',
    method: 'get',
  })
}

// 查外部系统的url
export function getThreeUrl() {
  return request({
    url: '/getThreeUrl',
    method: 'get',
  })
}
