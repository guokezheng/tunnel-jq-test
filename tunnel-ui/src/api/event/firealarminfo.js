import request from '@/utils/request'

// ��ѯ����Ϣ�б�
export function listFirealarminfo(query) {
  return request({
    url: '/firealarminfo/list',
    method: 'get',
    params: query
  })
}

// ��ѯ����Ϣ��ϸ
export function getFirealarminfo(id) {
  return request({
    url: '/firealarminfo/' + id,
    method: 'get'
  })
}

// ��������Ϣ
export function addFirealarminfo(data) {
  return request({
    url: '/firealarminfo',
    method: 'post',
    data: data
  })
}

// �޸Ļ���Ϣ
export function updateFirealarminfo(data) {
  return request({
    url: '/firealarminfo',
    method: 'put',
    data: data
  })
}

// ɾ������Ϣ
export function delFirealarminfo(id) {
  return request({
    url: '/firealarminfo/' + id,
    method: 'delete'
  })
}