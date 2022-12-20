import request from '@/utils/request'

// ��ѯԤ���ļ��б�
export function listReservePlanFile(query) {
  return request({
    url: '/reservePlanFile/list',
    method: 'get',
    params: query
  })
}

// ��ѯԤ���ļ���ϸ
export function getReservePlanFile(id) {
  return request({
    url: '/reservePlanFile/' + id,
    method: 'get'
  })
}

// ����Ԥ���ļ�
export function addReservePlanFile(data) {
  return request({
    url: '/reservePlanFile',
    method: 'post',
    data: data
  })
}

// �޸�Ԥ���ļ�
export function updateReservePlanFile(data) {
  return request({
    url: '/reservePlanFile',
    method: 'put',
    data: data
  })
}

// ɾ��Ԥ���ļ�
export function delReservePlanFile(id) {
  return request({
    url: '/reservePlanFile/' + id,
    method: 'delete'
  })
}
// 47
export function getReservePlanProcess(id) {
  return request({
    url: 'plan/process/getReservePlanProcess/' + id,
    method: 'get'
  })
}
