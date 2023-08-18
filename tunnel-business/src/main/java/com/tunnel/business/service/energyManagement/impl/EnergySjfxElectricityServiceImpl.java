package com.tunnel.business.service.energyManagement.impl;


import com.tunnel.business.datacenter.domain.enumeration.StatisticTypeEnum;
import com.tunnel.business.datacenter.domain.enumeration.TunnelEnum;
import com.tunnel.business.datacenter.util.ArithUtil;
import com.tunnel.business.domain.dataInfo.SdTunnels;
import com.tunnel.business.domain.energyManagement.*;
import com.tunnel.business.mapper.dataInfo.SdTunnelsMapper;
import com.tunnel.business.mapper.energyManagement.EnergySjfxElectricityMapper;
import com.tunnel.business.mapper.energyManagement.SdEnergyConfigcenterClassificationMapper;
import com.tunnel.business.mapper.energyManagement.SdEnergyConfigcenterItemizedMapper;
import com.tunnel.business.service.dataInfo.ISdTunnelsService;
import com.tunnel.business.service.energyManagement.EnergySjfxElectricityService;
import com.tunnel.business.utils.util.DateUtil;
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

    @Autowired
    private SdEnergyConfigcenterItemizedMapper itemizedMapper;

    @Autowired
    private SdEnergyConfigcenterClassificationMapper classificationMapper;


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
        //初始化数据
        List<Map<String, Object>> infoList = new ArrayList<>();
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
            if(energyList.size() == 0){
                //储存隧道数据
                List<Map<String, Object>> list = new ArrayList<>();
                List<String> collect = codeList.stream().filter(item -> TunnelEnum.contains(item) == true).collect(Collectors.toList());
                collect.stream().forEach(item -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id",item);
                    map.put("name",TunnelEnum.getValue(item));
                    list.add(map);
                });
                infoList = list;
            }
        }else if ("3".equals(tabType)) { // 分项

            energyList = mapper.getItemizedEnergyByDateType(codeList, statisticType, baseTime, deptCodes);
            energyList.stream().collect(Collectors.groupingBy(ElectricityData::getId, Collectors.toList()))
                    .forEach((model, list) -> {
                        resultList.add(list);
                    });
            if(energyList.size() == 0){
                //分项数据
                infoList = itemizedMapper.getItemizedMap(codeList);
            }

        } else if ("4".equals(tabType)) { // 分类

            energyList = mapper.getElectricityListByClass(codeList, statisticType, baseTime, deptCodes);
            energyList.stream().collect(Collectors.groupingBy(ElectricityData::getId, Collectors.toList()))
                    .forEach((model, list) -> {
                        resultList.add(list);
                    });
            if(energyList.size() == 0){
                infoList = classificationMapper.getFicationMap(codeList);
            }
        } else {
            throw new Exception("tabType类型错误");
        }
        if(resultList.size() == 0){
            List<List<ElectricityData>> lists = setDataInfo(statisticType, baseTime, infoList);
            for(List<ElectricityData> item : lists){
                resultList.add(item);
            }
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
            List<String> collect = deptCodeList.stream().filter(item -> TunnelEnum.contains(item) == true).collect(Collectors.toList());
            for(String item : collect){
                SplitTimeDto dto = new SplitTimeDto();
                dto.setCode(item);
                dto.setName(TunnelEnum.getValue(item));
                dto.setjPrice(0.0);
                dto.setpPrice(0.0);
                dto.setsPrice(0.0);
                dto.setgPrice(0.0);
                dto.setfPrice(0.0);
                dto.setSumGPrice(0.0);
                dto.setSumJPrice(0.0);
                dto.setSumPPrice(0.0);
                dto.setSumSPrice(0.0);
                dto.setSumFPrice(0.0);
                dto.setfValue(0.0);
                dto.setgValue(0.0);
                dto.setjValue(0.0);
                dto.setpValue(0.0);
                dto.setsValue(0.0);
                dto.setSumValue(0.0);
                dto.setSumPrice(0.0);
                result.add(dto);
            }
            return result;
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
            //单价总和
            double jPriceCount = 0;
            double gPriceCount = 0;
            double fPriceCount = 0;
            double sPriceCount = 0;
            double pPriceCount = 0;
            //金额总和
            double sumJPriceCount = 0;
            double sumFPriceCount = 0;
            double sumPPriceCount = 0;
            double sumGPriceCount = 0;
            double sumSPriceCount = 0;
            //年报总用电量
            for(EnergyDayparting energyDayparting : energyDataList){
                for(EnergyConfigcenterElectricityPrice item : priceList){
                    //计算每月单价
                    double jPrice = getPrice(item, item.getJianCof().doubleValue());
                    double gPrice = getPrice(item, item.getGuCof().doubleValue());
                    double fPrice = getPrice(item, item.getFengCof().doubleValue());
                    double sPrice = getPrice(item, item.getShenCof().doubleValue());
                    double pPrice = getPrice(item, 1.00);
                    //计算每月金额
                    double sumJPrice = ArithUtil.accurateDecimal((energyDayparting.getjValue() * jPrice), 2);
                    double sumFPrice = ArithUtil.accurateDecimal((energyDayparting.getfValue() * fPrice), 2);
                    double sumPPrice = ArithUtil.accurateDecimal((energyDayparting.getpValue() * pPrice), 2);
                    double sumGPrice = ArithUtil.accurateDecimal((energyDayparting.getgValue() * gPrice), 2);
                    double sumSPrice = ArithUtil.accurateDecimal((energyDayparting.getsValue() * sPrice), 2);
                    //计算全年总单价
                    jPriceCount = jPriceCount + jPrice;
                    gPriceCount = gPriceCount + gPrice;
                    fPriceCount = fPriceCount + fPrice;
                    sPriceCount = sPriceCount + sPrice;
                    pPriceCount = pPriceCount + pPrice;
                    //计算全年总金额
                    sumJPriceCount = sumJPriceCount+ sumJPrice;
                    sumFPriceCount = sumFPriceCount+ sumFPrice;
                    sumPPriceCount = sumPPriceCount+ sumPPrice;
                    sumGPriceCount = sumGPriceCount+ sumGPrice;
                    sumSPriceCount = sumSPriceCount+ sumSPrice;
                }
                SplitTimeDto dto = new SplitTimeDto();
                dto.setCode(energyDayparting.getTunnelId());
                dto.setName(energyDayparting.getTunnelName());
                dto.setjPrice(ArithUtil.accurateDecimal(jPriceCount / 12,2));
                dto.setpPrice(ArithUtil.accurateDecimal(pPriceCount / 12,2));
                dto.setsPrice(ArithUtil.accurateDecimal(sPriceCount / 12,2));
                dto.setgPrice(ArithUtil.accurateDecimal(gPriceCount / 12,2));
                dto.setfPrice(ArithUtil.accurateDecimal(fPriceCount / 12,2));
                dto.setSumGPrice(sumGPriceCount);
                dto.setSumJPrice(sumJPriceCount);
                dto.setSumPPrice(sumPPriceCount);
                dto.setSumSPrice(sumSPriceCount);
                dto.setSumFPrice(sumFPriceCount);
                dto.setfValue(energyDayparting.getfValue());
                dto.setgValue(energyDayparting.getgValue());
                dto.setjValue(energyDayparting.getjValue());
                dto.setpValue(energyDayparting.getpValue());
                dto.setsValue(energyDayparting.getsValue());
                dto.setSumValue(energyDayparting.getsValue());
                dto.setSumPrice(sumGPriceCount+sumJPriceCount+sumPPriceCount+sumSPriceCount+sumFPriceCount);
                result.add(dto);
            }
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

                dto.setSumJPrice(ArithUtil.accurateDecimal(energyDataList.get(i).getjValue(), 2));
                dto.setSumFPrice(ArithUtil.accurateDecimal(energyDataList.get(i).getfValue(), 2));
                dto.setSumPPrice(ArithUtil.accurateDecimal(energyDataList.get(i).getpValue(), 2));
                dto.setSumGPrice(ArithUtil.accurateDecimal(energyDataList.get(i).getgValue(), 2));
                dto.setSumSPrice(ArithUtil.accurateDecimal(energyDataList.get(i).getsValue(), 2));
                dto.setSumPrice(ArithUtil.accurateDecimal(dto.getSumFPrice()+dto.getSumGPrice()+dto.getSumJPrice()+dto.getSumPPrice()+dto.getSumSPrice(), 2));
                dto.setfValue(energyDataList.get(i).getfValue());
                dto.setgValue(energyDataList.get(i).getgValue());
                dto.setjValue(energyDataList.get(i).getjValue());
                dto.setpValue(energyDataList.get(i).getpValue());
                dto.setsValue(energyDataList.get(i).getsValue());
                result.add(dto);
            }
        }
        return result;
    }

    @Override
    public List<List<Map<String, Object>>> getElectricityBillByDept(List<String> deptCodeList, Date baseTime, String type) {
        Integer statisticType = null;
        List<Map<String, Object>> result = new ArrayList<>();
        //每月天数、每年月份
        List<String> dateTimeList = getDateTimeList(baseTime, type);
        //区分月报年报
        if(type.equals(StatisticTypeEnum.YUEBAO.getName())){
            statisticType = StatisticTypeEnum.YUEBAO.getCode();
        }else{
            statisticType = StatisticTypeEnum.NIANBAO.getCode();
        }
        //查询用电量
        List <EnergyAnalysisElectricityBill> energyDataList =  mapper.getElectricityBillByDept(deptCodeList,baseTime,statisticType);
        if (energyDataList.isEmpty()) {
            List<String> collect = deptCodeList.stream().filter(item -> TunnelEnum.contains(item) == true).collect(Collectors.toList());
            List<Map<String, Object>> list = new ArrayList<>();
            collect.stream().forEach(item -> {
                Map<String, Object> map = new HashMap<>();
                map.put("id",item);
                map.put("name",TunnelEnum.getValue(item));
                list.add(map);
            });
            List<List<ElectricityData>> lists = setDataInfo(statisticType, baseTime, list);
            List<List<Map<String, Object>>> infoList = new ArrayList<>();
            for(int i = 0; i < lists.size(); i++){
                List<ElectricityData> list1 = lists.get(i);
                List<Map<String, Object>> mapList = new ArrayList<>();
                list1.stream().forEach(item -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id",item.getId());
                    map.put("name",item.getName());
                    map.put("rt",item.getRt());
                    map.put("value",item.getValue());
                    mapList.add(map);
                });
                infoList.add(mapList);
            }
            return infoList;
        }
        //获取用电量
        List<EnergyConfigcenterElectricityPrice> priceList = selectPrice(type,baseTime);
        //计算电费
        List<List<Map<String, Object>>> list = countData(energyDataList, type, priceList, dateTimeList);
        return list;
    }

    /**
     * 计算电费
     * @param energyDataList
     * @param type
     * @param priceList
     * @param dateTimeList
     * @return
     */
    public List<List<Map<String, Object>>> countData(List<EnergyAnalysisElectricityBill> energyDataList, String type, List<EnergyConfigcenterElectricityPrice> priceList, List<String> dateTimeList){
        List<List<Map<String, Object>>> list = new ArrayList<>();
        for(EnergyAnalysisElectricityBill item : energyDataList){
            List<Map<String, Object>> mapList = new ArrayList<>();
            if("year".equals(type)){
                for(String time : dateTimeList){
                    Map<String, Object> map = new HashMap<>();
                    map.put("rt",time);
                    map.put("id",item.getDeptCode());
                    map.put("name",item.getTunnelName());
                    map.put("value",0.0);
                    mapList.add(map);
                }
                for(Map<String, Object> mapVal : mapList){
                    for(EnergyConfigcenterElectricityPrice price : priceList){
                        if(mapVal.get("rt").toString().equals(price.getMonth())){
                            mapVal.put("value",ArithUtil.accurateDecimal(countValue(item.getEneDataList(), mapVal.get("rt").toString(), price),2));
                        }
                    }
                }
            }else {
                //电价
                EnergyConfigcenterElectricityPrice dataPrice = priceList.get(0);
                for(String time : dateTimeList){
                    Map<String, Object> map = new HashMap<>();
                    map.put("rt",time);
                    map.put("id",item.getDeptCode());
                    map.put("name",item.getTunnelName());
                    map.put("value",ArithUtil.accurateDecimal(countValue(item.getEneDataList(), time, dataPrice),2));
                    mapList.add(map);
                }
            }
            list.add(mapList);
        }
        return list;
    }

    public double countValue(List<EnergyAnalysisElectricityBill> dataList, String dateTime, EnergyConfigcenterElectricityPrice dataPrice){
        double jPrice = getPrice(dataPrice, dataPrice.getJianCof().doubleValue());
        double gPrice = getPrice(dataPrice, dataPrice.getGuCof().doubleValue());
        double fPrice = getPrice(dataPrice, dataPrice.getFengCof().doubleValue());
        double sPrice = getPrice(dataPrice, dataPrice.getShenCof().doubleValue());
        double pPrice = getPrice(dataPrice,1.00);
        double priceValue = 0;
        for(EnergyAnalysisElectricityBill item : dataList){
            if(dateTime.equals(item.getRt())){
                //尖
                if("1220".equals(item.getSplitTimeType())){
                    priceValue += item.getValue().doubleValue() * jPrice;
                }
                //峰
                if("1221".equals(item.getSplitTimeType())){
                    priceValue += item.getValue().doubleValue() * fPrice;
                }
                //平
                if("1222".equals(item.getSplitTimeType())){
                    priceValue += item.getValue().doubleValue() * pPrice;
                }
                //谷
                if("1223".equals(item.getSplitTimeType())){
                    priceValue += item.getValue().doubleValue() * gPrice;
                }
                //深
                if("1224".equals(item.getSplitTimeType())){
                    priceValue += item.getValue().doubleValue() * sPrice;
                }
            }
        }
        if(priceValue == 0){
            return 0;
        }else {
            return priceValue;
        }
    }

    /**
     * 获取用电量
     * @param type
     * @param baseTime
     * @return
     */
    public List<EnergyConfigcenterElectricityPrice> selectPrice(String type, Date baseTime){
        List<EnergyConfigcenterElectricityPrice> list = new ArrayList<>();
        if ("year".equals(type)) {
            // 获取当年电价
            list.addAll(mapper.getListOfYear(baseTime));
        } else {
            // 获取当月电价
            list.add(mapper.getListOfMonth(baseTime));
        }
        return list;
    }

    /**
     * 计算每月天数、每年月数
     * @param time
     * @param type
     * @return
     */
    public static List<String> getDateTimeList(Date time, String type) {
        if (Objects.isNull(time) || Objects.isNull(type)) {
            return new ArrayList<>();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        calendar.setTime(time);
        List<String> result = new ArrayList<>();
        if ("day".equals(type)) {
            for (int i = 0; i < 24; i++) {
                calendar.set(Calendar.HOUR_OF_DAY, i);
                result.add(DateUtil.dateToStr(calendar.getTime(), "yyyy-MM-dd HH"));
            }
            /*calendar.add(Calendar.DAY_OF_MONTH, 1);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            result.add(DateUtil.dateToStr(calendar.getTime(), "yyyy-MM-dd HH"));*/
        }else if ("month".equals(type)) {
            int maxDay = DateUtil.getMaxDayOfMonth(time);
            for (int i = 1; i <= maxDay; i++) {
                calendar.set(Calendar.DAY_OF_MONTH, i);
                result.add(DateUtil.dateToStr(calendar.getTime(), "yyyy-MM-dd"));
            }
            //防止计算月份出错
            /*calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.add(Calendar.MONTH, 1);
            result.add(DateUtil.dateToStr(calendar.getTime(), "yyyy-MM-dd"));*/
        } else {
            //防止计算月份出错
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            for (int i = 0; i < 12; i++) {
                calendar.set(Calendar.MONTH, i);
                result.add(DateUtil.dateToStr(calendar.getTime(), "yyyy-MM"));
            }
            /*calendar.add(Calendar.YEAR, 1);
            calendar.set(Calendar.MONTH, 0);
            result.add(DateUtil.dateToStr(calendar.getTime(), "yyyy-MM"));*/
        }
        return result;
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

    /**
     * 能耗报表初始化表格展示
     * @param statisticType
     * @param baseTime
     * @param codeList
     * @return
     */
    public List<List<ElectricityData>> setDataInfo(Integer statisticType, Date baseTime, List<Map<String, Object>> codeList){
        List<List<ElectricityData>> dataList = new ArrayList<>();
        String type = "";
        if(statisticType == 0){
            type = "day";
        }else if(statisticType == 1){
            type = "month";
        }else {
            type = "year";
        }
        //获取时间
        List<String> dateData = getDateTimeList(baseTime, type);
        for(Map<String, Object> item : codeList){
            List<ElectricityData> list = new ArrayList<>();
            for(String date : dateData){
                ElectricityData electricityData = new ElectricityData();
                electricityData.setId(item.get("id").toString());
                electricityData.setRt(date);
                electricityData.setName(item.get("name").toString());
                electricityData.setValue(0.0);
                list.add(electricityData);
            }
            dataList.add(list);
        }
        return dataList;
    }
}
