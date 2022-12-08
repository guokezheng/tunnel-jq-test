import request from '@/utils/request'

// 查询隧道列表
export function listTunnels(deptId) {
    return request({
      url: '/tunnels/list?deptId=' + deptId,
      method: 'get',
    })
  }


  // 查询位置信息
export function devicessize() {
    return request({
      url: '/information/getdevicessize',
      method: 'get',
    })
  }

    // 查设备多选框
export function information(query) {
  return request({
    url: '/information/list',
    method: 'get',
    params: query
  })
}

// 左侧多选框
export function getBoardInfo(deviceId) {
  return request({
    url: '/parser/board/getBoardInfo?deviceId='+deviceId,
    method: 'get',
  })
}

// 左侧多选框
export function getBoardEditInfo(deviceId) {
  return request({
    url: '/parser/board/getBoardEditInfo?deviceId='+deviceId,
    method: 'get',
  })
}
