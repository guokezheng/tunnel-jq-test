import request from '@/utils/request'
import qs from 'qs'

// 发送指令
export function sendDirective(data) {
  return request({
    url: '/devices/sendDirective',
    method: 'post',
    headers: {
      "Content-Type": "application/x-www-form-urlencoded"
    },
    transformRequest: [function (data) {
      // `transformRequest` 允许在向服务器发送前，修改请求数据
      // 只能用在 'PUT', 'POST' 和 'PATCH' 这几个请求方法
      data.sex = 'man'
      return qs.stringify(data)
      // 结合create_headers里的内容，在这里又新增一条信息sex=man
      // 因此network中查看的结果是：name=xiaoming&age=12&sex=man
    }],
    data
  })
}
