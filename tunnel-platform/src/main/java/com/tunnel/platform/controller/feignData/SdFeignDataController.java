package com.tunnel.platform.controller.feignData;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.ruoyi.common.core.controller.BaseController;
import com.tunnel.business.domain.dataInfo.SdEquipmentState;
import com.tunnel.business.domain.event.SdStrategy;
import com.tunnel.business.domain.event.SdStrategyRl;
import com.tunnel.business.service.dataInfo.ISdEquipmentStateService;
import com.tunnel.business.service.event.ISdStrategyRlService;
import com.tunnel.platform.service.event.ISdStrategyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "策略关联设备信息Controller")
@ApiSupport(order = 16)
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
    @ApiOperation("查询控制策略")
    public SdStrategy queryByJobReletaionId(@PathVariable String jobRelationId)
    {
    	return sdStrategyService.selectSdStrategyByJobRelationId(jobRelationId);
    }
    /**
     * 查询策略关联设备信息列表
     */
    @PostMapping("/strategyRl/list")
    @ApiOperation("查询策略关联设备信息列表")
    public List<SdStrategyRl> list(@RequestBody SdStrategyRl sdStrategyRl)
    {
        List<SdStrategyRl> list = sdStrategyRlService.selectSdStrategyRlList(sdStrategyRl);
        return list;
    }
    /**
     * 查询设备类型状态关系列表
     */
    @PostMapping("/equipmentState/list")
    @ApiOperation("查询设备类型状态关系列表")
    public List<SdEquipmentState> list(SdEquipmentState sdEquipmentState)
    {
        List<SdEquipmentState> list = sdEquipmentStateService.selectSdEquipmentStateList(sdEquipmentState);
        return list;
    }
    
}