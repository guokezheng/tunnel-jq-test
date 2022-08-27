package com.tunnel.platform.controller.feignData;

import com.tunnel.platform.domain.dataInfo.SdEquipmentState;
import com.tunnel.platform.domain.event.SdStrategy;
import com.tunnel.platform.domain.event.SdStrategyRl;
import com.tunnel.platform.service.dataInfo.ISdEquipmentStateService;
import com.tunnel.platform.service.event.ISdStrategyRlService;
import com.tunnel.platform.service.event.ISdStrategyService;
import com.ruoyi.common.core.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 策略关联设备信息Controller
 * 
 * @author gongfanfei
 * @date 2020-08-31
 */
@RestController
@RequestMapping("/feignData")
public class SdFeignDataController extends BaseController
{
	@Autowired
    private ISdStrategyService sdStrategyService;
    @Autowired
    private ISdStrategyRlService sdStrategyRlService;
    @Autowired
    private ISdEquipmentStateService sdEquipmentStateService;
    /**
     * 查询控制策略
     */
    @GetMapping("/strategy/getInfo/{jobRelationId}")
    public SdStrategy queryByJobReletaionId(@PathVariable String jobRelationId)
    {
    	return sdStrategyService.selectSdStrategyByJobRelationId(jobRelationId);
    }
    /**
     * 查询策略关联设备信息列表
     */
    @PostMapping("/strategyRl/list")
    public List<SdStrategyRl> list(@RequestBody SdStrategyRl sdStrategyRl)
    {
        List<SdStrategyRl> list = sdStrategyRlService.selectSdStrategyRlList(sdStrategyRl);
        return list;
    }
    /**
     * 查询设备类型状态关系列表
     */
    @PostMapping("/equipmentState/list")
    public List<SdEquipmentState> list(SdEquipmentState sdEquipmentState)
    {
        List<SdEquipmentState> list = sdEquipmentStateService.selectSdEquipmentStateList(sdEquipmentState);
        return list;
    }
    
}