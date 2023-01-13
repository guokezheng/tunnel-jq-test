package com.tunnel.business.service.bigScreenApi.impl;

import com.tunnel.business.mapper.bigScreenApi.SdTunnelWarningMapper;
import com.tunnel.business.service.bigScreenApi.ISdTunnelWarningService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SdTunnelWarningServiceImpl implements ISdTunnelWarningService {

    private static final Logger log = LoggerFactory.getLogger(SdTunnelWarningServiceImpl.class);

    @Autowired
    private SdTunnelWarningMapper sdTunnelWarningMapper;

    @Override
    public List<Long> getTunnelWarningNumber(String tunnelId) {
        List<Long> tunnelWarningNumber = sdTunnelWarningMapper.getTunnelWarningNumber(tunnelId);
        log.info("查询近30日隧道预警事件成功");
        return tunnelWarningNumber;
    }
}
