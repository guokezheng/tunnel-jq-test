import request from '@/utils/request'

// 查询关键设备列表
export function listEquipment(query) {
  return request({
    url: '/configcenter/equipment/list',
    method: 'get',
    params: query
  })
}

// 查询关键设备详细
export function getEquipment(id) {
  return request({
    url: '/configcenter/equipment/' + id,
    method: 'get'
  })
}

// 新增关键设备
export function addEquipment(data) {
  return request({
    url: '/configcenter/equipment',
    method: 'post',
    data: data
  })
}

// 修改关键设备
export function updateEquipment(data) {
  return request({
    url: '/configcenter/equipment',
    method: 'put',
    data: data
  })
}

// 删除关键设备
export function delEquipment(id) {
  return request({
    url: '/configcenter/equipment/' + id,
    method: 'delete'
  })
}

// 导出关键设备
export function exportEquipment(query) {
  return request({
    url: '/configcenter/equipment/export',
    method: 'get',
    params: query
  })
}
