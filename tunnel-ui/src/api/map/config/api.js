import request from '@/utils/request'

// 交通监测 获取引入路径
export function configPage(query) {
    return request({
      url: '/config/configPage',
      method: 'get',
      params: query
    })
  }