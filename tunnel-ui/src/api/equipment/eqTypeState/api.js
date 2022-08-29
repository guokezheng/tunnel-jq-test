import request from '@/utils/request'

// 查询设备类型状态关系列表
export function listEqTypeState(query) {
  return request({
    url: '/eqTypeState/list',
    method: 'get',
    params: query
  })
}


// 查询设备类型状态关系列表(设备名称横向)
export function listEqTypeStates(query) {
  return request({
    url: '/eqTypeState/getList',
    method: 'get',
    params: query
  })
}

// 查询设备类型状态关系列表（可控制选项）
export function listEqTypeStateIsControl(query) {
  return request({
    url: '/eqTypeState/isControlList',
    method: 'get',
    params: query
  })
}

// 查询设备类型状态关系详细
export function getEqTypeState(id) {
  return request({
    url: '/eqTypeState/' + id,
    method: 'get'
  })
}
// 根据设备类型查状态
export function getEqTypeStateByType(typeId) {
  return request({
    url: '/eqTypeState/getState/' + typeId,
    method: 'get'
  })
}

// 根据设备类型查状态(状态名称横向)
export function getEqTypeStatesByType(typeId,stateType) {
  return request({
    url: '/eqTypeState/getStates/' + typeId + '/' +stateType,
    method: 'get'
  })
}

// 新增设备类型状态关系
export function addEqTypeState(data) {
  return request({
    url: '/eqTypeState',
    method: 'post',
    data: data
  })
}
//上传时候
export function addEqTypeState1(data) {
  return request({
    url: '/eqTypeState/addFiles',
    method: 'post',
    data: data
  })
}
//提交
export function addList2(data) {
  return request({
    url: '/eqTypeState/addList',
    method: 'post',
    data: data
  })
}
//更新
export function updatePic(data) {
  return request({
    url: '/eqTypeState/updateStates',
    method: 'put',
    data: data
  })
}


//删除
export function deletePicture(id) {
  return request({
    url: '/eqTypeState/delFiles/'+id,
    method: 'delete',
  })
}

// 新增设备类型状态关系(批量添加)
export function addEqTypeStates(data) {
  return request({
    url: '/eqTypeState',
    method: 'post',
    data: data
  })
}

// 修改设备类型状态关系
export function updateEqTypeState(data) {
  return request({
    url: '/eqTypeState',
    method: 'put',
    data: data
  })
}

// 删除设备类型状态关系
export function delEqTypeState(id) {
  return request({
    url: '/eqTypeState/' + id,
    method: 'delete'
  })
}
// 删除一行
export function deleteRow(id) {
  return request({
    url: '/eqTypeState/delState/' + id,
    method: 'delete'
  })
}
// 删除设备
export function deleteEquiment(eqTypeId) {
  return request({
    url: '/eqTypeState/delStatesByTypeId/' + eqTypeId,
    method: 'delete'
  })
}

// 查询设备类型状态关系设备运行状态
export function getStateByRun(query) {
  return request({
    url: '/eqTypeState/getRunTypeList',
    method: 'get',
    params: query
  })
}
// 查询设备类型状态关系设备数据状态
export function getStateByData(query) {
  return request({
    url: '/eqTypeState/getDataTypeList',
    method: 'get',
    params: query
  })
}