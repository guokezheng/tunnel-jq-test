package com.tunnel.platform.controller.workspace;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.ruoyi.common.core.domain.AjaxResult;
import com.tunnel.platform.service.SdDeviceControlService;
import com.tunnel.platform.service.SdOptDeviceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/optDevice")
@Api(tags = "gsy组装控制参数Controller")
@ApiSupport(order = 16)
public class OptDeviceController {

    @Value("${authorize.name}")
    private String deploymentType;
    @Autowired
    private SdOptDeviceService sdOptDeviceService;
    @Autowired
    private SdDeviceControlService sdDeviceControlService;

    @PostMapping(value = "/optSingleDevice")
    @ApiOperation("gsy组装控制参数")
    public AjaxResult optSingleDevice(@RequestBody Map<String, Object> params) {
        Integer workState = 0;
        if ("GSY".equals(deploymentType)) {
            workState = sdOptDeviceService.optSingleDevice(params);
        }
        return AjaxResult.success(workState);
    }

}
