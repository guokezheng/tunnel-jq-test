import request from '@/utils/request'

// 查询车流量
export function getTrafficVolume(tunnelId,direction) {
  var query = {
    tunnelId:tunnelId,
    direction:direction
  }
  return request({
    url: '/statistics/bigscreenStatisticsList',
    method: 'get',
    params: query
  })
}
// 查询传感数据
export function getSensorData(tunnelId,direction) {
  var query = {
    tunnelId:tunnelId,
    direction:direction
  }
  return request({
    url: '/statistics/bigscreenCgqList',
    method: 'get',
    params: query
  })
}
