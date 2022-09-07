import request from '@/utils/request'

// 查询雷达监测感知数据列表
export function listData(query) {
  return request({
    url: '/radar/data/list',
    method: 'get',
    params: query
  })
}

// 查询雷达监测感知数据详细
export function getData(id) {
  return request({
    url: '/radar/data/' + id,
    method: 'get'
  })
}

// 新增雷达监测感知数据
export function addData(data) {
  return request({
    url: '/radar/data',
    method: 'post',
    data: data
  })
}

// 修改雷达监测感知数据
export function updateData(data) {
  return request({
    url: '/radar/data',
    method: 'put',
    data: data
  })
}

// 删除雷达监测感知数据
export function delData(id) {
  return request({
    url: '/radar/data/' + id,
    method: 'delete'
  })
}

// 导出雷达监测感知数据
export function exportData(query) {
  return request({
    url: '/radar/data/export',
    method: 'get',
    params: query
  })
}