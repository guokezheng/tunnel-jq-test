package com.tunnel.business.service.energyManagement.impl;

import com.tunnel.business.domain.energyManagement.EnergySjfx;
import com.tunnel.business.mapper.energyManagement.SdEnergyDataMapper;
import com.tunnel.business.service.energyManagement.EnergySiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class EnergySiteServiceImpl implements EnergySiteService {

    @Autowired
    private SdEnergyDataMapper sdEnergyDataMapper;

    /**
     * 查询昨天今天实时用电量
     * @param energySjfx 能源分析电力账单实体
     * @return
     */
    @Override
    public List<EnergySjfx> getEnergySiteList(EnergySjfx energySjfx) {
        return sdEnergyDataMapper.getEnergySiteList(energySjfx);
    }
}
