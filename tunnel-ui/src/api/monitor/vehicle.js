import request from '@/utils/request'

// 车辆数据 表格
export function trafficVolumeList(query) {
    return request({
      url: '/trafficVolume/list',
      method: 'get',
      params: query
    })
  }

  // 车辆数据 导出
export function trafficVolumeExport(query) {
  return request({
    url: '/trafficVolume/export',
    method: 'get',
    params: query
  })
}