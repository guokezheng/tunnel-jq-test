import request from '@/utils/requestThree'
// 全景数据 - 运行环境接口


// 查看环境监测状态（水侵、烟感、声光报警、气体）
export function getToken() {
  return request({
    url: '/getToken',
    method: 'get',
  })
}

// 查看环境监测状态（水侵、烟感、声光报警、气体）
export function getTypeListApi(params) {
  return request({
    url: 'environmentalMonitoring/homePage/selectEquipmentStatus',
    method: 'get',
    params
  })
}
// （水侵、烟感、声光报警、气体） 点击详情
export function getTypeListXqApi(params) {
  return request({
    url: 'environmentalMonitoring/homePage/selectEquipmentDetailed',
    method: 'get',
    params
  })
}
// 获取温湿度设备
export function getSelectListEqApi(params) {
  return request({
    url: 'environmentalMonitoring/homePage/selectDevices',
    method: 'get',
    params
  })
}
// 获取温湿度数据
export function getSelectListDataEqApi(params) {
  return request({
    url: 'environmentalMonitoring/homePage/getRealDataByDeviceCode',
    method: 'get',
    params
  })
}
// 获取温湿度数据
export function getSelectEChartsApi(params) {
  return request({
    url: 'environmentalMonitoring/homePage/temperatureAndHumidityEcharts',
    method: 'get',
    params
  })
}
// 修改模式
export function fixIndustryModelApi(data) {
  return request({
    url: 'environmentalMonitoring/homePage/remoteControlCommand',
    method: 'post',
    data
  })
}
// 获取监测摄像头url
export function getCameraListApi(params) {
  return request({
    url: 'environmentalMonitoring/homePage/getStream',
    method: 'get',
    params
  })
}
// 获取监测摄像头url
export function getAllPowerDataInfoApi(params) {
  return request({
    url: 'power/getAllPowerDataInfo',
    method: 'get',
    params
  })
}
// 下载日原始数据
export function downloadExcelApi(params) {
  return request({
    url: 'power/exportPowerTable',
    method: 'get',
    params,
    responseType: "blob"
  })
}

// 获取逐日数据
export function getZhuRiEChartsApi(params) {
  return request({
    url: 'power/getDayAllInfo',
    method: 'get',
    params
  })
}
// 逐日 excel 下载
export function downloadZhuRiExcelApi(params) {
  return request({
    url: 'power/exportPowerTableDay',
    method: 'get',
    params
  })
}
// 变压器 绕组温度 eCharts和表格数据
export function getWindingEChartsApi(params) {
  return request({
    url: 'power/getWindingTemperature',
    method: 'get',
    params
  })
}

