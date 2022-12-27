import request from '@/utils/request'

// 查询电路列表
export function listCircuit(query) {
  return request({
    url: '/configcenter/circuit/list',
    method: 'get',
    params: query
  })
}

// 查询电路详细
export function getCircuit(id) {
  return request({
    url: '/configcenter/circuit/' + id,
    method: 'get'
  })
}

// 根据查询电路详细
export function getCircuitByCode(code) {
  return request({
    url: '/configcenter/circuit/getCircuitByCode/' + code,
    method: 'get'
  })
}

// 新增电路
export function addCircuit(data) {
  return request({
    url: '/configcenter/circuit',
    method: 'post',
    data: data
  })
}

// 修改电路
export function updateCircuit(data) {
  return request({
    url: '/configcenter/circuit',
    method: 'put',
    data: data
  })
}

// 删除电路
export function delCircuit(id) {
  return request({
    url: '/configcenter/circuit/' + id,
    method: 'delete'
  })
}

// 导出电路
export function exportCircuit(query) {
  return request({
    url: '/configcenter/circuit/export',
    method: 'get',
    params: query
  })
}

// 回路树
export function getCircuitTree() {
  return request({
    url: '/configcenter/circuit/circuitTreeselect',
    method: 'get',
  })
}
