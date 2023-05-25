package com.tunnel.platform.bigscreen;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.tunnel.business.domain.bigScreenApi.SdProportionOfEquipment;
import com.tunnel.business.service.bigScreenApi.ISdProportionOfEquipmentServcie;
import com.tunnel.business.service.bigScreenApiN.ISdDevicesDataService;
import com.tunnel.business.service.bigScreenApiN.ISdTunnelsDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 *   设备运维
 *
 */
@RestController
@RequestMapping("/devicesData")
public class SdDevicesDataController extends BaseController {

    @Autowired
    private ISdDevicesDataService iSdDevicesDataService;


    /**
     * 综合统计
     * @return
     */
    @GetMapping("/index")
    public AjaxResult index(){
        return AjaxResult.success(iSdDevicesDataService.getIndexData());
    }

    /**
     * 设备故障预警
     * @return
     */
    @GetMapping("/faultWarn")
    public AjaxResult faultWarn(){
        return AjaxResult.success(iSdDevicesDataService.getFaultWarnData());
    }


    /**
     * 设备实时状态统计
     * @return
     */
    @GetMapping("/realTimeStat")
    public AjaxResult realTimeStat(){
        return AjaxResult.success(iSdDevicesDataService.getRealTimeStatData());
    }



    /**
     * 故障持续时间TOP10
     * @return
     */
    @GetMapping("/faultTimeTop")
    public AjaxResult faultTimeTop(){
        return AjaxResult.success(iSdDevicesDataService.getFaultTimeTopData());
    }


    /**
     * 品牌故障率TOP10
     * @return
     */
    @GetMapping("/monthFault")
    public AjaxResult monthFault(){
        return AjaxResult.success(iSdDevicesDataService.getMonthFaultData());
    }

    /**
     * 设备占比
     * @return
     */
    @GetMapping("/eqPercent")
    public AjaxResult eqPercent(){
        return AjaxResult.success(iSdDevicesDataService.getEqPercentData());
    }


    /**
     * 故障分类统计
     * @return
     */
    @GetMapping("/faultCategory")
    public AjaxResult faultCategoryMonth(){
        return AjaxResult.success(iSdDevicesDataService.getFaultCategoryData());
    }



}
