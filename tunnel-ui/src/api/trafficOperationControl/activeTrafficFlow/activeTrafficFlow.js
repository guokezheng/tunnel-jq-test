import request from '@/utils/request'

//获取主动交通流推送措施
export function getActiveTrafficMeasure(id){
  return request({
    url: '/activeTrafficFlow/getActiveTrafficMeasure/'+id,
    method: 'get',
    // params: query
  })
}

//根据隧道id获取主动交通流推送措施
export function getActiveTrafficMeasureByTunnelId(query){
  return request({
    url: '/activeTrafficFlow/getActiveTrafficMeasureByTunnelId',
    method: 'get',
    params: query
  })
}


//根据管控类别、管控等级查询配置措施
export function getControlMeasureByTypeLevel(param){
  return request({
    url: '/controlConfig/config/getControlMeasureByTypeLevel',
    method: 'get',
    params: param
  })
}

//根据事件类型、事件级别获取主动交通流推送措施
export function getActiveMeasureWithEmergencyIncident(param){
  return request({
    url: '/activeTrafficFlow/getActiveMeasureWithEmergencyIncident',
    method: 'get',
    params: param
  })
}
