package com.tunnel.business.service.energyManagement.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.tunnel.business.domain.energyManagement.EnergyAnalysisElectricityBill;
import com.tunnel.business.domain.energyManagement.EnergySjfx;
import com.tunnel.business.mapper.energyManagement.SdEnergyAnalysisElectricityBillMapper;
import com.tunnel.business.mapper.energyManagement.SdEnergyDataMapper;
import com.tunnel.business.service.energyManagement.EnergyAnalysisElectricityBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public Map selectEnergyValueSum() {
        EnergyAnalysisElectricityBill energyAnaly = new EnergyAnalysisElectricityBill();
        //返回数据map
        HashMap<Object, Object> resultMap = new HashMap<>();
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

        //本月城市总用电  同比上个月增涨率
        double growthRateDouble = 0;
        EnergySjfx energySjfx = new EnergySjfx();
        Map<String, Object> sjfxMap = new HashMap<>();
        sjfxMap.put("beginTime",beginningTime);
        sjfxMap.put("endTime",timeStr);
        energySjfx.setParams(sjfxMap);
        List<EnergySjfx> energySjfxes = sdEnergyDataMapper.selectMonthValueSum(energySjfx);

        Map<String, Object> sjfxMapOne = new HashMap<>();
        sjfxMapOne.put("beginTime",beginningTimeOne);
        sjfxMapOne.put("endTime",timeStrOne);
        energySjfx.setParams(sjfxMapOne);
        List<EnergySjfx> energySjfxesOne = sdEnergyDataMapper.selectMonthValueSum(energySjfx);
        if(StringUtils.isNotNull(energySjfxes)&&StringUtils.isNotNull(energySjfxesOne)){
            Double currentValue =energySjfxes.get(0).getEnergyValue();  // 当前值
            Double previousYearValue = energySjfxesOne.get(0).getEnergyValue();  // 前一年的值
            // 计算同比增长率
            growthRateDouble = ((double) (currentValue - previousYearValue) / previousYearValue) * 100;
        }
        // 创建 DecimalFormat 对象，并设置格式化模式
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        // 格式化数字
        String result = decimalFormat.format(growthRateDouble);
        resultMap.put(energyAnalysisElectricityBill.getValue(),growthRate.setScale(2, RoundingMode.HALF_UP));

        resultMap.put(energySjfxes.get(0).getEnergyValue(),result);
        return resultMap;
    }

    public static void main(String[] args) {
        String time = DateUtils.getTime();
        System.out.print(time);
    }
}
