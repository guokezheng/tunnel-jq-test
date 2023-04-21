import request from '@/utils/request'

// 查询
export function handleQueryList(query) {
    return request({
        url: '/system/vehicle/list',
        method: 'get',
        params: query
    })
}

// 新增
export function addList(query) {
    return request({
        url: '/system/vehicle',
        method: 'post',
        params: query
    })
}

//删除
export function deleteForm(ids) {
    return request({
        url: '/system/vehicle' +'/'+ ids,
        method: 'delete',
    })
}

//点击修改的回显
export function editForm(id) {
    return request({
        url: '/system/vehicle' + '/'+id,
        method: 'get',
    })
}

//点击详情的回显
export function detailForm(data) {
  return request({
    url: '/system/vehicle/getVehicleDetails',
    method: 'get',
    params: data
  })
}

//修改的提交
export function updateForm(data) {
    return request({
        url: '/system/vehicle',
        method: 'put',
        data
    })
}

//导出
export function exportData(data) {
    return request({
        url: '/system/vehicle/export',
        method: 'get',
        params: data
    })
}
// 机构查询
export function veicleOrgId() {
    return request({
        url: '/system/vehicle/getOrg',
        method: 'get',
    })
}

// 应急车辆同步
export function syncVehicle() {
  return request({
    url: '/system/vehicle/syncVehicle',
    method: 'get',
  })
}
