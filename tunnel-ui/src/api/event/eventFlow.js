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
    method: 'delete'
  })
}