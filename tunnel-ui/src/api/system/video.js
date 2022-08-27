import request from '@/utils/request'


// 查询video列表
export function listVideo(query) {
  return request({
    url: '/video/list',
    method: 'get',
    params: query
  })
}

// 查询部门下拉树结构
export function treeselect() {
  return request({
    url: '/video/treeselect',
    method: 'get'
  })
}


