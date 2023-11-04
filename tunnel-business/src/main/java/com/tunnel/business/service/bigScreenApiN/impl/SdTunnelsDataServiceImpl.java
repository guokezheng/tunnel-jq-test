package com.tunnel.business.service.bigScreenApiN.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.SecurityUtils;
import com.tunnel.business.datacenter.domain.enumeration.SdEventTypeEnum;
import com.tunnel.business.datacenter.domain.enumeration.PrevControlTypeEnum;
import com.tunnel.business.domain.dataInfo.SdTunnels;
import com.tunnel.business.mapper.bigScreenApiN.SdTunnelsDataMapper;
import com.tunnel.business.mapper.dataInfo.SdTunnelsMapper;
import com.tunnel.business.service.bigScreenApiN.ISdTunnelsDataService;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SdTunnelsDataServiceImpl implements ISdTunnelsDataService {

    @Autowired
    private SdTunnelsDataMapper sdTunnelsDataMapper;

    @Autowired
    private SdTunnelsMapper tunnelsMapper;

    @Autowired
    private RedisCache redisCache;


    @Override
    public Map getIndexData() {
        Map<String,Object> resMap = new HashMap<>();
        resMap.put("twentyFourHoursEventCount",sdTunnelsDataMapper.getIndexData24HoursEventCount());
        resMap.put("eventCount",sdTunnelsDataMapper.getIndexDataEventCount());
        resMap.put("eventSourceList",sdTunnelsDataMapper.getIndexDataEventSourceList());
        resMap.put("travelVehicleFlowCount",sdTunnelsDataMapper.getIndexDataTravelVehicleFlowCount());
        resMap.put("travelVehicleFlowList",sdTunnelsDataMapper.getIndexDataTravelVehicleFlowList());
        resMap.put("importantVehicleCount",sdTunnelsDataMapper.getIndexDataImportantVehicleCount());
        resMap.put("importantVehicleList",sdTunnelsDataMapper.getIndexDataImportantVehicleList());
        return resMap;
    }

    @Override
    public Map getMajorEventData() {
        String deptId = SecurityUtils.getDeptId();
        Map<String,Object> resMap = new HashMap<>();
        resMap.put("count",sdTunnelsDataMapper.getMajorEventDataCount(deptId));
        resMap.put("warnCount",sdTunnelsDataMapper.getMajorEventDataWarnCount(deptId));
        resMap.put("handleWarnCount",sdTunnelsDataMapper.getMajorEventDataHandleWarnCount(deptId));
        resMap.put("eventCount",sdTunnelsDataMapper.getMajorEventDataEventCount(deptId));
        resMap.put("handleEventCount",sdTunnelsDataMapper.getMajorEventDataHandleEventCount(deptId));
        resMap.put("list",sdTunnelsDataMapper.getMajorEventDataList(deptId));

        return resMap;
    }

    @Override
    public Map getEventStatData() {

        Map<String,Object> resMap = new HashMap<>();
        String deptId = SecurityUtils.getDeptId();
        List<SdTunnels> sdTunnelsList = tunnelsMapper.selectTunnelsDeptIdList(deptId);
        List<String> collect = sdTunnelsList.stream().map(SdTunnels::getTunnelName).collect(Collectors.toList());
        // 获取所有隧道列表
        String [] tunnelsNameList = collect.toArray(new String[collect.size()]);
        Integer[] integers1 = new Integer[tunnelsNameList.length];
        Integer[] integers2 = new Integer[tunnelsNameList.length];
        Integer[] integers3 = new Integer[tunnelsNameList.length];
        Integer[] integers4 = new Integer[tunnelsNameList.length];
        Integer[] integers5 = new Integer[tunnelsNameList.length];
        Integer[] integers6 = new Integer[tunnelsNameList.length];
        Integer[] integers7 = new Integer[tunnelsNameList.length];
        Integer[] integers8 = new Integer[tunnelsNameList.length];
        for(int i = 0; i < sdTunnelsList.size(); i++){
            String tunnelId = sdTunnelsList.get(i).getTunnelId();
            Integer nx = sdTunnelsDataMapper.getEventStatMonthDataByEventTypeId(SdEventTypeEnum.NI_XING.getCode(), tunnelId);
            Integer tc = sdTunnelsDataMapper.getEventStatMonthDataByEventTypeId(SdEventTypeEnum.TING_CHE.getCode(), tunnelId);
            Integer mx = sdTunnelsDataMapper.getEventStatMonthDataByEventTypeId(SdEventTypeEnum.MAN_XING.getCode(), tunnelId);
            Integer bd = sdTunnelsDataMapper.getEventStatMonthDataByEventTypeId(SdEventTypeEnum.BIAN_DAO.getCode(), tunnelId);
            Integer cs = sdTunnelsDataMapper.getEventStatMonthDataByEventTypeId(SdEventTypeEnum.CHAO_SU.getCode(), tunnelId);
            Integer yjcd = sdTunnelsDataMapper.getEventStatMonthDataByEventTypeId(SdEventTypeEnum.YING_JI_CHE_DAO.getCode(), tunnelId);
            Integer hz = sdTunnelsDataMapper.getEventStatMonthDataByEventTypeId(SdEventTypeEnum.HUO_ZAI.getCode(), tunnelId);
            Integer dh = sdTunnelsDataMapper.getEventStatMonthDataByEventTypeId(SdEventTypeEnum.DIAN_HUA.getCode(), tunnelId);
            integers1[i] = nx == null ? 0 : nx;
            integers2[i] = tc == null ? 0 : tc;
            integers3[i] = mx == null ? 0 : mx;
            integers4[i] = bd == null ? 0 : bd;
            integers5[i] = cs == null ? 0 : cs;
            integers6[i] = yjcd == null ? 0 : yjcd;
            integers7[i] = hz == null ? 0 : hz;
            integers8[i] = dh == null ? 0 : dh;
        }
        resMap.put(toPinyin(SdEventTypeEnum.NI_XING.getName()),integers1);
        resMap.put(toPinyin(SdEventTypeEnum.TING_CHE.getName()),integers2);
        resMap.put(toPinyin(SdEventTypeEnum.MAN_XING.getName()),integers3);
        resMap.put(toPinyin(SdEventTypeEnum.BIAN_DAO.getName()),integers4);
        resMap.put(toPinyin(SdEventTypeEnum.CHAO_SU.getName()),integers5);
        resMap.put(toPinyin(SdEventTypeEnum.YING_JI_CHE_DAO.getName()),integers6);
        resMap.put(toPinyin(SdEventTypeEnum.HUO_ZAI.getName()),integers7);
        resMap.put(toPinyin(SdEventTypeEnum.DIAN_HUA.getName()),integers8);
        resMap.put("xData",tunnelsNameList);

        return resMap;
    }

    @Override
    public Map getFindEventStatData() {
        Map<String,Object> resMap = new HashMap<>();
        String deptId = SecurityUtils.getDeptId();
        Integer[] integers = new Integer[]{0,0,0,0,0,0,0};
        // 获取每个事件类型 周的数据集
        Integer[] integers1 = sdTunnelsDataMapper.getEventStatWeekDataByEventTypeId(SdEventTypeEnum.NI_XING.getCode(), deptId);
        Integer[] integers2 = sdTunnelsDataMapper.getEventStatWeekDataByEventTypeId(SdEventTypeEnum.TING_CHE.getCode(), deptId);
        Integer[] integers3 = sdTunnelsDataMapper.getEventStatWeekDataByEventTypeId(SdEventTypeEnum.MAN_XING.getCode(), deptId);
        Integer[] integers4 = sdTunnelsDataMapper.getEventStatWeekDataByEventTypeId(SdEventTypeEnum.BIAN_DAO.getCode(), deptId);
        Integer[] integers5 = sdTunnelsDataMapper.getEventStatWeekDataByEventTypeId(SdEventTypeEnum.CHAO_SU.getCode(), deptId);
        Integer[] integers6 = sdTunnelsDataMapper.getEventStatWeekDataByEventTypeId(SdEventTypeEnum.YING_JI_CHE_DAO.getCode(), deptId);
        Integer[] integers7 = sdTunnelsDataMapper.getEventStatWeekDataByEventTypeId(SdEventTypeEnum.HUO_ZAI.getCode(), deptId);
        Integer[] integers8 = sdTunnelsDataMapper.getEventStatWeekDataByEventTypeId(SdEventTypeEnum.DIAN_HUA.getCode(), deptId);
        resMap.put(toPinyin(SdEventTypeEnum.NI_XING.getName()),integers1 == null ? integers : integers1);
        resMap.put(toPinyin(SdEventTypeEnum.TING_CHE.getName()),integers2 == null ? integers : integers2);
        resMap.put(toPinyin(SdEventTypeEnum.MAN_XING.getName()),integers3 == null ? integers : integers3);
        resMap.put(toPinyin(SdEventTypeEnum.BIAN_DAO.getName()),integers4 == null ? integers : integers4);
        resMap.put(toPinyin(SdEventTypeEnum.CHAO_SU.getName()),integers5 == null ? integers : integers5);
        resMap.put(toPinyin(SdEventTypeEnum.YING_JI_CHE_DAO.getName()),integers6 == null ? integers : integers6);
        resMap.put(toPinyin(SdEventTypeEnum.HUO_ZAI.getName()),integers7 == null ? integers : integers7);
        resMap.put(toPinyin(SdEventTypeEnum.DIAN_HUA.getName()),integers8 == null ? integers : integers8);
        return resMap;
    }

    @Override
    public Map getCarFlowData() {
        Map<String,Object> resMap = new HashMap<>();
        String deptId = SecurityUtils.getDeptId();
        //查询今日车流量top
        List<Map<String, Object>> tunnelTopCarNum = sdTunnelsDataMapper.getTunnelCarNum(deptId);
        //查询车流量数据
        List<Map<String, Integer>> carNumList = sdTunnelsDataMapper.getCarFlowDataList(deptId);
        String[] xData = new String[carNumList.size()];
        String[] yData = new String[carNumList.size()];
        long dayCarNum = 0;
        for(int i = 0; i < carNumList.size(); i++){
            xData[i] = carNumList.get(i).get("order_hour").toString();
            yData[i] = carNumList.get(i).get("count").toString();
            dayCarNum = dayCarNum + carNumList.get(i).get("count");
        }
        resMap.put("dayCarNum",dayCarNum);
        resMap.put("xData",xData);
        resMap.put("yData",yData);
        resMap.put("tunnelTop",tunnelTopCarNum);
        return resMap;
    }

    @Override
    public Map getRealTimeCarFlow() {
        Map<String,Object> resMap = new HashMap<>();

        // 获取所有隧道列表
        String [] tunnelsNameList = sdTunnelsDataMapper.getAllTunnelsList();

        //获取 所有车辆类型
        List<Map<String,Object>> vehicleTypeList = sdTunnelsDataMapper.getVehicleTypeList();

        List<Map> seriesList = new ArrayList<>();
        // 获取每个车辆类型 隧道数据集
        for(Map map : vehicleTypeList){
            Map<String,Object> seriesMap = new HashMap<>();
            Integer vehicleTypeId = Integer.parseInt(map.get("vehicleTypeId").toString());
            String vehicleTypeName = map.get("vehicleTypeName").toString();
            seriesMap.put("name",vehicleTypeName);
            seriesMap.put("data", sdTunnelsDataMapper.getRealTimeCarFlowByVehicleTypeId(vehicleTypeId));
            seriesList.add(seriesMap);
        }
        resMap.put("series",seriesList);
        resMap.put("xData",tunnelsNameList);

        return resMap;
    }

    @Override
    public Map<String, Object> realTimeCongestion() {
        Map<String, Object> map = new HashMap<>();
        String deptId = SecurityUtils.getDeptId();
        List<SdTunnels> sdTunnelsList = tunnelsMapper.selectTunnelsDeptIdList(deptId);
        List<String> collect = sdTunnelsList.stream().map(SdTunnels::getTunnelName).collect(Collectors.toList());
        // 获取所有隧道列表
        String [] tunnelsNameList = collect.toArray(new String[collect.size()]);
        // 隧道数据
        String[] tunnelCongDatay = new String[sdTunnelsList.size()];
        int count = 0;
        for(SdTunnels item : sdTunnelsList){
            String redisKeyw = "carVolume:" + item.getTunnelId() + ":" + "1";
            String redisKeyj = "carVolume:" + item.getTunnelId() + ":" + "2";
            Object dataw = redisCache.getCacheObject("redisKeyw");
            Object dataj = redisCache.getCacheObject("redisKeyj");
            int carNum = 0;
            int num = 0;
            if(dataw != null){
                carNum = carNum + Integer.valueOf(dataw.toString());
                num++;
            }
            if(dataj != null){
                carNum = carNum + Integer.valueOf(dataj.toString());
                num++;
            }
            if(carNum == 0){
                tunnelCongDatay[count] = carNum+"";
            }
            //结束桩号
            Integer endpile = Integer.valueOf(item.getEndPileNum());
            //开始桩号
            Integer startPile = Integer.valueOf(item.getStartPileNum());
            //隧道长度
            BigDecimal tunnelNum = new BigDecimal(endpile - startPile);
            //瞬时隧道最大承受车流量(上行/下行) 公式: 隧道长度/100*3个车道+应急停车带数量
            //(上行/下行) 交通负荷公式: 隧道内车辆数/瞬时隧道最大承受车流量 (上行/下行)
            BigDecimal maxCarNum = tunnelNum.divide(BigDecimal.valueOf(100),0,BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(3)).add(BigDecimal.valueOf(getParkingStripNum(item.getTunnelId()) * num));
            BigDecimal divide = new BigDecimal(carNum).divide(maxCarNum,2,BigDecimal.ROUND_HALF_UP);
            tunnelCongDatay[count] = divide+"";
            count++;
        }
        map.put("tunnelList",tunnelsNameList);
        map.put("tunnelData",tunnelCongDatay);
        return map;
    }

    public int getParkingStripNum(String tunnelId){

        switch (tunnelId){
            case "JQ-JiNan-WenZuBei-MJY" : return 3;
            case "JQ-ZiBo-TaiHe-PDS" : return 5;
            case "JQ-ZiBo-TaiHe-QFL" : return 2;
            case "JQ-WeiFang-MiaoZi-BJY" : return 2;
            case "JQ-WeiFang-MiaoZi-WCL" : return 3;
            case "JQ-WeiFang-YangTianShan-SZS" : return 2;
            case "JQ-WeiFang-YangTianShan-YTS" : return 1;
            case "JQ-WeiFang-JiuLongYu-MAS" : return 2;
            case "JQ-WeiFang-JiuLongYu-JJL" : return 1;
            case "JQ-WeiFang-JiuLongYu-HSD" : return 1;
            default: return 0;
        }
    }

    /**
     * 汉字转为拼音
     */
    public static String toPinyin(String chinese){
        String pinyinStr = "";
        char[] newChar = chinese.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < newChar.length; i++) {
            if (newChar[i] > 128) {
                try {
                    pinyinStr += PinyinHelper.toHanyuPinyinStringArray(newChar[i], defaultFormat)[0];
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            }else{
                pinyinStr += newChar[i];
            }
        }
        return pinyinStr;
    }
}
