import request from '@/utils/request'

// ��ѯ�¼������б�
export function listEventType(query) {
  return request({
    url: '/eventType/list',
    method: 'get',
    params: query
  })
}

// ��ѯ�¼�������ϸ
export function getEventType(id) {
  return request({
    url: '/eventType/' + id,
    method: 'get'
  })
}

// �����¼�����
export function addEventType(data) {
  return request({
    url: '/eventType',
    method: 'post',
    data: data
  })
}

// �޸��¼�����
export function updateEventType(data) {
  return request({
    url: '/eventType',
    method: 'put',
    data: data
  })
}

// ɾ���¼�����
export function delEventType(id) {
  return request({
    url: '/eventType/' + id,
    method: 'delete'
  })
}
export function getTodayEventCount() {
  return request({
    url: '/event/getTodayEventCount',
    method: 'get'
  })
}
