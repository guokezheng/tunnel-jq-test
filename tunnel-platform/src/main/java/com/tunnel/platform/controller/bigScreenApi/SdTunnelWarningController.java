package com.tunnel.platform.controller.bigScreenApi;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.tunnel.business.service.bigScreenApi.ISdTunnelWarningService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

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
    @ApiOperation("查询近30日隧道预警")
    @GetMapping("/warningnum")
    public AjaxResult getTunnelWarningNumber(String tunnelId){
        List<Long> tunnelWarningNumber = iSdTunnelWarningService.getTunnelWarningNumber(tunnelId);
        return AjaxResult.success(tunnelWarningNumber);
    }

    /**
     * 查询近30日隧道预警带日期
     * @return
     */
    @ApiOperation("查询近30日隧道预警带日期")
    @GetMapping("/getTunnelWarningNumData")
    public AjaxResult getTunnelWarningNumData(String tunnelId, String startDate, String endDate){
        List<Map<String, Object>> list = iSdTunnelWarningService.getTunnelWarningNumData(tunnelId,startDate,endDate);
        return AjaxResult.success(list);
    }
}
