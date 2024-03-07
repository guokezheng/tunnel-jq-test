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

// 获取事件详情
export function getEventDetail(query) {
  return request({
    url: '/event/getEventDetail/',
    method: 'get',
    params: query
  })
}

//获取事件处置记录
export function getDisposalRecord(query) {
  return request({
    url: '/eventFlow/list',
    method: 'get',
    params: query
  })
}

// 获取预案设备列表
export function getManagementDevice(query) {
  return request({
    url: '/event/getManagementDevice',
    method: 'get',
    params: query
  })
}
export function handleStrategy(id) {
  return request({
    url: '/strategy/handleStrategy/' + id,
    method: 'get'
  })
}

export function updateAudio(query) {
  return request({
    url: '/eventType/updateSdEventTypeAudio',
    method: 'get',
    params: query
  })
}