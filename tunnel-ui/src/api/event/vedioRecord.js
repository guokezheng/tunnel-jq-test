import request from '@/utils/request'

// ��ѯ��ʷ��Ƶ��Ϣ�б�
export function listVediorecord(query) {
  return request({
    url: '/vediorecord/list',
    method: 'get',
    params: query
  })
}

// ��ѯ��ʷ��Ƶ��Ϣ��ϸ
export function getVediorecord(id) {
  return request({
    url: '/vediorecord/' + id,
    method: 'get'
  })
}

export function getVideoRecord(id) {
  return request({
    url: '/vediorecord/getVideos/' + id,
    method: 'get'
  })
}

// ������ʷ��Ƶ��Ϣ
export function addVediorecord(data) {
  return request({
    url: '/vediorecord',
    method: 'post',
    data: data
  })
}

// �޸���ʷ��Ƶ��Ϣ
export function updateVediorecord(data) {
  return request({
    url: '/vediorecord',
    method: 'put',
    data: data
  })
}

// ɾ����ʷ��Ƶ��Ϣ
export function delVediorecord(id) {
  return request({
    url: '/vediorecord/' + id,
    method: 'delete'
  })
}

export function getLocalIP() {
  return request({
    url: '/vediorecord/getLocalIP',
  })
}
