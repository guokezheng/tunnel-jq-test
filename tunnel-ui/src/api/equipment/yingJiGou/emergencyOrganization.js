import request from '@/utils/request'

// 查询
export function handleQueryList(query) {
    return request({
        url: '/system/org/list',
        method: 'get',
        params: query
    })
}

// 新增
export function addList(query) {
    return request({
        url: '/system/org',
        method: 'post',
        params: query
    })
}

//点击修改的回显
export function editForm(orgId) {
    return request({
        url: '/system/org' + '/'+orgId,
        method: 'get',
    })
}
//修改的提交
export function updateForm(data) {
    return request({
        url: '/system/org',
        method: 'put',
        data
    })
}
//删除
export function deleteForm(orgIds) {
    return request({
        url: '/system/org' +'/'+ orgIds,
        method: 'delete',
    })
}
//导出
export function exportData(data) {
    return request({
        url: '/system/org/export',
        method: 'get',
        data
    })
}