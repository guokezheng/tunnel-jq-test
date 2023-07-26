package com.tunnel.business.service.energyManagement.impl;


import com.tunnel.business.datacenter.domain.enumeration.StatisticTypeEnum;
import com.tunnel.business.datacenter.util.ArithUtil;
import com.tunnel.business.domain.dataInfo.SdTunnels;
import com.tunnel.business.domain.energyManagement.*;
import com.tunnel.business.mapper.dataInfo.SdTunnelsMapper;
import com.tunnel.business.mapper.energyManagement.EnergySjfxElectricityMapper;
import com.tunnel.business.service.dataInfo.ISdTunnelsService;
import com.tunnel.business.service.energyManagement.EnergySjfxElectricityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * EnergySjfxElectricityImpl
 *
 * @date: 2023/7/17 15:45
 * @author: tjw
 * @version: 1.0
 */
@Service
public class EnergySjfxElectricityServiceImpl implements EnergySjfxElectricityService {

    @Autowired
    private EnergySjfxElectricityMapper mapper;

    @Autowired
    private ISdTunnelsService sdTunnelsService;

    @Autowired
    private SdTunnelsMapper sdTunnelsMapper;


    /**
     * 获取回路用能报表
     *
     * @param codeList code列表
     * @param baseTime 基础日期
     * @param type     日期类型
     * @param tabType  机构1 回路2 分项3 分类4
     * @param deptCode 站点code
     * @return
     */
    @Override
    public List<List<ElectricityData>> getElectricityReportList(List<String> codeList,
                                                                Date baseTime,
                                                                Integer statisticType,
                                                                String tabType,
                                                                String deptCode) throws Exception {
        List<List<ElectricityData>> result = new ArrayList<>();
        List<ElectricityData> energyList = new ArrayList<>();
        List<ElectricityData> newlist = new ArrayList<>();
        List<List<ElectricityData>> resultList = new ArrayList<>();
        List<String> deptCodes = null;
        if(deptCode!=null){
            // 查询deptCode所有子节点
            List<SdTunnels>depts = sdTunnelsMapper.getChildCodeList(deptCode);
            deptCodes = depts.stream().map(e->e.getTunnelId()).collect(Collectors.toList());
        }

        if ("1".equals(tabType)) {// 机构
            energyList = mapper.getSiteByDateType(codeList, statisticType, baseTime);
            energyList.stream().collect(Collectors.groupingBy(ElectricityData::getId, Collectors.toList()))
                    .forEach((model, list) -> {
                        resultList.add(list);
                    });
        }else if ("3".equals(tabType)) { // 分项

            energyList = mapper.getItemizedEnergyByDateType(codeList, statisticType, baseTime, deptCodes);
            energyList.stream().collect(Collectors.groupingBy(ElectricityData::getId, Collectors.toList()))
                    .forEach((model, list) -> {
                        resultList.add(list);
                    });

        } else if ("4".equals(tabType)) { // 分类

            energyList = mapper.getElectricityListByClass(codeList, statisticType, baseTime, deptCodes);
            energyList.stream().collect(Collectors.groupingBy(ElectricityData::getId, Collectors.toList()))
                    .forEach((model, list) -> {
                        resultList.add(list);
                    });

        } else {
            throw new Exception("tabType类型错误");
        }

        return resultList;
    }

    /**
     * 能耗足迹
     * @param codeList
     * @param baseTime
     * @param statisticType
     * @return
     */
    @Override
    public List<EnergyDayparting> getEnergyTrackList(List<String> codeList, Date baseTime, Integer statisticsType) {
        return mapper.getEnergyTrackList(codeList, baseTime, statisticsType);
    }

    @Override
    public List<EnergySjfx> test() {
        return mapper.test();
    }

