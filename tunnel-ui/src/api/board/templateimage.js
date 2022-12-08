/*
 * @Author: your name
 * @Date: 2022-03-28 14:13:59
 * @LastEditTime: 2022-03-29 15:10:33
 * @LastEditors: Please set LastEditors
 * @Description: 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 * @FilePath: \TunnelPlatform-V3\src\api\board\templateimage.js
 */
import request from '@/utils/request'

// 查询情报板模板图片列表
export function getTemplateImageList(query) {
    return request({
      url: '/system/upload/list',
      method: 'get',
      params: query
    })
}


// 获取详细信息
export function getTemplateImageInfo(id) {
    return request({
      url: '/system/upload/'+id,
      method: 'get',
    })
}

// 新增
export function addTemplateImage(data) {
  return request({
    url: '/system/upload',
    method: 'post',
    data: data
  })
}

  // 修改
export function editTemplateImage(data) {
    return request({
        url: '/system/upload',
        method: 'put',
        data: data
    })
}

// 删除情报板模板
export function deleteTemplateImage(id) {
    return request({
        url: '/system/upload/' + id,
        method: 'delete'
    })
}

// 导出情报板图片
export function exportTemplateImage(query) {
  return request({
    url: '/system/upload/export',
    method: 'get',
    params: query
  })
}
