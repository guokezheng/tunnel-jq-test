import request from '@/utils/request'


// 数据统计 年
export function getCarNumberByMonth(data) {
  return request({
    url: '/trafficFlowData/month/getCarNumberByMonth',
    method: 'post',
    data: data
  })
}

// 数据统计 月
export function getCarNumberByDay(data) {
  return request({
    url: '/trafficFlowData/day/getCarNumberByDay',
    method: 'post',
    data: data
  })
}

// 数据统计 日
export function getCarNumberByHour(data) {
  return request({
    url: '/trafficFlowData/hour/getCarNumberByHour',
    method: 'post',
    data: data
  })
}

// 查询当天预警事件总数
export function getTheWarningDataOfToday(query) {
  return request({
    url: '/warningInfo/getTheWarningDataOfToday',
    method: 'get',
    params: query
  })
}

// 今日各隧道车流量
export function getCarFlowNumberOfTodayGroupByTunnel() {
  return request({
    url: '/trafficFlowData/day/getCarFlowNumberOfTodayGroupByTunnel',
    method: 'get',
  })
}

// 按年月日查询隧道预警事件
export function getWarningDataAnalysis(query) {
  return request({
    url: '/warningInfo/getWarningDataAnalysis',
    method: 'get',
    params: query
  })
}