    /**
     * 分时段用能
     * @param deptCodeList
     * @param baseTime
     * @param type
     * @return
     */
    @Override
    public List<SplitTimeDto> getSplitTimeByDept(List<String> deptCodeList, Date baseTime, String type) {
        Integer statisticType = null;
        List<SplitTimeDto> result = new ArrayList<>();
        if(type.equals(StatisticTypeEnum.RIBAO.getName())){//日报
            statisticType = StatisticTypeEnum.RIBAO.getCode();
        }else if(type.equals(StatisticTypeEnum.YUEBAO.getName())){//月报
            statisticType = StatisticTypeEnum.YUEBAO.getCode();
        }else{
            statisticType = StatisticTypeEnum.NIANBAO.getCode();
        }
        List <EnergyDayparting> energyDataList =  mapper.getEnergyDayparting(deptCodeList,baseTime,statisticType);
        if (energyDataList.isEmpty()) {
            return new ArrayList<>();
        }
        HashMap<String, Double> meanUnitPriceOfYear = new HashMap<>();
        List<EnergyConfigcenterElectricityPrice> priceList = new ArrayList<>();
        if ("year".equals(type)) {
            meanUnitPriceOfYear = getMeanUnitPriceOfYear(baseTime);
            priceList.addAll(mapper.getListOfYear(baseTime));// 获取当年电价

        } else {
            priceList.add(mapper.getListOfMonth(baseTime));// 获取当月电价
        }

        EnergyConfigcenterElectricityPrice elePrices = mapper.getInfoByLastMonth(); // 未配置默认使用上月电价
        Map<String, EnergyConfigcenterElectricityPrice> priceMap = priceList.stream().collect(Collectors.toMap(EnergyConfigcenterElectricityPrice::getMonth, e->e));

        if("year".equals(type)){

        }else{
            for(int i = 0;i<energyDataList.size();i++){//按月统计的话
                SplitTimeDto dto = new SplitTimeDto();
                dto.setCode(energyDataList.get(i).getTunnelId());
                dto.setName(energyDataList.get(i).getTunnelName());
                dto.setjPrice(getPrice(priceList.get(0), priceList.get(0).getJianCof().doubleValue()));
                dto.setgPrice(getPrice(priceList.get(0), priceList.get(0).getGuCof().doubleValue()));
                dto.setfPrice(getPrice(priceList.get(0), priceList.get(0).getFengCof().doubleValue()));
                dto.setsPrice(getPrice(priceList.get(0), priceList.get(0).getShenCof().doubleValue()));
                dto.setpPrice(getPrice(priceList.get(0),1.00));
                Double sumValue= energyDataList.get(i).getpValue()+energyDataList.get(i).getjValue()+energyDataList.get(i).getfValue()+energyDataList.get(i).getgValue()+energyDataList.get(i).getsValue();
                dto.setSumValue(sumValue);

                dto.setSumPrice(ArithUtil.accurateDecimal(sumValue, 2));
                dto.setSumJPrice(ArithUtil.accurateDecimal(energyDataList.get(i).getjValue(), 2));
                dto.setSumFPrice(ArithUtil.accurateDecimal(energyDataList.get(i).getfValue(), 2));
                dto.setSumPPrice(ArithUtil.accurateDecimal(energyDataList.get(i).getpValue(), 2));
                dto.setSumGPrice(ArithUtil.accurateDecimal(energyDataList.get(i).getgValue(), 2));
                dto.setSumSPrice(ArithUtil.accurateDecimal(energyDataList.get(i).getsValue(), 2));
                result.add(dto);
            }
        }







        return null;
    }

    private HashMap<String, Double> getMeanUnitPriceOfYear(Date baseTime) {
        //获取一年内的数据
        List<EnergyConfigcenterElectricityPrice> listOfInterval = mapper.getListOfYear(baseTime);
        double j=0;
        double f=0;
        double p=0;
        double g=0;
        double s=0;
        for(EnergyConfigcenterElectricityPrice price : listOfInterval ){
            j = ArithUtil.add(j, getPrice(price, price.getJianCof().doubleValue()));
            f = ArithUtil.add(f, getPrice(price, price.getFengCof().doubleValue()));
            p = ArithUtil.add(p, getPrice(price, 1.0));
            g = ArithUtil.add(g, getPrice(price, price.getGuCof().doubleValue()));
            s = ArithUtil.add(s, getPrice(price, price.getShenCof().doubleValue()));
        }
        HashMap hashMap=new HashMap();
        hashMap.put("1220",ArithUtil.div(j,listOfInterval.size()));
        hashMap.put("1221",ArithUtil.div(f,listOfInterval.size()));
        hashMap.put("1222",ArithUtil.div(p,listOfInterval.size()));
        hashMap.put("1223",ArithUtil.div(g,listOfInterval.size()));
        hashMap.put("1224",ArithUtil.div(s,listOfInterval.size()));
        return hashMap;
    }

    private static double getPrice(EnergyConfigcenterElectricityPrice p, Double cof) {
        return p.getUnitPrice2().doubleValue() * cof + p.getUnitPrice3().doubleValue() * cof + p.getUnitPrice4().doubleValue() + p.getUnitPrice5().doubleValue() + p.getUnitPrice6().doubleValue();
    }


}
