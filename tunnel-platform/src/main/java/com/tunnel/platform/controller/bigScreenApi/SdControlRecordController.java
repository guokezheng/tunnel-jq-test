package com.tunnel.platform.controller.bigScreenApi;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.tunnel.business.service.bigScreenApi.ISdControlRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 近12小时控制记录信息Controller
 *
 * @author wuhaoyang
 * @date 2021-12-13
 */
@RestController
@RequestMapping("/controlRecord")
public class SdControlRecordController extends BaseController {

    @Autowired
    private ISdControlRecordService iSdControlRecordService;

    @PostMapping("/recordlist")
    public AjaxResult getRecentControlRecordMsg(String tunnelId){
        List<Map<String, Object>> recentControlRecordMsg = iSdControlRecordService.getRecentControlRecordMsg(tunnelId);
        return AjaxResult.success(recentControlRecordMsg);
    }

}
