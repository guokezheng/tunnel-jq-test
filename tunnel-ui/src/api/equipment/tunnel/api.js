import request from '@/utils/request'

// 查询隧道列表
export function listTunnels1(query) {
  return request({
    url: '/tunnels/list1',
    method: 'get',
    params: query
  })
}


// 查询隧道列表
export function listTunnels(query) {
  return request({
    url: '/tunnels/list',
    method: 'get',
    params: query
  })
}
// 查询隧道列表
// export function listDepId(query) {
//   return request({
//     url: '/tunnels/listDepId',
//     method: 'get',
//     params: query
//   })
// }
export function listAllTunnels(query) {
  return request({
    url: '/tunnels/listAll',
    method: 'get',
    params: query
  })
}

export function listAllTunnels1(query) {
  return request({
    url: '/tunnels/listAll1',
    method: 'get',
    params: query
  })
}


// 查询隧道详细
export function getTunnels(tunnelId) {
  return request({
    url: '/tunnels/' + tunnelId,
    method: 'get'
  })
}

// 新增隧道
export function addTunnels(data) {
  return request({
    url: '/tunnels',
    method: 'post',
    data: data
  })
}

// 修改隧道
export function updateTunnels(data) {
  return request({
    url: '/tunnels',
    method: 'put',
    data: data
  })
}

// 删除隧道
export function delTunnels(tunnelId) {
  return request({
    url: '/tunnels/' + tunnelId,
    method: 'delete'
  })
}

// 导出隧道信息
export function exportTunnels(query) {
  return request({
    url: '/tunnels/exportTunnels',
    method: 'get',
    params: query
  })
}
//查询配置数据（测试）
export function getConfigData(tunnelId) {
  return request({
    url: '/workspace/getDevState/' + tunnelId,
    method: 'get'
  })
}

export function getStorageData(query) {
  return request({
    url: '/system/storage/getDevState',
    method: 'get',
    params: query
  })
}

//控制设备状态（测试）
export function setConfigData(data) {
  return request({
    url: '/workspace/controlDevice',
    method: 'post',
    params: data
  })
}

//控制隧道诱导设备
export function setCorLight(data) {
  return request({
    url: '/workspace/controlGuide',
    method: 'post',
    params: data
  })
}

//获取主机当前闪烁参数
export function getHostData(data) {
  return request({
    url: '/workspace/getHostData',
    method: 'post',
    params: data
  })
}
//点击工作台压力表图标 获取弹窗内数据
export function pressure(query) {
  return request({
    url: '/xfwater/record/getNumberOfPressureGaugesCollected',
    method: 'post',
    params: query
  })
}
export function sendAnalogCom(data) {
  return request({
    // url: '/sendMsg/storage', 连接中台测试用的
    url: '/system/storage',
    method: 'put',
    data: data
  })
}
// 查询单个设备的实时数据状态
export function getDevice(eqId) {
  return request({
    url: '/devices/getDevice/' + eqId,
    method: 'get'
  })
}

//  批量控制
export function updateCarFinger(query) {
  return request({
    url: '/workspace/updateCarFinger/',
    method: 'post',
    params: query
  })
}

export function getDeviceDataAndState(tunnelId) {
  return request({
    url: '/workspace/getDeviceDataAndState?tunnelId=' + tunnelId,
    method: 'get'
  })
}

// 疏散标志弹窗信息
export function fireMarkList(eqId) {
  return request({
    url: '/devices/fireMarkList/' + eqId,
    method: 'get'
  })
}

// 疏散标志弹窗信息
export function getJlyTunnel() {
  return request({
    url: '/tunnels/getJlyTunnel',
    method: 'get'
  })
}

// // 加强照明 弹窗
// export function setBrightness(query) {
//   return request({
//     url: '/light/setBrightness',
//     method: 'get',
//     params: query
//
//   })
// }

// 工作台 能耗图表
export function energyConsumptionDetection(tunneld) {
  return request({
    url: '/system/data/energyConsumptionDetection/' + tunneld,
    method: 'get'

  })
}

// 情报板 设备图标样式
export function getBoardContent(query) {
  return request({
    url: '/parser/board/getWorkBenchBoardContent/' + query,
    method: 'get',

  })
}

// 查询隧道列表
export function listDepId(query) {
  return request({
    url: '/tunnels/listDepId',
    method: 'get',
    params: query
  })
}
// 机器人实时状态
export function getWorkRobot(query) {
  return request({
    url: '/robot/getWorkRobot',
    method: 'get',
    params: query
  })
}