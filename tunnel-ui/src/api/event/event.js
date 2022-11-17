/*
 * @Author: Praise-Sun 18053314396@163.com
 * @Date: 2022-09-07 22:06:50
 * @LastEditors: Praise-Sun 18053314396@163.com
 * @LastEditTime: 2022-10-20 11:05:43
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
export function performRecovery(eventId) {
  return request({
    url: '/event/performRecovery?eventId='+ eventId,
    method: 'get',
  })
}


// 一进页面获取已执行数据
export function dispatchExecuted(eventId) {
  return request({
    url: '/log/dispatchExecuted?eventId='+ eventId,
    method: 'get',
  })
}

// 事件弹窗分类数组
export function eventList(searchValue,pageNum,startTime) {
  return request({
    url: searchValue == 3?'/event/list?remark=pop&pageNum='+pageNum +'&pageSize=10&startTime='+startTime:'/event/list?searchValue='+searchValue+'&remark=pop&pageNum='+pageNum +'&pageSize=10$startTime='+startTime,
    method: 'get',
  })
}

// 事件弹窗分类数组 全部
export function eventPopAll() {
  return request({
    url: '/event/eventPopAll',
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
