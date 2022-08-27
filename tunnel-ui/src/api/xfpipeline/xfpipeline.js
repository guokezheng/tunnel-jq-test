import request from '@/utils/request'

// 查询消防管道监测列表
export function listXfpipeline(query) {
  return request({
    url: '/xfpipeline/list',
    method: 'get',
    params: query
  })
}

// 查询消防管道监测详细
export function getXfpipeline(id) {
  return request({
    url: '/xfpipeline/' + id,
    method: 'get'
  })
}

// 新增消防管道监测
export function addXfpipeline(data) {
  return request({
    url: '/xfpipeline',
    method: 'post',
    data: data
  })
}

// 修改消防管道监测
export function updateXfpipeline(data) {
  return request({
    url: '/xfpipeline',
    method: 'put',
    data: data
  })
}

// 删除消防管道监测
export function delXfpipeline(id) {
  return request({
    url: '/xfpipeline/' + id,
    method: 'delete'
  })
}

// 导出消防管道监测
export function exportXfpipeline(query) {
  return request({
    url: '/xfpipeline/export',
    method: 'get',
    params: query
  })
}
// 获取所有压力表一天上报次数
export function frequencyAll() {
  return request({
    url: '/xfwater/record/getAllPressureGaugesCollectedPerDay',
    method: 'get',
  })
}
// 获取指定压力表一天上报次数
export function frequency(query) {
  return request({
    url: '/xfwater/record/getNumberOfPressureGaugesCollectedPerDay',
    method: 'get',
    params: query
  })
}

// 压力表记录查询
export function record(query) {
  return request({
    url: '/xfwater/record/list',
    method: 'get',
    params: query
  })
}
// 获取所有压力表最近一条采集记录信息并判断是否已经离线
export function offLine() {
  return request({
    url: '/xfwater/record/getStatusOfAllPressureGauges',
    method: 'get',
  })
}
// 获取当天压力表预警信息
export function earlyWarning() {
  return request({
    url: '/warningInfo/getAllPressureGaugesWarningMsg',
    method: 'get',
  })
}
// 获取当天压力表信息
export function allMessage() {
  return request({
    url: '/devices/getAllPressureGaugesMsg',
    method: 'get',
  })
}
// 统计查询
export function getPressureGauges(query) {
  return request({
    url: '/xfwater/record/getHistoryOfPressureGauges',
    method: 'get',
    params: query
  })
}
