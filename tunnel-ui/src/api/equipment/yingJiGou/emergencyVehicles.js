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
        data
    })
}
// 机构查询
export function veicleOrgId() {
    return request({
        url: '/system/vehicle/getOrg',
        method: 'get',        
    })
}