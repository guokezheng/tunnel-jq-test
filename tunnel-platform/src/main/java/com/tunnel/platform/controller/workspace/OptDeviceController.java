package com.tunnel.platform.controller.workspace;

import com.ruoyi.common.core.domain.AjaxResult;
import com.tunnel.platform.service.SdDeviceControlService;
import com.tunnel.platform.service.SdOptDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/optDevice")
public class OptDeviceController {

    @Value("${authorize.name}")
    private String deploymentType;
    @Autowired
    private SdOptDeviceService sdOptDeviceService;
    @Autowired
    private SdDeviceControlService sdDeviceControlService;

    @PostMapping(value = "/optSingleDevice")
    public AjaxResult optSingleDevice(@RequestBody Map<String, Object> params) {
        Integer workState = 0;
        if ("GLZ".equals(deploymentType)) {
            workState = sdDeviceControlService.controlDevices(params);
        } else {
            workState = sdOptDeviceService.optSingleDevice(params);
        }
        return AjaxResult.success(workState);
    }

}
