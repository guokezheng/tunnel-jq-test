import request from '@/utils/request'

// 查询物联设备列表（网关设备和直连设备）
export function listIotDevice(query) {
  return request({
    url: '/configcenter/iotDevice/list',
    method: 'get',
    params: query
  })
}

// 查询物联设备列表(网关子设备)
export function listIotDeviceChild(query) {
  return request({
    url: '/configcenter/iotDevice/childlist',
    method: 'get',
    params: query
  })
}

// 查询物联设备详细
export function getIotDevice(id) {
  return request({
    url: '/configcenter/iotDevice/' + id,
    method: 'get'
  })
}

// 根据编号查询物联设备信息
export function getIotDeviceByCode(powerCode, deviceCode) {
  return request({
    url: '/configcenter/iotDevice/getInfoByCode/'+powerCode+'/'+deviceCode,
    method: 'get'
  })
}

// 新增物联设备
export function addIotDevice(data) {
  return request({
    url: '/configcenter/iotDevice',
    method: 'post',
    data: data
  })
}

// 修改物联设备
export function updateIotDevice(data) {
  return request({
    url: '/configcenter/iotDevice',
    method: 'put',
    data: data
  })
}

// 删除物联设备
export function delIotDevice(id) {
  return request({
    url: '/configcenter/iotDevice/' + id,
    method: 'delete'
  })
}

// 导出物联设备
export function exportIotDevice(query) {
  return request({
    url: '/configcenter/iotDevice/export',
    method: 'get',
    params: query
  })
}

//物联设备数据导入模板
export function importTemplate() {
  return request({
    url: '/configcenter/iotDevice/importTemplate',
    method: 'get'
  })
}
// 查询物联设备选项
export function iotDeviceDropDown(query) {
  return request({
    url: '/configcenter/iotDevice/dropDown',
    method: 'get',
    params: query
  })
}

//查询物联设备下拉选项+数据项级联
export function selectDeviceItemByPowerCode(query) {
  return request({
    url: '/configcenter/iotDevice/selectDeviceItemByPowerCode',
    method: 'get',
    params: query
  })
}

// 查询物联设备实时数据
export function deviceRealData(query) {
  return request({
    url: '/configcenter/iotDevice/deviceRealData',
    method: 'get',
    params: query
  })
}

//设备控制接口
export function deviceControl(query) {
  return request({
    url: '/configcenter/iotDevice/deviceControl',
    method: 'get',
    params: query
  })
}
