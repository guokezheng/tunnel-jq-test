/*
 * @Author: Praise-Sun 18053314396@163.com
 * @Date: 2022-09-07 22:06:50
 * @LastEditors: Praise-Sun 18053314396@163.com
 * @LastEditTime: 2022-11-17 16:27:03
 * @FilePath: \tunnel-ui\src\api\event\event.js
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
import request from '@/utils/request'

// ��ѯ�¼������б�
export function listEvent(query) {
  return request({
    url: '/event/list',
    method: 'get',
    params: query
  })
}

// 获取事件详情
export function getEvent(id) {
  return request({
    url: '/event/' + id,
    method: 'get'
  })
}

// �����¼�����
export function addEvent(data) {
  return request({
    url: '/event',
    method: 'post',
    data: data
  })
}

// �޸��¼�����
export function updateEvent(data) {
  return request({
    url: '/event',
    method: 'put',
    data: data
  })
}

// ɾ���¼�����
export function delEvent(id) {
  return request({
    url: '/event/' + id,
    method: 'delete'
  })
}

// ��������
export function toll() {
  return request({
    url: '/system/dept/toll',
    method: 'get'
  })
}
// ��������
export function getTunnelList(query) {
  return request({
    url: '/tunnels/deptId',
    method: 'get',
    params: query
  })
}

// ��ѯ����
export function listDevices(query) {
  return request({
    url: '/devices/list',
    method: 'get',
    params: query
  })
}

// ��ѯ����
export function getWarnEvent(query) {
  return request({
    url: '/event/getEvent',
    method: 'get',
    params: query
  })
}

//执行预案eventId:'',reserveId:
export function getImplement(query) {
  return request({
    url: '/plan/process/implement',
    method: 'Post',
    data: query
  })
}

// 获取事故点

export function getSubareaByStakeNum(query) {
  return request({
    url: '/event/getSubareaByStakeNum',
    method: 'get',
    params: query
  })
}

// 一键恢复
// export function performRecovery(eventId) {
//   return request({
//     url: '/event/performRecovery?eventId=' + eventId,
//     method: 'get',
//   })
// }


// 一进页面获取已执行数据
export function dispatchExecuted(eventId) {
  return request({
    url: '/log/dispatchExecuted?eventId=' + eventId,
    method: 'get',
  })
}

// 事件弹窗分类数组
export function eventList(searchValue, pageNum, startTime) {
  return request({
    // url: searchValue == 3?'/event/list?remark=pop&pageNum='+pageNum +'&pageSize=10&startTime='+startTime:'/event/list?searchValue='+searchValue+'&remark=pop&pageNum='+pageNum +'&pageSize=10$startTime='+startTime,
    url: '/event/list?searchValue=' + searchValue + '&remark=pop&pageNum=' + pageNum + '&pageSize=10&startTime=' + startTime,
    method: 'get',
  })
}

// 事件弹窗分类数组 全部
export function eventPopAll(pageNum) {
  return request({
    url: '/event/eventPopAll?subIndex=' + pageNum + ',10',
    method: 'get',
  })
}
// 事件 获取总条数
export function getEventUntreatedNum() {
  return request({
    url: '/event/getEventUntreatedNum',
    method: 'get',
  })
}

// 设备故障
export function eventPopFault(pageNum) {
  return request({
    url: '/event/eventPopFault?subIndex=' + pageNum + ',10',
    method: 'get',
  })
}

// 获取车道数
export function getTunnelLane(tunnelId) {
  return request({
    url: '/tunnels/' + tunnelId,
    method: 'get',
  })
}

//  处置记录
export function eventFlowList(query) {
  return request({
    url: '/eventFlow/list',
    method: 'get',
    params: query

  })
}

// 事件处置
export function getHandle(query) {
  return request({
    url: '/event/getHandle',
    method: 'get',
    params: query

  })
}

// 事件处置 下发（改状态）
export function updateHandle(query) {
  return request({
    url: '/event/updateHandle',
    method: 'get',
    params: query
  })
}

// 关联事件 
export function getRelation(query) {
  return request({
    url: '/event/getRelation',
    method: 'get',
    params: query
  })
}

// 关联事件 
export function getAccidentPoint(query) {
  return request({
    url: '/event/getAccidentPoint',
    method: 'get',
    params: query
  })
}

// 一键 下发设备
export function implementPlan(planId,eventId) {
  return request({
    url: '/plan/implementPlan?planId='+planId+'&eventId='+eventId,
    method: 'get',
  })
}

// 单点 下发设备
export function implementProcess(processId,eventId) {
  return request({
    url: '/plan/implementProcess?processId='+ processId + '&eventId=' + eventId,
    method: 'get',
  })
}

// 一键 下发设备 交通事件
export function performRecovery(eventId,handleId) {
  return request({
    url: '/event/performRecovery?eventId='+eventId+'&handleId='+handleId,
    method: 'get',
  })
}


// 主动安全 处置
export function getSafetyHandle(query) {
  return request({
    url: '/event/getSafetyHandle',
    method: 'get',
    params: query

  })
}

// 主动安全 一键
export function implementDisposalStrategy(eventId,strategyId) {
  return request({
    url: '/strategy/implementDisposalStrategy?eventId='+eventId+'&strategyId='+strategyId,
    method: 'get',
  })
}

// 主动安全 单点下发
export function implementDisposalStrategyRl(eventId,rlId) {
  return request({
    url: '/strategy/implementDisposalStrategyRl?eventId='+eventId+'&rlId='+rlId,
    method: 'get',
  })
}


