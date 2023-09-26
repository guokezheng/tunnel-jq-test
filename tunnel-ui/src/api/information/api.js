import request from '@/utils/request'

// 查询隧道列表
export function listTunnels(deptId) {
    return request({
      url: '/tunnels/list?deptId=' + deptId,
      method: 'get',
    })
  }


  // 查询位置信息
export function devicessize(query) {
    return request({
      url: '/information/getdevicessize',
      method: 'get',
      params: query

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

// 查询情报板设备列表
export function getIotBoardList(query) {
  return request({
    url: '/information/getIotBoardList',
    method: 'get',
    params: query
  })
}
// 预案查询情报板模板
export function getVmsTemplateList(query) {
  return request({
    url: '/system/template/getVmsTemplateList',
    method: 'get',
    params: query
  })
}
// 查询当前情报板信息

export function selectVmsContent(query) {
  return request({
    url: '/plan/process/selectVmsContent',
    method: 'get',
    params: query
  })
}


// 9.23 情报板回读、关闭
export function getBoardContentData(query) {
  return request({
    url: '/parser/board/getBoardContentData',
    method: 'get',
    params: query
  })
}