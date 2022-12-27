package com.tunnel.platform.controller.video;

import com.tunnel.platform.service.SdIntegratedVideoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.core.page.Result;

import java.util.Map;

@RestController
@RequestMapping("/integrated/video")
public class SdIntegratedVideoController {

    @Autowired
    private SdIntegratedVideoService sdIntegratedVideoService;

    @GetMapping("/videoStreaming/{camId}")
    @ApiOperation("打开相机实时流")
    public Result videoStreaming(@PathVariable("camId") String camId)
    {
        return Result.success(sdIntegratedVideoService.getVideoStreaming(camId));
    }

    @GetMapping("/PTZControl")
    @ApiOperation("云台控制")
    public Result PTZControl(@RequestParam Map param)
    {
        return Result.success(sdIntegratedVideoService.PTZControl(param));
    }

//    @GetMapping("/camList")
//    @ApiOperation("部门下设备数据同步")
//    public Result camList()
//    {
//        return Result.success(sdIntegratedVideoService.getDeptCamList());
//    }

}
