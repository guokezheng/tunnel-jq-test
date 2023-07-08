import request from '@/utils/request'

// 查询plc 报文列表
export function listCmd(query) {
  return request({
    url: '/cmd/list',
    method: 'get',
    params: query
  })
}

// 查询plc 报文详细
export function getCmd(cmdId) {
  return request({
    url: '/cmd/' + cmdId,
    method: 'get'
  })
}

// 新增plc 报文
export function addCmd(data) {
  return request({
    url: '/cmd',
    method: 'post',
    data: data
  })
}

// 修改plc 报文
export function updateCmd(data) {
  // debugger
  return request({
    url: '/cmd',
    method: 'put',
    data: data
  })
}

// 删除plc 报文
export function delCmd(cmdId) {
  return request({
    url: '/cmd/' + cmdId,
    method: 'delete'
  })
}
