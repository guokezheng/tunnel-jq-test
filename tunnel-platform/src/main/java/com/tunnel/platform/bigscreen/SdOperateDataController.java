package com.tunnel.platform.bigscreen;

import com.ruoyi.common.core.domain.AjaxResult;
import com.tunnel.business.service.bigScreenApiN.ISdOperateDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/operateData")
public class SdOperateDataController {
    @Autowired
    private ISdOperateDataService iSdOperateDataService;


    /**
     * 综合统计
     * @return
     */
    @GetMapping("/index")
    public AjaxResult index(){
        return AjaxResult.success(iSdOperateDataService.getIndexData());
    }

    /**
     * 昨日运营日流量
     * @return
     */
    @GetMapping("/yesterdayFlow")
    public AjaxResult yesterdayFlow(){
        return AjaxResult.success(iSdOperateDataService.getYesterdayFlowData());
    }

    /**
     * 路况信息
     * @return
     */
    @GetMapping("/event")
    public AjaxResult event(){
        return AjaxResult.success(iSdOperateDataService.getEventData());
    }
}
