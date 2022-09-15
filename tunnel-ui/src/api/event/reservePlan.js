import request, {download} from '@/utils/request'

// 查询预案信息列表
export function listPlan(query) {
  return request({
    url: '/plan/list',
    method: 'get',
    params: query
  })
}

// 查询预案信息详细
export function getPlan(id) {
  return request({
    url: '/plan/' + id,
    method: 'get'
  })
}

// 新增预案信息
export function addPlan(data) {
  return request({
    url: '/plan',
    method: 'post',
    data: data
  })
}

// 修改预案信息
export function updatePlan(data) {
  return request({
    url: '/plan',
    method: 'put',
    data: data
  })
}

// 删除预案信息
export function delPlan(id) {
  return request({
    url: '/plan/' + id,
    method: 'delete'
  })
}
// 新增预案信息
export function addPlanFile(data) {
  return request({
    url: '/plan/addReservePlan',
    method: 'post',
    data: data
  })
}

// 下载预案文件
export function loadPlanFile(id, fileName) {
  return download(
    `/plan/${id}`,
    {}, fileName
  )
}
// 更新预案信息
export function updatePlanFile(data) {
  return request({
    url: '/plan/updateReservePlan',
    method: 'post',
    data: data
  })
}
// 根据预案id，查询策略信息列表
export function listStrategyByPlanId(id) {
  return request({
    url: '/plan/s/' + id,
    method: 'get',
  })
}

export function tunnelNames() {
  return request({
    url:'/tunnels/sublist',
    method: 'get',
  })
}

export function getPlanType() {
  return request({
    url:'/plan/getPlanCateGory',
    method: 'get',
  })
}
