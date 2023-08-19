package com.tunnel.business.service.energyManagement;

import com.tunnel.business.domain.energyManagement.EnergyAnalysisElectricityBill;
import com.tunnel.business.domain.energyManagement.EnergySjfx;

import java.util.List;

public interface EnergySiteService {

    /**
     * 查询昨天今天实时用电量
     * @param energySjfx 能耗站点实体
     * @return
     */
    public List<EnergySjfx> getEnergySiteList(EnergySjfx energySjfx);
}
