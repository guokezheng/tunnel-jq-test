package com.tunnel.platform.controller.energyManagement;

import com.ruoyi.common.core.domain.AjaxResult;
import com.tunnel.business.domain.dataInfo.SdTunnels;
import com.tunnel.business.service.energyManagement.SdZeroCarBonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhai
 * @date 2023/9/4
 */
@RestController
@RequestMapping("/zeroCarBon")
@Api(tags = "零碳大屏")
public class SdZeroCarBonController {

    @Autowired
    public SdZeroCarBonService sdZeroCarBonService;

    /**
     * 获取零碳大屏url
     * @return
     */
    @ApiOperation("获取零碳大屏url")
    @GetMapping("/getZeroUrl")
    public AjaxResult getZeroUrl(SdTunnels sdTunnels){
        String zeroUrl = sdZeroCarBonService.getZeroUrl(sdTunnels.getTunnelId());
        return AjaxResult.success("",zeroUrl);
    }
}
