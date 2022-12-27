import request from '@/utils/request'
// 全景数据 - 运行环境接口

// 查看环境监测状态（水侵、烟感、声光报警、气体）
export function getToken() {
  return request({
    url: '/getToken',
    method: 'get',
  })
}

