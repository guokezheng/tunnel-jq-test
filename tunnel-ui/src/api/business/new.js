/*
 * @Author: Praise-Sun 18053314396@163.com
 * @Date: 2022-11-16 10:28:09
 * @LastEditors: Praise-Sun 18053314396@163.com
 * @LastEditTime: 2022-11-16 17:28:26
 * @FilePath: \tunnel-ui\src\api\business\new.js
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
import request from '@/utils/request'

// 设备状态
export function getEquipmentStatus() {
    return request({
        url: '/proportionOfEquipment/getEquipmentStatus',
        method: 'get',
    })
}

// 设备占比
export function getEquipmentStatusList() {
    return request({
        url: '/proportionOfEquipment/list',
        method: 'get',
    })
}

// 本月预警事件
export function getEventWarning() {
    return request({
        url: '/eventWarning/getEventWarning',
        method: 'get',
    })
}
//近30天隧道预警
export function getWarningnum() {
    return request({
        url: '/tunnelWarning/warningnum',
        method: 'get',
    })
}

//近12h控制记录
export function getRecordlist(query) {
    return request({
        url: '/controlRecord/recordlist',
        method: 'post',
        params: query
    })
}

// 今日报警统计
export function getToDayEventWarning(query) {
    return request({
        url: '/eventWarning/getToDayEventWarning',
        method: 'get',
        params: query
    })
}

// 本周报警统计
export function getToWeekEventWarning(query) {
    return request({
        url: '/eventWarning/getToWeekEventWarning',
        method: 'get',
        params: query
    })
}

// 本月报警趋势
export function getSameMonthEventWarning(query) {
    return request({
        url: '/eventWarning/getSameMonthEventWarning',
        method: 'get',
        params: query
    })
}

// 累计报警分析
export function getCumulativeAlarm(query) {
    return request({
        url: '/eventWarning/getCumulativeAlarm',
        method: 'get',
        params: query
    })
}

// 设备故障信息列表
export function getequipmentFailure(query) {
    return request({
        url: '/eventWarning/getequipmentFailure',
        method: 'get',
        params: query
    })
}

// 交通事件信息列表
export function getTrafficIncident(query) {
    return request({
        url: '/eventWarning/getTrafficIncident',
        method: 'get',
        params: query
    })
}



// 主动安全信息列表
export function getActiveSafety(query) {
    return request({
        url: '/eventWarning/getActiveSafety',
        method: 'get',
        params: query
    })
}


// 预案列表
export function getReservePlan() {
    return request({
        url: '/eventWarning/getReservePlan',
        method: 'get',
    })
}
// 应急物资
export function getEmergencyMaterials() {
    return request({
        url: '/eventWarning/getEmergencyMaterials',
        method: 'get',
    })
}
// 值班人员
export function getEmergencyPer() {
    return request({
        url: '/eventWarning/getEmergencyPer',
        method: 'get',
    })
}