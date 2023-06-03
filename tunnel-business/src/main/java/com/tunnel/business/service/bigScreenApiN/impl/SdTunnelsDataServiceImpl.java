package com.tunnel.business.service.bigScreenApiN.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.tunnel.business.datacenter.domain.enumeration.PrevControlTypeEnum;
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

@Service
public class SdTunnelsDataServiceImpl implements ISdTunnelsDataService {

    @Autowired
    private SdTunnelsDataMapper sdTunnelsDataMapper;


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

        Map<String,Object> resMap = new HashMap<>();
        resMap.put("count",sdTunnelsDataMapper.getMajorEventDataCount());
        resMap.put("warnCount",sdTunnelsDataMapper.getMajorEventDataWarnCount());
        resMap.put("handleWarnCount",sdTunnelsDataMapper.getMajorEventDataHandleWarnCount());
        resMap.put("eventCount",sdTunnelsDataMapper.getMajorEventDataEventCount());
        resMap.put("handleEventCount",sdTunnelsDataMapper.getMajorEventDataHandleEventCount());
        resMap.put("list",sdTunnelsDataMapper.getMajorEventDataList());

        return resMap;
    }

    @Override
    public Map getEventStatData() {

        Map<String,Object> resMap = new HashMap<>();

        // 获取所有隧道列表
        String [] tunnelsNameList = sdTunnelsDataMapper.getAllTunnelsList();

        // 获取所有事件类型
        List<Map<String,Object>> eventTypeList = sdTunnelsDataMapper.getListByPrevControlTypeId(PrevControlTypeEnum.TRAFFIC_NCIDENT.getCode());

        // 获取每个事件类型的总数
        for(Map map : eventTypeList){

            Integer eventTypeId = Integer.parseInt(map.get("eventTypeId").toString());
            String eventTypeName = map.get("simplifyName").toString();

            resMap.put(toPinyin(eventTypeName), sdTunnelsDataMapper.getEventStatMonthDataByEventTypeId(eventTypeId));
        }

        resMap.put("xData",tunnelsNameList);

        return resMap;
    }

    @Override
    public Map getFindEventStatData() {
        Map<String,Object> resMap = new HashMap<>();

        // 获取所有事件类型
        List<Map<String,Object>> eventTypeList = sdTunnelsDataMapper.getListByPrevControlTypeId(PrevControlTypeEnum.TRAFFIC_NCIDENT.getCode());

        // 获取每个事件类型 周的数据集
        for(Map map : eventTypeList){

            Integer eventTypeId = Integer.parseInt(map.get("eventTypeId").toString());
            String eventTypeName = map.get("simplifyName").toString();
            Integer [] resStr = sdTunnelsDataMapper.getEventStatWeekDataByEventTypeId(eventTypeId);

            resMap.put(toPinyin(eventTypeName),resStr);
        }

        return resMap;
    }

    @Override
    public Map getCarFlowData() {
        Map<String,Object> resMap = new HashMap<>();

        // 今年车流量总数
        Integer thisYearCount = sdTunnelsDataMapper.getCarFlowDataThisYearCount();
        // 去年车流量总数
        Integer lastYearCount = sdTunnelsDataMapper.getCarFlowDataLastYearCount();

        // 同比 增长率
        String yearPercent = thisYearCount + "";
        if(lastYearCount != 0){
            // 同比 增长率
             yearPercent = new BigDecimal(thisYearCount).divide(new BigDecimal((lastYearCount)), 2, RoundingMode.HALF_UP).toString();
        }

        resMap.put("thisYearCount",thisYearCount);
        resMap.put("lastYearCount",lastYearCount);
        resMap.put("yearPercent",yearPercent);

        resMap.put("topList",sdTunnelsDataMapper.getCarFlowDataTopList());

        resMap.put("weekList",sdTunnelsDataMapper.getCarFlowDataWeekList());
        resMap.put("lastYearList",sdTunnelsDataMapper.getCarFlowDataLastYearList());

        List<Map<String, Object>> fourHoursList = sdTunnelsDataMapper.getCarFlowDataFourHoursList();

        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY) + 1;
        String fourHour = new BigDecimal(hour).divide(new BigDecimal(4), 0, RoundingMode.UP).toString();
        resMap.put("fourHoursList", CollectionUtil.sub(fourHoursList , 0,Integer.parseInt(fourHour)));

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
