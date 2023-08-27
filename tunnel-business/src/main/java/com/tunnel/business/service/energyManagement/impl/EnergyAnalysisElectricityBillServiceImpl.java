package com.tunnel.business.service.energyManagement.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.tunnel.business.domain.energyManagement.EnergyAnalysisElectricityBill;
import com.tunnel.business.domain.energyManagement.EnergySite;
import com.tunnel.business.domain.energyManagement.EnergySjfx;
import com.tunnel.business.mapper.energyManagement.SdEnergyAnalysisElectricityBillMapper;
import com.tunnel.business.mapper.energyManagement.SdEnergyDataMapper;
import com.tunnel.business.service.energyManagement.EnergyAnalysisElectricityBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;

/**
 * 能源分析电力账单
 * @date: 2023/7/17 15:45
 * @author: hyw
 * @version: 1.0
 */
@Service
public class EnergyAnalysisElectricityBillServiceImpl implements EnergyAnalysisElectricityBillService {

    @Autowired
    private SdEnergyAnalysisElectricityBillMapper energyAnalysisElectricityBillMapper;

    @Autowired
    private SdEnergyDataMapper sdEnergyDataMapper;
    /**
     * 查询时段用电柱状图
     * @param energyAnalysisElectricityBill 能源分析电力账单实体
     * @return
     */
    @Override
    public List<EnergyAnalysisElectricityBill> selectEnergyAnalysisElectricityBillList(EnergyAnalysisElectricityBill energyAnalysisElectricityBill) {
        return energyAnalysisElectricityBillMapper.selectEnergyAnalysisTimeFrame(energyAnalysisElectricityBill);
    }

    /**
     * 计算用能总电量
     * @return
     */
    @Override
    public Map selectEnergyValueSum(EnergySite energySite) {
        EnergyAnalysisElectricityBill energyAnaly = new EnergyAnalysisElectricityBill();
        //返回数据map
        Map<Object, Object> resultMap = new HashMap<>();
        //获取月总用电量map
        Map<String, Object> dateMap = new HashMap<>();
        //获取当前时间
        String timeStr = DateUtils.getTime();
        //获取月初时间
        String beginningTime = DateUtils.getBeginningTime();
        //获取前一个月当前时间
        String timeStrOne = DateUtils.getsubtractOneTime(timeStr);
        //获取前一个月月初时间
        String beginningTimeOne = DateUtils.getsubtractOneTime(beginningTime);
        dateMap.put("beginTime",beginningTime);
        dateMap.put("endTime",timeStr);
        energyAnaly.setParams(dateMap);
        energyAnaly.setDeptCode(energySite.getTunnelId());
        //本月累计总电量
        EnergyAnalysisElectricityBill energyAnalysisElectricityBill = energyAnalysisElectricityBillMapper.selectEnergyValueSum(energyAnaly);
        Map<String, Object> dateMapOne = new HashMap<>();
        dateMapOne.put("beginTime",beginningTimeOne);
        dateMapOne.put("endTime",timeStrOne);
        energyAnaly.setParams(dateMapOne);

        //上月累计总电量
        BigDecimal growthRate = new BigDecimal(0);
        EnergyAnalysisElectricityBill energyAnalysisElectricity= energyAnalysisElectricityBillMapper.selectEnergyValueSum(energyAnaly);
        if(StringUtils.isNotNull(energyAnalysisElectricity)&&StringUtils.isNotNull(energyAnalysisElectricityBill)){
            BigDecimal currentValue =energyAnalysisElectricityBill.getValue();  // 当前值
            BigDecimal previousYearValue = energyAnalysisElectricity.getValue();  // 前一年的值
            // 计算同比增长率
            BigDecimal difference = currentValue.subtract(previousYearValue);
            growthRate = difference.divide(previousYearValue, 4, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100));
        }

        //本月累计能耗
        // 获取月初时间
        Date monthStart = DateUtils.StrChangeDate(beginningTime);
        EnergySjfx energySjfx = new EnergySjfx();
        energySjfx.setStatisticsType(2);
        energySjfx.setCreateTime(monthStart);
        energySjfx.setTunnelId(energySite.getTunnelId());

        EnergySjfx energySjfxes = sdEnergyDataMapper.getEnergyMonthSum(energySjfx);

        Date monthStartOne = DateUtils.StrChangeDate(beginningTimeOne);
        EnergySjfx energySjfxOne = new EnergySjfx();
        energySjfxOne.setStatisticsType(2);
        energySjfxOne.setCreateTime(monthStartOne);
        energySjfxOne.setTunnelId(energySite.getTunnelId());
        EnergySjfx energySjfxesOne = sdEnergyDataMapper.getEnergyMonthSum(energySjfxOne);

