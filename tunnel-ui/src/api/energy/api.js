import request from '@/utils/request'

// 左侧机构树
export function siteTree() {
    return request({
      url: '/energy/analysis/siteTree',
      method: 'post',
    })
  }

  // 分项树
export function itemizedTreeselect() {
    return request({
      url: '/energy/analysis/itemizedTreeselect',
      method: 'get'
    })
  }

    // 分类树
export function classificationTreeselect() {
    return request({
      url: '/energy/analysis/classificationTreeselect',
      method: 'get'
    })
  }

      // 用能报表
export function getElectricityReportList(params) {
    return request({
      url: '/analysis/getElectricityReportList?codeList=' + params.codeList +'&baseTime=' + params.baseTime + '&type=' + params.type + '&tabType=' + params.tabType + '&deptCode=' + params.deptCode,
      method: 'get'
    })
  }

        // 能耗足迹
export function getEnergyTrackList(params) {
    return request({
      url: '/analysis/getEnergyTrackList?deptCodeList=' + params.deptCodeList + '&baseTime=' + params.baseTime + '&type=' + params.type,
      method: 'get'
    })
  }

      // 分时段用能
export function getSplitTimeByDept(params) {
  return request({
    url: '/analysis/getSplitTimeByDept',
    method: 'get',
    params: params
  })
}

// 电费分析
export function getElectricityBillByDept(params) {
  return request({
    url: '/analysis/getElectricityBillByDept',
    method: 'get',
    params: params
  })
}

// 查询时段用电柱状图
export function selectEnergyAnalysisElectricityBillList(params) {
  return request({
    url: '/energyAnalysisElectricityBillController/selectEnergyAnalysisElectricityBillList',
    method: 'get',
    params: params
  })
}

// 查询时段用电柱状图
export function getEnergySiteList(params) {
  return request({
    url: '/energySiteController/getEnergySiteList',
    method: 'get',
    params: params
  })
}

// 累计数据展示
export function getAccumulated(params) {
  return request({
    url: '/energyAnalysisElectricityBillController/selectEnergyValueSum',
    method: 'get',
    params: params
  })
}
// 实时数据展示
export function getrealTimeData(params) {
  return request({
    url: '/energyAnalysisElectricityBillController/getrealTimeData',
    method: 'get',
    params: params
  })
}

//获取零碳大屏url
export function getZeroUrl(params){
  return request({
    url: '/zeroCarBon/getZeroUrl',
    method: 'get',
    params: params
  })
}
