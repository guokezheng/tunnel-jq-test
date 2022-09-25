/*
 * @Author: Praise-Sun 18053314396@163.com
 * @Date: 2022-09-07 22:06:50
 * @LastEditors: Praise-Sun 18053314396@163.com
 * @LastEditTime: 2022-09-25 11:38:06
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

// ��ѯ�¼�������ϸ
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