        //本年累计能耗
        EnergySjfx energyYearSjfx = new EnergySjfx();
        energyYearSjfx.setStatisticsType(2);
        List atYearConsumption = DateUtils.getAtYear();
        Map<String, Object> yearConsumptionMap = new HashMap<>();
        yearConsumptionMap.put("beginTime",atYearConsumption.get(0));
        yearConsumptionMap.put("endTime",atYearConsumption.get(1));
        energyYearSjfx.setParams(yearConsumptionMap);
        energyYearSjfx.setTunnelId(energySite.getTunnelId());
        EnergySjfx yearEnergySjfxes = sdEnergyDataMapper.getYearEnergySiteList(energyYearSjfx);
        //去本年累计能耗
        EnergySjfx EnergylastYearSjfx = new EnergySjfx();
        EnergylastYearSjfx.setStatisticsType(2);
        List lastYearConsumption = DateUtils.getAtLastYear();
        Map<String, Object> lastYearConsumptionMap = new HashMap<>();
        lastYearConsumptionMap.put("beginTime",lastYearConsumption.get(0));
        lastYearConsumptionMap.put("endTime",lastYearConsumption.get(1));
        EnergylastYearSjfx.setParams(lastYearConsumptionMap);
        EnergylastYearSjfx.setTunnelId(energySite.getTunnelId());
        EnergySjfx lastYearEnergySjfxes = sdEnergyDataMapper.getYearEnergySiteList(EnergylastYearSjfx);

        //本年累计电量
        Map<String, Object> yearDateMap = new HashMap<>();
        List atYear = DateUtils.getAtYear();
        yearDateMap.put("beginTime",atYear.get(0));
        yearDateMap.put("endTime",atYear.get(1));
        energyAnaly.setParams(yearDateMap);
        //计算年总用电量
        EnergyAnalysisElectricityBill yearElectricitySum = energyAnalysisElectricityBillMapper.selectEnergyValueSum(energyAnaly);
        //对比同期 去年累计电量
        Map<String, Object> lastYearDateMap = new HashMap<>();
        List atLastYear = DateUtils.getAtLastYear();
        lastYearDateMap.put("beginTime",atLastYear.get(0));
        lastYearDateMap.put("endTime",atLastYear.get(1));
        energyAnaly.setParams(lastYearDateMap);
        //计算去年年总用电量
        EnergyAnalysisElectricityBill lastYearElectricitySum = energyAnalysisElectricityBillMapper.selectEnergyValueSum(energyAnaly);

        //本月照明电量
        EnergySjfx energySjfxIllumination = new EnergySjfx();
        energySjfxIllumination.setItemizedName("隧道照明");
        energySjfxIllumination.setStatisticsType(1);
        Map<String, Object> dateMapIllumination = new HashMap<>();
        dateMapIllumination.put("beginTime",beginningTime);
        dateMapIllumination.put("endTime",timeStr);
        energySjfxIllumination.setParams(dateMapIllumination);
        energySjfxIllumination.setTunnelId(energySite.getTunnelId());
        EnergySjfx energySubentryMonthSum = sdEnergyDataMapper.getEnergySubentryMonthSum(energySjfxIllumination);
        //同期照明电量
        EnergySjfx energySjfxIlluminationOne = new EnergySjfx();
        energySjfxIlluminationOne.setItemizedName("隧道照明");
        energySjfxIlluminationOne.setTunnelId(energySite.getTunnelId());
        energySjfxIlluminationOne.setStatisticsType(1);
        Map<String, Object> dateMapIlluminationOne = new HashMap<>();
        dateMapIlluminationOne.put("beginTime",beginningTimeOne);
        dateMapIlluminationOne.put("endTime",timeStrOne);
        energySjfxIlluminationOne.setParams(dateMapIlluminationOne);
        EnergySjfx energySubentryMonthSumOne = sdEnergyDataMapper.getEnergySubentryMonthSum(energySjfxIlluminationOne);

        //本月风机电量
        EnergySjfx energySjfxDraught = new EnergySjfx();
        energySjfxDraught.setItemizedName("风机");
        energySjfxDraught.setStatisticsType(1);
        Map<String, Object> dateMapDraught = new HashMap<>();
        dateMapDraught.put("beginTime",beginningTime);
        dateMapDraught.put("endTime",timeStr);
        energySjfxDraught.setParams(dateMapDraught);
        energySjfxDraught.setTunnelId(energySite.getTunnelId());
        EnergySjfx energyDraughtMonthSum = sdEnergyDataMapper.getEnergySubentryMonthSum(energySjfxDraught);
        //同期照明电量
        EnergySjfx energySjfxDraughtOne = new EnergySjfx();
        energySjfxDraughtOne.setItemizedName("风机");
        energySjfxDraughtOne.setStatisticsType(1);
        Map<String, Object> dateMapDraughtOne = new HashMap<>();
        dateMapDraughtOne.put("beginTime",beginningTimeOne);
        dateMapDraughtOne.put("endTime",timeStrOne);
        energySjfxDraughtOne.setParams(dateMapDraughtOne);
        energySjfxDraughtOne.setTunnelId(energySite.getTunnelId());
        EnergySjfx energyDraughtMonthSumOne = sdEnergyDataMapper.getEnergySubentryMonthSum(energySjfxDraughtOne);




