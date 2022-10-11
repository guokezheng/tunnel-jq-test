import request from '@/utils/request'

// 应急车辆 批量删除
export function batchDelete(data) {
    return request({
      url: '/system/vehicle/batchDelete',
      method: 'delete',
      data: data
    })
  }