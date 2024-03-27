package com.tunnel.platform.controller.digitalTwinsApi;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.ruoyi.common.core.domain.AjaxResult;
import com.tunnel.business.service.bigScreenApi.SdEquipmentApiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 万集接口
 * @author zhai
 * @date 2022/11/15
 */
@RestController
@RequestMapping("/equipmentApi")
@Api(tags = "提供给万集接口")
@ApiSupport(order = 16)
public class SdEquipmentApiController {

    @Autowired
    private SdEquipmentApiService sdEquipmentApiService;

    /**
     * 设备故障状况统计
     * @param tunnelId
     * @return
     */
    @ApiOperation("设备故障状况统计")
    @GetMapping("/getEquipmentFault")
    public AjaxResult getEquipmentFault(String tunnelId){
        return sdEquipmentApiService.getEquipmentFault(tunnelId);
    }

    /**
     * 未处理故障统计
     * @param tunnelId
     * @return
     */
    @ApiOperation("未处理故障统计")
    @GetMapping("/getUnhandledFault")
    public AjaxResult getUnhandledFault(String tunnelId){
        return sdEquipmentApiService.getUnhandledFault(tunnelId);
    }

    /**
     * 获取设备类型
     * @param tunnelId
     * @return
     */
    @ApiOperation("获取设备类型")
    @GetMapping("/getEquipmentType")
    public AjaxResult getEquipmentType(String tunnelId){
        return sdEquipmentApiService.getEquipmentType(tunnelId);
    }
}
