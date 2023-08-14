/*
 * @Author: Praise-Sun 18053314396@163.com
 * @Date: 2022-10-17 09:18:00
 * @LastEditors: Praise-Sun 18053314396@163.com
 * @LastEditTime: 2022-10-21 09:32:21
 * @FilePath: \tunnel-ui\src\api\event\strategy.js
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
import request from '@/utils/request'

// 查询控制策略列表
export function listStrategy(query) {
  return request({
    url: '/strategy/list',
    method: 'get',
    params: query
  })
}
//查询所有定时策略详情信息
export function getSdStrategyAll(query) {
  return request({
    url: '/strategy/getSdStrategyAll',
    method: 'get',
    params: query
  })
}

// 查询控制策略详细
export function getStrategy(id) {
  return request({
    url: '/strategy/' + id,
    method: 'get'
  })
}

// 新增控制策略
export function addStrategy(data) {
  return request({
    url: '/strategy',
    method: 'post',
    data: data
  })
}

// 修改控制策略
export function updateStrategy(data) {
  return request({
    url: '/strategy',
    method: 'put',
    data: data
  })
}

// 导出策略
export function export1(query) {
  return request({
    url: '/strategy/export',
    method: 'get',
    params: query
  })
}

// 删除控制策略
export function delStrategy(id) {
  return request({
    url: '/strategy/' + id,
    method: 'delete'
  })
}
// 新增控制策略
export function addStrategyInfo(data) {
  return request({
    url: '/strategy/addStrategysInfo',
    method: 'post',
    data: data
  })
}
// 修改控制策略
export function updateStrategyInfo(data) {
  return request({
    url: '/strategy/updateStrategysInfo',
    method: 'post',
    data: data
  })
}
//手动控制执行
export function manualControlInfo(data) {
  return request({
    url: '/strategy/manualControl',
    method: 'post',
    data: data
  })
}

// 获取guid
export function getGuid() {
  return request({
    url: '/strategy/getGuid',
    method: 'get'
  })
}

// 手动控制策略
export function handleStrategy(id) {
  return request({
    url: '/strategy/handleStrategy/' + id,
    method: 'get'
  })
}

// 返回控制策略的设备和状态
export function getRl(id) {
  return request({
    url: '/strategyRl/getStrategyRlByStrategyId/' + id,
    method: 'get'
  })
}


export function updateState(query) {
  return request({
    url: '/strategy/switch',
    method: 'get',
    params: query
  })
}

// 触发控制
export function workTriggerInfo(id) {
  return request({
    url: '/strategy/workTriggerInfo/' + id,
    method: 'get'
  })
}

// 查询设备类型大小类 树结构 可控设备
export function getCategoryTree() {
  return request({
    url: '/bigType/getCategoryTree',
    method: 'get'
  })
}

// 查询设备类型大小类 树结构 所有设备
export function getCategoryAllTree() {
  return request({
    url: '/bigType/getCategoryAllTree',
    method: 'get'
  })
}
