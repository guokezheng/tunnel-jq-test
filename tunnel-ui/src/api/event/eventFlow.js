/*
 * @Author: Praise-Sun 18053314396@163.com
 * @Date: 2022-09-05 09:35:35
 * @LastEditors: Praise-Sun 18053314396@163.com
 * @LastEditTime: 2022-09-25 16:08:16
 * @FilePath: \tunnel-ui\src\api\event\eventFlow.js
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
import request from '@/utils/request'

// ��ѯ�¼����������б�
export function listEventFlow(query) {
  return request({
    url: '/eventFlow/list',
    method: 'get',
    params: query
  })
}

// ��ѯ�¼�����������ϸ
export function getEventFlow(id) {
  return request({
    url: '/eventFlow/' + id,
    method: 'get'
  })
}

// �����¼���������
export function addEventFlow(data) {
  return request({
    url: '/eventFlow',
    method: 'post',
    data: data
  })
}

// �޸��¼���������
export function updateEventFlow(data) {
  return request({
    url: '/eventFlow',
    method: 'put',
    data: data
  })
}

// ɾ���¼���������
export function delEventFlow(id) {
  return request({
    url: '/eventFlow/' + id,
    method: 'delete',
  })
}
// 应急调度恢复预案接口
export function getListBySId(query) {
  return request({
    url: '/plan/getListBySId',
    method: 'get',
    params: query
  })
}
