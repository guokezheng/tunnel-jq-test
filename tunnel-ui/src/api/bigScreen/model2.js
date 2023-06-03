import request from '@/utils/request'

// 设备故障预警
export function faultWarn() {
  return request({
    url: '/devicesData/faultWarn',
    method: 'get',
  })
}

// 综合统计
export function allIndex() {
    return request({
        url: '/devicesData/index',
        method: 'get',
    })
}

// 品牌故障率TOP10
export function monthFault() {
    return request({
        url: '/devicesData/monthFault',
        method: 'get',
    })
}

// 品牌故障率TOP10
export function realTimeStat() {
    return request({
        url: '/devicesData/realTimeStat',
        method: 'get',
    })
}


// 故障持续时间TOP10
export function faultTimeTop() {
    return request({
        url: '/devicesData/faultTimeTop',
        method: 'get',
    })
}

// 设备占比
export function eqPercent() {
    return request({
        url: '/devicesData/eqPercent',
        method: 'get',
    })
}

// 故障分类统计
export function faultCategory() {
    return request({
        url: '/devicesData/faultCategory',
        method: 'get',
    })
}