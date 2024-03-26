package com.tunnel.platform.controller.energyManagement;


import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.tunnel.business.datacenter.domain.enumeration.StatisticTypeEnum;
import com.tunnel.business.datacenter.domain.enumeration.TunnelEnum;
import com.tunnel.business.datacenter.util.ArithUtil;
import com.tunnel.business.domain.dataInfo.SdTunnels;
import com.tunnel.business.domain.energyManagement.*;
import com.tunnel.business.mapper.dataInfo.SdTunnelsMapper;
import com.tunnel.business.mapper.energyManagement.EnergySjfxElectricityMapper;
import com.tunnel.business.mapper.energyManagement.SdEnergyConfigcenterClassificationMapper;
import com.tunnel.business.mapper.energyManagement.SdEnergyConfigcenterItemizedMapper;
import com.tunnel.business.service.dataInfo.ISdTunnelsService;
import com.tunnel.business.service.energyManagement.EnergySiteService;
import com.tunnel.business.service.energyManagement.EnergySjfxElectricityService;
import com.tunnel.business.utils.util.DateUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

/**
 * EnergySjfxElectricityImpl
 *
 * @date: 2023/7/17 15:45
 * @author: tjw
 * @version: 1.0
 */
@RequestMapping("/energySiteController")
@RestController
public class EnergySiteController extends BaseController {

    @Autowired
    private EnergySiteService energySiteService;

    /**
     * 查询昨天今天实时用电量
     * @param energySjfx 能源分析电力账单实体
     * @return
     */
    @ApiOperation("查询昨天今天实时用电量")
    @GetMapping("/getEnergySiteList")
    public TableDataInfo getEnergySiteList(EnergySjfx energySjfx){

        return getDataTable(energySiteService.getEnergySiteList(energySjfx));
    }
}
