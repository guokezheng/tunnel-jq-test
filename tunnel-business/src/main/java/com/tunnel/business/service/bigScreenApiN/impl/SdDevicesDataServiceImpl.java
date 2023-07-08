package com.tunnel.business.service.bigScreenApiN.impl;

import com.tunnel.business.mapper.bigScreenApiN.SdDevicesDataMapper;
import com.tunnel.business.mapper.bigScreenApiN.SdTunnelsDataMapper;
import com.tunnel.business.service.bigScreenApiN.ISdDevicesDataService;
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
public class SdDevicesDataServiceImpl implements ISdDevicesDataService {

    @Autowired
    private SdDevicesDataMapper sdDevicesDataMapper;

    @Override
    public Map getIndexData() {
        Map<String,Object> resMap = new HashMap<>();

        // 机电总数
        resMap.put("count",sdDevicesDataMapper.getIndexDataCount());
        // 预警总数
        resMap.put("warnCount",sdDevicesDataMapper.getIndexDataWarnCount());
        // 在修总数
        resMap.put("repairCount",sdDevicesDataMapper.getIndexDataRepairCount());

        return resMap;
    }

    @Override
    public Map getFaultWarnData() {
        Map<String,Object> resMap = new HashMap<>();

        resMap.put("list",sdDevicesDataMapper.getFaultWarnDataList());
        return resMap;
    }

    @Override
    public Map getRealTimeStatData() {
        Map<String,Object> resMap = new HashMap<>();

        resMap.put("normalList",sdDevicesDataMapper.getRealTimeStatDataNormalList());
        resMap.put("errorList",sdDevicesDataMapper.getRealTimeStatDataErrorList());
        resMap.put("xData",sdDevicesDataMapper.getRealTimeStatDataEquipments());

        return resMap;
    }

    @Override
    public Map getMonthFaultData() {
        Map<String,Object> resMap = new HashMap<>();

        Integer[] devices = sdDevicesDataMapper.getMonthFaultDataDevices();
        Integer[] faults = sdDevicesDataMapper.getMonthFaultDataFaults();
        resMap.put("devices", Arrays.copyOf(devices,Calendar.getInstance().get(Calendar.MONDAY)+1));
        resMap.put("faults",Arrays.copyOf(faults,Calendar.getInstance().get(Calendar.MONDAY)+1));

        String [] percent = new String[devices.length];

        // 计算故障率
        for(int i = 0; i < devices.length; i ++){
            percent[i] = new BigDecimal(faults[i]).divide(new BigDecimal(devices[i]), 2, RoundingMode.UP).toString();
        }

        resMap.put("percent",Arrays.copyOf(percent,Calendar.getInstance().get(Calendar.MONDAY)+1));

        return resMap;
    }

    @Override
    public Map getFaultTimeTopData() {
        Map<String,Object> resMap = new HashMap<>();

        resMap.put("list",sdDevicesDataMapper.getFaultTimeTopDataList());

        resMap.put("xData",sdDevicesDataMapper.getFaultTimeTopDataEqList());
        return resMap;
    }

    @Override
    public Map getEqPercentData() {
        Map<String,Object> resMap = new HashMap<>();

        resMap.put("list",sdDevicesDataMapper.getEqPercentData());
        return resMap;
    }


    @Override
    public Map getFaultCategoryData() {
        Map<String,Object> resMap = new HashMap<>();

        resMap.put("monthList",sdDevicesDataMapper.getFaultCategoryMonthData());

        resMap.put("sixMonthsList",sdDevicesDataMapper.getFaultCategorySixMonthsData());

        return resMap;
    }

}
