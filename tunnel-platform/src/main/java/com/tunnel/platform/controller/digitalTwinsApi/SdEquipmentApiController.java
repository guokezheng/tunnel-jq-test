package com.tunnel.platform.controller.digitalTwinsApi;

import com.ruoyi.common.core.domain.AjaxResult;
import com.tunnel.business.service.bigScreenApi.SdEquipmentApiService;
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
public class SdEquipmentApiController {

    @Autowired
    private SdEquipmentApiService sdEquipmentApiService;

    /**
     * 设备故障状况统计
     * @param tunnelId
     * @return
     */
    @GetMapping("/getEquipmentFault")
    public AjaxResult getEquipmentFault(String tunnelId){
        return sdEquipmentApiService.getEquipmentFault(tunnelId);
    }

    /**
     * 未处理故障统计
     * @param tunnelId
     * @return
     */
    @GetMapping("/getUnhandledFault")
    public AjaxResult getUnhandledFault(String tunnelId){
        return sdEquipmentApiService.getUnhandledFault(tunnelId);
    }

    /**
     * 设备类型
     * @param tunnelId
     * @return
     */
    @GetMapping("/getEquipmentType")
    public AjaxResult getEquipmentType(String tunnelId){
        return sdEquipmentApiService.getEquipmentType(tunnelId);
    }
}
