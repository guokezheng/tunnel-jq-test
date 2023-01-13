package com.tunnel.platform.controller.bigScreenApi;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.tunnel.business.service.bigScreenApi.ISdTunnelWarningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 近30日隧道预警信息Controller
 *
 * @author wuhaoyang
 * @date 2021-12-13
 */
@RestController
@RequestMapping("/tunnelWarning")
public class SdTunnelWarningController extends BaseController {

    @Autowired
    private ISdTunnelWarningService iSdTunnelWarningService;

    /**
     * 查询近30日隧道预警
     * @return
     */
    @GetMapping("/warningnum")
    public AjaxResult getTunnelWarningNumber(String tunnelId){
        List<Long> tunnelWarningNumber = iSdTunnelWarningService.getTunnelWarningNumber(tunnelId);
        return AjaxResult.success(tunnelWarningNumber);
    }

}
