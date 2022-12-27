
import request from "@/utils/request";
// 获取设备历史数据详细信息
export function selectIotDeviceHistoryByCode(params) {
  return request({
    url: '/configcenter/iotDeviceHistory/selectIotDeviceHistoryByCode',
    method: 'get',
    params: params
  })
}
