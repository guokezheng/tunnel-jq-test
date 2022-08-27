import request from '@/utils/request'

// ��ѯ���Ʋ����б�
export function listStrategy(query) {
  return request({
    url: '/strategy/list',
    method: 'get',
    params: query
  })
}

// ��ѯ���Ʋ�����ϸ
export function getStrategy(id) {
  return request({
    url: '/strategy/' + id,
    method: 'get'
  })
}

// �������Ʋ���
export function addStrategy(data) {
  return request({
    url: '/strategy',
    method: 'post',
    data: data
  })
}

// �޸Ŀ��Ʋ���
export function updateStrategy(data) {
  return request({
    url: '/strategy',
    method: 'put',
    data: data
  })
}

// ɾ�����Ʋ���
export function delStrategy(id) {
  return request({
    url: '/strategy/' + id,
    method: 'delete'
  })
}
// �������Ʋ���
export function addStrategysInfo(data) {
  return request({
    url: '/strategy/addStrategysInfo',
    method: 'post',
    data: data
  })
}
// �޸Ŀ��Ʋ���
export function updateStrategysInfo(data) {
  return request({
    url: '/strategy/updateStrategysInfo',
    method: 'post',
    data: data
  })
}

// ��ȡguid
export function getGuid() {
  return request({
    url: '/strategy/getGuid',
    method: 'get'
  })
}

// �ֶ����Ʋ���
export function handleStrategy(id) {
  return request({
    url: '/strategy/handleStrategy/' + id,
    method: 'get'
  })
}