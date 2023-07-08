import request from '@/utils/request'

// 隧道重要事件情况
export function majorEvent() {
  return request({
    url: '/tunnelsData/majorEvent',
    method: 'get',
  })
}

// 分隧道事件统计
export function eventStat() {
  return request({
    url: '/tunnelsData/eventStat',
    method: 'get',
  })
}

// 感知事件类型统计
export function findEventStat() {
  return request({
    url: '/tunnelsData/findEventStat',
    method: 'get',
  })
}

// 隧道车流量情况
export function carFlow() {
  return request({
    url: '/tunnelsData/carFlow',
    method: 'get',
  })
}

// 各隧道实时车流量
export function realTimeCarFlow() {
  return request({
    url: '/tunnelsData/realTimeCarFlow',
    method: 'get',
  })
}

// 综合统计
export function allIndex() {
  return request({
    url: '/tunnelsData/index',
    method: 'get',
  })
}