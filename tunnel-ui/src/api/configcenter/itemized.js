import request from '@/utils/request'
import method from "vue-resource/src/http/interceptor/method";

// 查询分项列表
export function listItemized(query) {
  return request({
    url: '/configcenter/itemized/list',
    method: 'get',
    params: query
  })
}

// 查询分项详细
export function getItemized(id) {
  return request({
    url: '/configcenter/itemized/' + id,
    method: 'get'
  })
}

//根据编号查询getInfoByCode
export function getItemizedByCode(code){
  return request({
    url: '/configcenter/itemized/getInfoByCode/' + code,
    method:'get'
  })
}

// 新增分项
export function addItemized(data) {
  return request({
    url: '/configcenter/itemized',
    method: 'post',
    data: data
  })
}

// 修改分项
export function updateItemized(data) {
  return request({
    url: '/configcenter/itemized',
    method: 'put',
    data: data
  })
}

// 删除分项
export function delItemized(id) {
  return request({
    url: '/configcenter/itemized/' + id,
    method: 'delete'
  })
}

// 导出分项
export function exportItemized(query) {
  return request({
    url: '/configcenter/itemized/export',
    method: 'get',
    params: query
  })
}

// 分项树
export function getItemizedTree() {
  return request({
    url: '/configcenter/itemized/itemizedTreeselect',
    method: 'get',
  })
}
