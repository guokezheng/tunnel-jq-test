package com.tunnel.platform.service.bigScreenApi.impl;

import com.tunnel.platform.mapper.bigScreenApi.SdAlarmNumberMapper;
import com.tunnel.platform.service.bigScreenApi.ISdAlarmNumberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SdAlarmNumberServiceImpl implements ISdAlarmNumberService {

    private static final Logger log = LoggerFactory.getLogger(SdAlarmNumberServiceImpl.class);

    @Autowired
    private SdAlarmNumberMapper sdAlarmNumberMapper;

    @Override
    public Map<String, Object> getTodayWarningmsg() {
        List<Map<String, Object>> todayWarningmsg = sdAlarmNumberMapper.getTodayWarningmsg();
        Map<String, Object> map = new HashMap<>();
        map.put("name", "预警信息");
        map.put("all", todayWarningmsg.size());
        map.put("hasDisposal","0");
        map.put("notDDisposedOf","0");
        List<Map<String, Object>> todayWarningmsgStatus = sdAlarmNumberMapper.getTodayWarningmsgStatus();
        for (int i = 0;i < todayWarningmsgStatus.size();i++){
            Map<String, Object> warningStatusMsg = todayWarningmsgStatus.get(i);
            String ps = warningStatusMsg.get("ps").toString();
            System.err.println("ps" + ps);
            if (!ps.isEmpty() && ps != null){
                if (ps.equals("1")){
                    map.put("hasDisposal",warningStatusMsg.get("number").toString());
                } else if (ps.equals("0")){
                    map.put("notDDisposedOf",warningStatusMsg.get("number").toString());
                }
            }
        }
        map.put("listData", todayWarningmsg);
        log.info("查询当日总预警信息成功");
        return map;
    }

    @Override
    public Map<String, Object> getTodayAccidentmsg() {
        List<Map<String, Object>> todayAccidentmsg = sdAlarmNumberMapper.getTodayAccidentmsg();
        Map<String, Object> map = new HashMap<>();
        map.put("name", "事件事故");
        map.put("all", todayAccidentmsg.size());
        map.put("hasDisposal","0");
        map.put("notDDisposedOf","0");
        List<Map<String, Object>> todayAccidentmsgStatus = sdAlarmNumberMapper.getTodayAccidentmsgStatus();
        for (int i = 0;i < todayAccidentmsgStatus.size();i++){
            Map<String, Object> accidentStatusMsg = todayAccidentmsgStatus.get(i);
            String ps = accidentStatusMsg.get("ps").toString();
            System.err.println("ps" + ps);
            if (!ps.isEmpty() && ps != null){
                if (ps.equals("1")){
                    map.put("hasDisposal",accidentStatusMsg.get("number").toString());
                } else if (ps.equals("0")){
                    map.put("notDDisposedOf",accidentStatusMsg.get("number").toString());
                }
            }
        }
        map.put("listData", todayAccidentmsg);
        log.info("查询当日总事件事故信息成功");
        return map;
    }
}
