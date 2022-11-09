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