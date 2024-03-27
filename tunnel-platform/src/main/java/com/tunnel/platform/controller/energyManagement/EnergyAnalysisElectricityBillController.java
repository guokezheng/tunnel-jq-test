package com.tunnel.platform.controller.energyManagement;


import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.Result;
import com.ruoyi.common.core.page.TableDataInfo;
import com.tunnel.business.domain.energyManagement.EnergyAnalysisElectricityBill;
import com.tunnel.business.domain.energyManagement.EnergySite;
import com.tunnel.business.service.energyManagement.EnergyAnalysisElectricityBillService;
import com.tunnel.business.service.energyManagement.EnergySjfxElectricityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

/**
 * 能源监控Controller
 *
 * @author hyw
 * @date 2023-08-17
 */

@RequestMapping("/energyAnalysisElectricityBillController")
@RestController
@Api(tags = "能源监控")
@ApiSupport(order = 16)
public class EnergyAnalysisElectricityBillController extends BaseController {

    @Autowired
    private EnergyAnalysisElectricityBillService energyAnalysisElectricity;


    /**
     * 查询时段用电柱状图
     * @param energyAnalysisElectricityBill 能源分析电力账单实体
     * @return
     */
    @ApiOperation("查询时段用电柱状图")
    @GetMapping("/selectEnergyAnalysisElectricityBillList")
    public TableDataInfo selectEnergyAnalysisElectricityBillList(EnergyAnalysisElectricityBill energyAnalysisElectricityBill){
        startPage();
        return getDataTable(energyAnalysisElectricity.selectEnergyAnalysisElectricityBillList(energyAnalysisElectricityBill));
    }

    /**
     * 计算用能总电量
     * @return
     */
    @ApiOperation("计算用能总电量")
    @GetMapping("/selectEnergyValueSum")
    public Result selectEnergyValueSum(EnergySite energySite){
        return Result.success(energyAnalysisElectricity.selectEnergyValueSum(energySite));
    }

    /**
     * 实时电量展示
     * @return
     */
    @ApiOperation("实时电量展示")
    @GetMapping("/getrealTimeData")
    public Result getrealTimeData(EnergySite energySite){
        return Result.success(energyAnalysisElectricity.getrealTimeData(energySite));
    }

}
