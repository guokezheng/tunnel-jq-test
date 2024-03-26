package com.tunnel.platform.controller.illumination;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.tunnel.business.service.bigScreenApi.ISdAlarmNumberService;
import com.tunnel.business.service.dataInfo.IlluminationFluctuateService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 大屏总预警、事件事故、故障信息Controller
 *
 * @author wuhaoyang
 * @date 2021-12-13
 */
@RestController
@RequestMapping("/illumination")
public class IlluminationFluctuateController extends BaseController {

    @Autowired
    private IlluminationFluctuateService lluminationFluctuateService;

    /**
     * 波动模式开启
     * */
    @PostMapping("/openIllumination")
    @ApiOperation("波动模式开启")
    public AjaxResult openIllumination(){
        boolean openBoolean = lluminationFluctuateService.openIllumination();

        return AjaxResult.success(openBoolean);
    }

    /**
     * 波动模式关闭
     * */
    @PostMapping("/closeIllumination")
    @ApiOperation("波动模式关闭")
    public AjaxResult closeIllumination(){
        boolean closeBoolean = lluminationFluctuateService.closeIllumination();
        return AjaxResult.success(closeBoolean);
    }

}
