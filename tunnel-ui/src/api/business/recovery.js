/*
 * @Author: your name
 * @Date: 2022-02-22 14:08:00
 * @LastEditTime: 2022-02-23 11:55:58
 * @LastEditors: Please set LastEditors
 * @Description: 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 * @FilePath: \TunnelPlatform-V3\src\api\business\recovery.js
 */
import request from '@/utils/request'
// 查询应急恢复列表
export function listRecovery(query) {
    return request({
      url: '/dataInfo/recovery/list',
      method: 'get',
      params: query
    })
}

// 修改
export function updateRecover(data) {
  return request({
    url: '/dataInfo/recovery',
    method: 'put',
    data: data
  })
}
// 控制复原
export function controlRecovery(data) {
  return request({
    url: '/dataInfo/recovery/controlRecovery',
    method: 'get',
    params: data
  })
}

