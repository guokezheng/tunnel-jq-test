import request from '@/utils/request'

// 查询巡检机器人列表
export function getRobotList() {
  return request({
    url: '/devices/list',
    method: 'get',
    params: {eqType: 112}
  })
}
