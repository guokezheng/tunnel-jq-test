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
    method: 'post',
    data: query
  })
}

// 废除巡查任务
export function abolishList(data) {
  return request({
    url: '/task/list/abolishSdTaskList',
    method: 'post',
    data: data
  })
}

// 获取设备table
export function getDevicesList(tunnelId,deviceType,pageNum,pageSize) {
  return request({
    url: '/task/list/getDevicesList?tunnelId='+tunnelId +'&deviceType=' + deviceType +'&pageNum=' + pageNum +'&pageSize=' +pageSize,
    method: 'get',
  })
}


// 获取故障table
export function getFaultList(tunnelId,faultLevel,pageNum,pageSize) {
  return request({
    url: '/task/list/getFaultList?tunnelId='+tunnelId +'&faultLevel=' + faultLevel +'&pageNum=' + pageNum +'&pageSize=' +pageSize,
    method: 'get',
  })
}

//新增    发布任务
export function addTask(data) {
  return request({
    url: '/task/list/addTask',
    method: 'post',
    data: data
  })
}

//修改  发布任务
export function updateTask(data) {
  return request({
    url: '/task/list/updateTask',
    method: 'post',
    data: data
  })
}


//根据隧道信息查询对应的班组
export function selectBzByTunnel(data) {
  return request({
    url: '/task/list/selectBzByTunnel',
    method: 'post',
    data: data
  })
}

//查询操作记录
/*export function getTaskOpt(taskId) {
  return request({
    url: '/task/list/getTaskOpt',
    method: 'post',
    data: data
  })
}*/
