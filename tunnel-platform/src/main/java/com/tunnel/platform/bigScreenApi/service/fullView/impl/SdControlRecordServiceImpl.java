package com.tunnel.platform.bigScreenApi.service.fullView.impl;

import com.tunnel.platform.bigScreenApi.mapper.fullView.SdControlRecordMapper;
import com.tunnel.platform.bigScreenApi.service.fullView.ISdControlRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SdControlRecordServiceImpl implements ISdControlRecordService {

    private static final Logger log = LoggerFactory.getLogger(SdControlRecordServiceImpl.class);

    @Autowired
    private SdControlRecordMapper sdControlRecordMapper;

    @Override
    public List<Map<String, Object>> getRecentControlRecordMsg(String tunnelId) {
        List<Map<String, Object>> recentControlRecordMsg = sdControlRecordMapper.getRecentControlRecordMsg(tunnelId);
        log.info("查询近12小时控制记录成功");
        return recentControlRecordMsg;
    }
}
