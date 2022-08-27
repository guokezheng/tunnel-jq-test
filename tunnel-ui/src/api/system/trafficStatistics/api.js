import request from '@/utils/request'

// 查询车流量信息列表
export function listStatistics(query) {
  return request({
    url: '/statistics/list',
    method: 'get',
    params: query
  })
}

// 查询车流量信息详细
export function getStatistics(statisticsId) {
  return request({
    url: '/statistics/' + statisticsId,
    method: 'get'
  })
}

// 新增车流量信息
export function addStatistics(data) {
  return request({
    url: '/statistics',
    method: 'post',
    data: data
  })
}

// 修改车流量信息
export function updateStatistics(data) {
  return request({
    url: '/statistics',
    method: 'put',
    data: data
  })
}

// 删除车流量信息
export function delStatistics(statisticsId) {
  return request({
    url: '/statistics/' + statisticsId,
    method: 'delete'
  })
}

//
export function analysisData(tunnelId,holes) {
  return request({
    url: '/statistics/analysisData?tunnelId='+tunnelId + "&holes="+holes,
    method: 'get'
  })
}

export function analysisDataByTime(query) {
  return request({
    url: '/statistics/analysisDataByTIme',
    method: 'get',
    params: query
  })
}
