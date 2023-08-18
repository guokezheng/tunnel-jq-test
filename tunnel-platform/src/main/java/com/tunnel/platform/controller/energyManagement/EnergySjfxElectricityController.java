package com.tunnel.platform.controller.energyManagement;


import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.StringUtils;
import com.tunnel.business.datacenter.domain.enumeration.StatisticTypeEnum;
import com.tunnel.business.domain.energyManagement.EnergySjfx;
import com.tunnel.business.service.energyManagement.EnergySjfxElectricityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 用电统计Controller
 *
 * @author ruoyi
 * @date 2023-07-17
 */

@RequestMapping("/analysis")
@RestController
public class EnergySjfxElectricityController extends BaseController {

    @Autowired
    private EnergySjfxElectricityService energySjfxElectricityService;



    /**
     * 用能报表
     *
     * @param codeList 电路code列表
     * @param baseTime 基础日期
     * @param type     报表类型 （日报，月报，年报）
     * @param tabType  分类类型 站点1分项3 分类4
     * @param deptCode 所属机构
     */
    @GetMapping("/getElectricityReportList")
    public AjaxResult getElectricityReportList(@RequestParam("codeList") List<String> codeList,
                                               @RequestParam Date baseTime,
                                               @RequestParam String type,
                                               @RequestParam String tabType,
                                               @RequestParam(value = "deptCode", required = false) String deptCode) {
        if (StringUtils.isEmpty(tabType)) {
            return error("缺少参数tabType");
        }
        startPage();
        if (codeList.size() == 0) {
            return error("未选择仓储");
        }
        if (!"1".equals(tabType) && StringUtils.isEmpty(deptCode)) {
            return error("请选择归属部门");
        }
        Integer statisticType = null;
        if(type.equals(StatisticTypeEnum.RIBAO.getName())){//日报
            statisticType = StatisticTypeEnum.RIBAO.getCode();
        }else if(type.equals(StatisticTypeEnum.YUEBAO.getName())){//月报
            statisticType = StatisticTypeEnum.YUEBAO.getCode();
        }else{
            statisticType = StatisticTypeEnum.NIANBAO.getCode();
        }
        java.sql.Timestamp resultDate= new java.sql.Timestamp(baseTime.getTime());
        baseTime = resultDate;
        try {
            return AjaxResult.success(energySjfxElectricityService.getElectricityReportList(codeList, baseTime, statisticType, tabType, deptCode));
        } catch (Exception e) {
            e.printStackTrace();
            return error(e.getMessage());
        }
    }


    /**
     * 能耗足迹
     *
     * @param deptCodeList 机构编号列表
     * @param baseTime     基础日期
     * @param type         报表类型
     */
    @GetMapping("/getEnergyTrackList")
    public AjaxResult getEnergyTrackList(@RequestParam("deptCodeList") List<String> deptCodeList,
                                         @RequestParam Date baseTime,
                                         @RequestParam String type) {
        if (deptCodeList.size() == 0) {
            return error("请选择归属部门");
        }
        Integer statisticsType = null;
        if(type.equals(StatisticTypeEnum.RIBAO.getName())){//日报
            statisticsType = StatisticTypeEnum.RIBAO.getCode();
        }else if(type.equals(StatisticTypeEnum.YUEBAO.getName())){//月报
            statisticsType = StatisticTypeEnum.YUEBAO.getCode();
        }else{
            statisticsType = StatisticTypeEnum.NIANBAO.getCode();
        }
        java.sql.Timestamp resultDate= new java.sql.Timestamp(baseTime.getTime());
        baseTime = resultDate;
        return AjaxResult.success(energySjfxElectricityService.getEnergyTrackList(deptCodeList, baseTime, statisticsType));
    }




    /**
     * 获取站点分时段用电报表
     * @param deptCodeList 站点编号
     * @param baseTime 时间
     * @param type 类型
     */
    @GetMapping("/getSplitTimeByDept")
    public AjaxResult getSplitTimeByDept(@RequestParam List<String> deptCodeList,
                                         @RequestParam Date baseTime,
                                         @RequestParam String type ) {
        if(deptCodeList.size() == 0){
            return error("未选择站点");
        }
        return AjaxResult.success(energySjfxElectricityService.getSplitTimeByDept(deptCodeList, baseTime, type));
    }

    /**
     * 电费分析
     * @param deptCodeList
     * @param baseTime
     * @param type
     * @return
     */
    @GetMapping("/getElectricityBillByDept")
    public AjaxResult getElectricityBillByDept(@RequestParam List<String> deptCodeList,
                                               @RequestParam Date baseTime,
                                               @RequestParam String type ){
        if(deptCodeList.size() == 0){
            return error("未选择站点");
        }
        return AjaxResult.success(energySjfxElectricityService.getElectricityBillByDept(deptCodeList, baseTime, type));
    }

    @GetMapping("/test")
    public AjaxResult test() {
        List<EnergySjfx>list = energySjfxElectricityService.test();
        return AjaxResult.success(list);
    }
}
