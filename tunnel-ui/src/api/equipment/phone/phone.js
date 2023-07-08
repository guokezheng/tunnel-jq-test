import request from '@/utils/request'


export function onMessage(data) {
  return request({
    url: '/phoneSpk/onMessage',
    method: 'post',
    data: data
  })
}
