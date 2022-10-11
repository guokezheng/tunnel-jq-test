import request from '@/utils/request'

// 应急机构 批量删除
export function batchDelete(data) {
    return request({
      url: '/system/org/batchDelete',
      method: 'delete',
      data: data
    })
  }