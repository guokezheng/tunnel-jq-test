
import request from '@/utils/request'

// 查询情报板模板列表
export function getTemplates(query) {
  return request({
    url: '/system/template/list',
    method: 'get',
    params: query
  })
}

//获取情报板模板详细信息
export function getTemplateInfo(id) {
  return request({
    url: '/system/template/' + id,
    method: 'get',
  })
}

//获取情报板模板内容信息
export function getTemplateContent(id) {
  return request({
    url: '/system/content/list?templateId=' + id,
    method: 'get',
  })
}
// 新增情报板模板
export function addTemplate(data) {
  return request({
    url: '/system/template',
    method: 'post',
    data: data
  })
}

//新增情报板模板内容
export function addTemplateContent(data) {
  return request({
    url: '/system/content',
    method: 'post',
    data: data
  })
}
//修改情报板模板内容
export function editTemplateContent(data) {
  return request({
    url: '/system/content',
    method: 'put',
    data: data
  })
}

//修改情报板模板
export function editTemplate(data) {
  return request({
    url: '/system/template',
    method: 'put',
    data: data
  })
}

// 删除情报板模板
export function deleteTemplate(id) {
  return request({
    url: '/system/template/' + id,
    method: 'delete'
  })
}


//图片库列表查询
export function getGalleryList() {
  return request({
    url: '/system/upload/list',
    method: 'get',
  })
}

//图片获取详细信息
export function getGallery(id) {
  return request({
    url: '/system/upload/'+ id,
    method: 'get',
  })
}


// 情报板发布
export function postInformationBoardRelease(data) {
  return request({
    url: '/system/template/informationBoardRelease',
    method: 'post',
    data: data
  })
}

// 情报板获取
export function getInformationBoardRelease(data) {
  return request({
    url: '/system/template/informationBoardAcquisition',
    method: 'post',
    data: data
  })
}

//情报板发布
export function uploadBoardEditInfo(devicelds,protocolType,parameters) {
  var data = {
    deviceIds:devicelds,
    protocolType:protocolType,
    parameters:encodeURI(parameters)

  }
  return request({
    url: '/parser/board/uploadBoardEditInfo',
    method: 'get',
    params: data
  })
}



//情报板管理右侧查询接口
export function getAllVmsTemplate(data) {
  return request({
    url: '/system/template/getAllVmsTemplate',
    method: 'get',
    params: data

  })
}

// 情报板内容查询
export function getBoardContent(deviceId) {
  return request({
    url: '/parser/board/getBoardContent?deviceId=' + deviceId,
    method: 'get',

  })
}