package com.tunnel.platform.controller.energyManagement;


import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.Result;
import com.ruoyi.common.core.page.TableDataInfo;
import com.tunnel.business.domain.energyManagement.EnergyAnalysisElectricityBill;
import com.tunnel.business.service.energyManagement.EnergyAnalysisElectricityBillService;
import com.tunnel.business.service.energyManagement.EnergySjfxElectricityService;
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
public class EnergyAnalysisElectricityBillController extends BaseController {

    @Autowired
    private EnergyAnalysisElectricityBillService energyAnalysisElectricity;


    /**
     * 查询时段用电柱状图
     * @param energyAnalysisElectricityBill 能源分析电力账单实体
     * @return
     */
    @GetMapping("/selectEnergyAnalysisElectricityBillList")
    public TableDataInfo selectEnergyAnalysisElectricityBillList(EnergyAnalysisElectricityBill energyAnalysisElectricityBill){
        startPage();
        return getDataTable(energyAnalysisElectricity.selectEnergyAnalysisElectricityBillList(energyAnalysisElectricityBill));
    }

    /**
     * 计算用能总电量
     * @return
     */
    @GetMapping("/selectEnergyValueSum")
    public Result selectEnergyValueSum(){
        return Result.success(energyAnalysisElectricity.selectEnergyValueSum());
    }

}
