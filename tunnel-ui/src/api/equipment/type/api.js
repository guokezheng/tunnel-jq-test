/*
 * @Author: your name
 * @Date: 2022-01-04 11:14:17
 * @LastEditTime: 2022-03-21 15:33:02
 * @LastEditors: Please set LastEditors
 * @Description: 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 * @FilePath: \TunnelPlatform-V3\src\api\equipment\type\api.js
 */
import request from '@/utils/request'

// 查询设备类型列表
export function listType(query) {
  return request({
    url: '/type/list',
    method: 'get',
    params: query
  })
}

// 查询存在设备的设备类型列表
export function listHasType(tunnelId) {
  return request({
    url: '/type/hasList?tunnelId=' + tunnelId,
    method: 'get',
  })
}
export function hasListByBigType(bigType) {
  return request({
    url: '/type/hasListByBigType?bigType=' + bigType,
    method: 'get',
  })
}
// 查询存在设备的设备类型列表
export function groupByBigType(tunnelId) {
  return request({
    url: '/type/hasListGroupByBigType',
    method: 'get',
  })
}

// 查询设备类型列表
export function eqTypeList(data) {
  return request({
    url: '/type/eqTypeList',
    method: 'get',
    params: data
  })
}

// 查询设备类型详细
export function getType(typeId) {
  return request({
    url: '/type/' + typeId,
    method: 'get'
  })
}

// 新增设备类型
export function addType(data) {
  return request({
    url: '/type',///addEquipmentType
    method: 'post',
    data: data
  })
}

// 修改设备类型
export function updateType(data) {
  return request({
    url: '/type',
    method: 'put',
    data: data
  })
}

// 删除设备类型
export function delType(typeId) {
  return request({
    url: '/type/' + typeId,
    method: 'delete'
  })
}

//加载图片
export function loadPicture(data) {
  return request({
    url: '/type/loadPicture',
    method: 'post',
    params: data
  })
}

// 查询自动触发设备类型列表
export function autoEqTypeList(data) {
  return request({
    url: '/type/getHasItemEqTypeList',
    method: 'get',
    params: data
  })
}


// 查询设备需要执行的操作
export function getStateTypeId(data) {
  return request({
    url: '/eqTypeState/getDataTypeList',
    method: 'get',
    params: data
  })
}