package com.tunnel.platform.bigscreen;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.tunnel.business.domain.bigScreenApi.SdProportionOfEquipment;
import com.tunnel.business.service.bigScreenApi.ISdProportionOfEquipmentServcie;
import com.tunnel.business.service.bigScreenApiN.ISdTunnelsDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 *   综合态势
 *
 */
@RestController
@RequestMapping("/tunnelsData")
public class SdTunnelsDataController extends BaseController {

    @Autowired
    private ISdTunnelsDataService iSdTunnelsDataService;


    /**
     * 综合统计
     * @return
     */
    @GetMapping("/index")
    public AjaxResult index(){
        return AjaxResult.success(iSdTunnelsDataService.getIndexData());
    }

    /**
     * 查询隧道重要事件情况
     * @return
     */
    @GetMapping("/majorEvent")
    public AjaxResult majorEvent(){
        return AjaxResult.success(iSdTunnelsDataService.getMajorEventData());
    }

    /**
     * 分隧道事件统计
     * @return
     */
    @GetMapping("/eventStat")
    public AjaxResult eventStat(){
        return AjaxResult.success(iSdTunnelsDataService.getEventStatData());
    }


    /**
     * 感知事件类型统计
     * @return
     */
    @GetMapping("/findEventStat")
    public AjaxResult findEventStat(){
        return AjaxResult.success(iSdTunnelsDataService.getFindEventStatData());
    }


    /**
     * 隧道车流量情况
     * @return
     */
    @GetMapping("/carFlow")
    public AjaxResult carFlow(){
        return AjaxResult.success(iSdTunnelsDataService.getCarFlowData());
    }


    /**
     * 各隧道实时车流量
     * @return
     */
    @GetMapping("/realTimeCarFlow")
    public AjaxResult realTimeCarFlow(){
        return AjaxResult.success(iSdTunnelsDataService.getRealTimeCarFlow());
    }

    /**
     * 隧道实时拥挤度
     * @return
     */
    @GetMapping("/realTimeCongestion")
    public AjaxResult realTimeCongestion(){
        return AjaxResult.success(iSdTunnelsDataService.realTimeCongestion());
    }

}