        //本月电费  -对比同期
        String ds = StringUtils.isNotNull(energyAnalysisElectricityBill )?energyAnalysisElectricityBill.getValue().toString() : "0";
        String ds1 = StringUtils.isNotNull(energyAnalysisElectricity) ? energyAnalysisElectricity.getValue().toString():"0";
        String energyStr = ds+ ","+ ds1;
        resultMap.put("本月电费",energyStr);
        //本月能耗  -对比同期
        String energySjfxesStr =   StringUtils.isNotNull(energySjfxes ) ?energySjfxes.getEnergyValue().toString() :"0";
        String energySjfxesStrOne =   StringUtils.isNotNull(energySjfxesOne ) ?energySjfxesOne.getEnergyValue().toString() :"0";
        String energyStrOne = energySjfxesStr+","+energySjfxesStrOne;
        resultMap.put("本月能耗",energyStrOne);
        //照明电量  -对比同期
        String energySubentryMonthSumStr =   StringUtils.isNotNull(energySubentryMonthSum ) ? energySubentryMonthSum.getEnergyValue().toString() :"0";
        String energySubentryMonthSumStrOne =   StringUtils.isNotNull(energySubentryMonthSumOne ) ?energySubentryMonthSumOne.getEnergyValue().toString() :"0";
        String energyStrTwo = energySubentryMonthSumStr+","+energySubentryMonthSumStrOne;
        resultMap.put("照明电量",energyStrTwo);
        //风机电量  -对比同期
        String energyDraughtMonthSumStr =   StringUtils.isNotNull(energyDraughtMonthSum ) ?energyDraughtMonthSum.getEnergyValue().toString():"0";
        String energyDraughtMonthSumOneStr =   StringUtils.isNotNull(energyDraughtMonthSumOne ) ?energyDraughtMonthSumOne.getEnergyValue().toString() :"0";
        String energyStrThree = energyDraughtMonthSumStr+","+energyDraughtMonthSumOneStr;
        resultMap.put("风机电量",energyStrThree);

        //年总用电量  -对比同期
        String yearElectricitySumStr =   StringUtils.isNotNull(yearElectricitySum ) ?yearElectricitySum.getValue().toString():"0";
        String lastYearElectricitySumStr =   StringUtils.isNotNull(lastYearElectricitySum ) ?lastYearElectricitySum.getValue().toString() :"0";
        String lastYearElectricity = yearElectricitySumStr+","+lastYearElectricitySumStr;
        resultMap.put("本年电费",lastYearElectricity);

        //年总耗能  -对比同期
        String yearEnergySjfxesStr =   StringUtils.isNotNull(yearEnergySjfxes ) ?yearEnergySjfxes.getEnergyValue().toString():"0";
        String lastYearEnergySjfxesStr =   StringUtils.isNotNull(lastYearEnergySjfxes ) ?lastYearEnergySjfxes.getEnergyValue().toString() :"0";
        String energyStrThere = yearEnergySjfxesStr+","+lastYearEnergySjfxesStr;
        resultMap.put("本年能耗",energyStrThere);
        return resultMap;
    }

    @Override
    public Map getrealTimeData(EnergySite energySite) {
        //获取当前时间
        String timeStr = DateUtils.getTime();
        //获取月初时间
        String beginningTime = DateUtils.getBeginningTime();
        EnergySjfx energySjfxOne = new EnergySjfx();
        energySjfxOne.setTunnelId("JQ-WeiFang-JiuLongYu-HSD");
        energySjfxOne.setStatisticsType(1);
        energySjfxOne.setClassificationName("市电");
        Map<String, Object> dateMap = new HashMap<>();
        dateMap.put("beginTime",beginningTime);
        dateMap.put("endTime",timeStr);
        energySjfxOne.setParams(dateMap);
        energySjfxOne.setTunnelId(energySite.getTunnelId());
        EnergySjfx energySjfxescity = sdEnergyDataMapper.selectMonthValueSum(energySjfxOne);
        energySjfxOne.setClassificationName("光伏自用");
        EnergySjfx energySjfxescityLight = sdEnergyDataMapper.selectMonthValueSum(energySjfxOne);
        energySjfxOne.setClassificationName("柴发");
        EnergySjfx energySjfxescityFirewood = sdEnergyDataMapper.selectMonthValueSum(energySjfxOne);
        energySjfxOne.setClassificationName("风发");
        EnergySjfx energySjfxescityLightWind = sdEnergyDataMapper.selectMonthValueSum(energySjfxOne);
        System.out.print(energySjfxescityLight);
        Map resultMap = new HashMap();
        resultMap.put("市电", StringUtils.isNotNull(energySjfxescity) ? energySjfxescity.getEnergyValue():0);
        resultMap.put("光伏自用",StringUtils.isNotNull(energySjfxescityLight)?energySjfxescityLight.getEnergyValue() :0);
        resultMap.put("柴发",StringUtils.isNotNull(energySjfxescityFirewood)? energySjfxescityFirewood.getEnergyValue() : 0);
        resultMap.put("风发",StringUtils.isNotNull(energySjfxescityLightWind)? energySjfxescityLightWind.getEnergyValue():0);
        return resultMap;
    }

    public static void main(String[] args) {
        String time = DateUtils.getTime();
        System.out.print(time);
    }
}
