import request from '@/utils/request'

// 查询设备列表
export function listDevice(query) {
  return request({
    url: '/iot/device/list',
    method: 'get',
    params: query
  })
}

// 查询设备详细
export function getDevice(id) {
  return request({
    url: '/iot/device/' + id,
    method: 'get'
  })
}

// 新增设备
export function addDevice(data) {
  return request({
    url: '/iot/device',
    method: 'post',
    data: data
  })
}

// 修改设备
export function updateDevice(data) {
  return request({
    url: '/iot/device',
    method: 'put',
    data: data
  })
}

// 删除设备
export function delDevice(id) {
  return request({
    url: '/iot/device/' + id,
    method: 'delete'
  })
}

// 导出设备
export function exportDevice(query) {
  return request({
    url: '/iot/device/export',
    method: 'get',
    params: query
  })
}

/**
 * 查询设备功能详细
 * 权限标识：iot:device:queryFunction
 * @param id
 * @returns {AxiosPromise}
 */
export function getDeviceFunction(id) {
  return request({
    url: '/iot/device/function/' + id,
    method: 'get'
  })
}


/**
 * 功能调试，属性设置
 * 权限标识：iot:device:debug
 * @param data:
 *            deviceId: 设备主键
 *            functionId：功能主键
 *            value：调试属性值
 * @returns {AxiosPromise}
 */
export function setAttribute(data) {
  return request({
    url: '/iot/device/debug/attribute',
    method: 'put',
    data: data
  })
}

/**
 * 功能调试，属性获取
 * 权限标识：iot:device:debug
 * @param data:
 *            deviceId: 设备主键
 *            functionId：功能主键
 * @returns {AxiosPromise}
 */
export function getAttribute(data) {
  return request({
    url: '/iot/device/debug/attribute',
    method: 'get',
    params: data
  })
}


/**
 * 功能调试，服务调用
 * 权限标识：iot:device:debug
 * @param data:
 *            deviceId: 设备主键
 *            functionId：功能主键
 *            value：调试属性值
 * @returns {AxiosPromise}
 */
export function invokeService(data) {
  return request({
    url: '/iot/device/debug/service',
    method: 'put',
    data: data
  })
}
