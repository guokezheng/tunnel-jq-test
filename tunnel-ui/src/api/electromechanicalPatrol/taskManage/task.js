import request from '@/utils/request'
import {listTunnels} from "@/api/equipment/tunnel/api";

// 查询巡查任务列表
export function listList(query) {
  return request({
    url: '/task/list/list',
    method: 'get',
    params: query
  })
}

// 查询巡查任务详细
export function getList(id) {
  return request({
    url: '/task/list/' + id,
    method: 'get'
  })
}

// 新增巡查任务
export function addList(data) {
  return request({
    url: '/task/list',
    method: 'post',
    data: data
  })
}

// 修改巡查任务
export function updateList(data) {
  return request({
    url: '/task/list',
    method: 'put',
    data: data
  })
}

// 删除巡查任务
export function delList(id) {
  return request({
    url: '/task/list/' + id,
    method: 'delete'
  })
}

// 导出巡查任务
export function exportList(query) {
  return request({
    url: '/task/list/export',
    method: 'get',
    params: query
  })
}

// 任务详情
export function getTaskInfoList(data) {
  return request({
    url: '/task/list/getTaskInfoList',
    method: 'post',
    data: data
  })
}

// 巡查班组
export function listBz(query) {
  return request({
    url: '/task/list/getListBz',
    method: 'get',
    params: query
  })
}


// 部门隧道树
export function treeselect(query) {
  return request({
    url: '/task/list/treeselect',
    method: 'get',
    params: query
  })
}

// 获取设备类型下拉
export function getDevicesTypeList(data) {
  return request({
    url: '/task/list/getDevicesTypeList',
    method: 'post',
    data: data
  })
}