package com.tunnel.business.service.bigScreenApiN.impl;

import com.tunnel.business.mapper.bigScreenApiN.SdOperateDataMapper;
import com.tunnel.business.mapper.bigScreenApiN.SdTunnelsDataMapper;
import com.tunnel.business.service.bigScreenApiN.ISdOperateDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SdOperateDataServiceImpl implements ISdOperateDataService {

    @Autowired
    private SdOperateDataMapper sdOperateDataMapper;


    @Autowired
    private SdTunnelsDataMapper sdTunnelsDataMapper;



    @Override
    public Map getIndexData() {
        Map<String,Object> resMap = new HashMap<>();

        resMap.put("todayFlowCount",sdOperateDataMapper.getIndexDataTodayFlowCount());
        resMap.put("todayFolwPercent",sdOperateDataMapper.getIndexDataTodayFolwPercent());
        resMap.put("serverCount","暂无");
        resMap.put("serverCountPercent","暂无");
        resMap.put("boardCount",sdOperateDataMapper.getIndexDataBoardCount());
        resMap.put("sevenDaysBoardPushCount",sdOperateDataMapper.getIndexDataSevenDaysBoardPushCount());
        resMap.put("boardPushCount",sdOperateDataMapper.getIndexDataBoardPushCount());

        return resMap;
    }

    @Override
    public Map getYesterdayFlowData() {
        Map<String,Object> resMap = new HashMap<>();

        // 各隧道 昨日 车流量
        Integer[] yeasterDayFlowDataList = sdOperateDataMapper.getYesterdayFlowDataTunnelsList();

        // 各隧道 周环比
        Integer[] yeasterDayFlowDataQoQList = sdOperateDataMapper.getYeasterDayFlowDataTunnelsQoQList();

        resMap.put("list",yeasterDayFlowDataList);

        resMap.put("qoQList",yeasterDayFlowDataQoQList);

        resMap.put("xData",sdTunnelsDataMapper.getAllTunnelsList());
        return resMap;
    }


    @Override
    public Map getEventData() {
        Map<String,Object> resMap = new HashMap<>();
        resMap.put("list",sdOperateDataMapper.getEventDataList());
        return resMap;
    }


}
