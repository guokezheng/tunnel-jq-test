import request, {download} from '@/utils/request'

// 查询设备档案管理列表
export function listComponent(query) {
  return request({
    url: '/component/list',
    method: 'get',
    params: query
  })
}

// 查询设备档案管理详细
export function getComponent(id) {
  return request({
    url: '/component/' + id,
    method: 'get'
  })
}

// 新增设备档案管理
export function addComponent(data) {
  return request({
    url: '/component',
    method: 'post',
    data: data
  })
}

// 下载预案文件
export function loadPlanFile(id, fileName) {
  return download(
    `/component/${id}`,
    {}, fileName
  )
}
export function addComponentFile(data) {
  return request({
    url: '/component/addSdComponent',
    method: 'post',
    data: data
  })
}


// 修改设备档案管理
export function updateComponent(data) {
  return request({
    url: '/component',
    method: 'put',
    data: data
  })
}
// 更新预案信息updateComponentFile
export function updateComponentFile(data) {
  return request({
    url: '/component/updateComponentPlan',
    method: 'post',
    data: data
  })
}

// 删除设备档案管理 id指文件id
export function delComponent(id) {
  return request({
    url: '/component/' + id,
    method: 'delete'
  })
}

// 删除设备档案管理 此id指设备档案id
export function delComponentById(cid) {
  return request({
    url: '/component/delbyid/' + cid,
    method: 'delete'
  })
}
