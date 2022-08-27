import request from '@/utils/request'

// ��ѯԤ�������б�
export function listPlanType(query) {
  return request({
    url: '/planType/list',
    method: 'get',
    params: query
  })
}

// ��ѯԤ��������ϸ
export function getPlanType(id) {
  return request({
    url: '/planType/' + id,
    method: 'get'
  })
}

// ����Ԥ������
export function addPlanType(data) {
  return request({
    url: '/planType',
    method: 'post',
    data: data
  })
}

// �޸�Ԥ������
export function updatePlanType(data) {
  return request({
    url: '/planType',
    method: 'put',
    data: data
  })
}

// ɾ��Ԥ������
export function delPlanType(id) {
  return request({
    url: '/planType/' + id,
    method: 'delete'
  })
}
