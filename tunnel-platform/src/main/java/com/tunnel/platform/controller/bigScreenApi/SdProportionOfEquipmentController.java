package com.tunnel.platform.controller.bigScreenApi;

import com.oracle.net.Sdp;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.tunnel.business.domain.bigScreenApi.SdProportionOfEquipment;
import com.tunnel.business.service.bigScreenApi.ISdProportionOfEquipmentServcie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 全视图  设备占比
 *
 * @author wuhaoyang
 * @date 2021-12-10
 */
@RestController
@RequestMapping("/proportionOfEquipment")
public class SdProportionOfEquipmentController extends BaseController {

    @Autowired
    private ISdProportionOfEquipmentServcie iSdProportionOfEquipmentServcie;

    /**
     * 查询设备占比
     * @param tunnelId
     * @return
     */
    @GetMapping("/list")
    public AjaxResult getProportionOfEquipment(String tunnelId){
        List<Map<String, Object>> proportionOfEquipment = iSdProportionOfEquipmentServcie.getProportionOfEquipment(tunnelId);
        return AjaxResult.success(proportionOfEquipment);
    }

    /**
     * 获取设备运行状态
     * @return
     */
    @GetMapping(value = "/getEquipmentStatus")
    public AjaxResult getEquipmentOperationStatus(String tunnelId){
        List<SdProportionOfEquipment> equipmentOperationStatus = iSdProportionOfEquipmentServcie.getEquipmentOperationStatus(tunnelId);
        return AjaxResult.success(equipmentOperationStatus);
    }
}
