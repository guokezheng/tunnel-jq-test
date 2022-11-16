/*
 * @Author: Praise-Sun 18053314396@163.com
 * @Date: 2022-11-16 10:28:09
 * @LastEditors: Praise-Sun 18053314396@163.com
 * @LastEditTime: 2022-11-16 10:38:17
 * @FilePath: \tunnel-ui\src\api\business\new.js
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
import request from '@/utils/request'

// 设备状态
export function getEquipmentStatus() {
    return request({
        url: '/proportionOfEquipment/getEquipmentStatus',
        method: 'get',
    })
}