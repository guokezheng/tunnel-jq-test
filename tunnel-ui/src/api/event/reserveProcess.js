/*
 * @Author: Praise-Sun 18053314396@163.com
 * @Date: 2022-09-17 10:28:08
 * @LastEditors: Praise-Sun 18053314396@163.com
 * @LastEditTime: 2022-09-19 15:08:40
 * @FilePath: \tunnel-ui\src\api\event\reserveProcess.js
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
import request from '@/utils/request'

// 查询预案流程节点列表
export function listProcess(query) {
  return request({
    url: '/plan/process/list',
    method: 'get',
    params: query
  })
}

// 查询预案流程节点详细
export function getProcess(id) {
  return request({
    url: '/plan/process/' + id,
    method: 'get'
  })
}

// 新增预案流程节点
export function addProcess(data) {
  return request({
    url: '/plan/process',
    method: 'post',
    data: data
  })
}

// 修改预案流程节点
export function updateProcess(data) {
  return request({
    url: '/plan/process',
    method: 'put',
    data: data
  })
}

// 删除预案流程节点
export function delProcess(id) {
  return request({
    url: '/plan/process/' + id,
    method: 'delete'
  })
}

// 导出预案流程节点
export function exportProcess(query) {
  return request({
    url: '/plan/process/export',
    method: 'get',
    params: query
  })
}

// 根据预案id查找预案流程节点
export function getListByRId(query) {
  return request({
    url: '/plan/process/getListByRId',
    method: 'get',
    params: query
  })
}

export function previewDisplay(reserveId) {
  return request({
    url: '/plan/process/previewDisplay?reserveId=' + reserveId,
    method: 'get',
  })
}


export function getSubareaByTunnelId(tunnelId) {
  return request({
    url: '/tunnel/subarea/getSubareaByTunnelId?tunnelId=' + tunnelId,
    method: 'get',
  })
}