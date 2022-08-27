package com.tunnel.platform.bigScreenApi.service.fullView.impl;

import com.tunnel.platform.bigScreenApi.mapper.fullView.SdProportionOfEquipmentMapper;
import com.tunnel.platform.bigScreenApi.service.fullView.ISdProportionOfEquipmentServcie;
import com.tunnel.platform.domain.dataInfo.SdTunnels;
import com.tunnel.platform.mapper.dataInfo.SdTunnelsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SdProportionOfEquipmentServcieImpl implements ISdProportionOfEquipmentServcie {

    private static final Logger log = LoggerFactory.getLogger(SdProportionOfEquipmentServcieImpl.class);

    @Autowired
    private SdProportionOfEquipmentMapper sdProportionOfEquipmentMapper;
    @Autowired
    private SdTunnelsMapper sdTunnelsMapper;

    @Override
    public List<Map<String, Object>> getProportionOfEquipment(String tunnelId) {
        log.info("调用设备占比查询接口成功");
        if (tunnelId == null || tunnelId.equals("")) {
            SdTunnels sdTunnels = new SdTunnels();
            List<SdTunnels> sdTunnelsList = sdTunnelsMapper.selectSdTunnelsList(sdTunnels);
            tunnelId = sdTunnelsList.get(0).getTunnelId();
        }
        return sdProportionOfEquipmentMapper.getProportionOfEquipment(tunnelId);
    }
}
