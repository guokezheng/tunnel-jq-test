import request from '@/utils/request'

// 查询设备列表
export function listEquipmentRunning(query) {
  return request({
    url: '/equipmentRunning/list',
    method: 'get',
    params: query
  })
}
