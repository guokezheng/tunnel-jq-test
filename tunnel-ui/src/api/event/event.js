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