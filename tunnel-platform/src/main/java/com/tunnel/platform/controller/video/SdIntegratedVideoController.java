package com.tunnel.platform.controller.video;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.tunnel.platform.service.SdIntegratedVideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.core.page.Result;

import java.util.Map;

@RestController
@RequestMapping("/integrated/video")
@Api(tags = "视频信息Controller")
@ApiSupport(order = 16)
public class SdIntegratedVideoController {

    @Autowired
    private SdIntegratedVideoService sdIntegratedVideoService;

    @GetMapping("/videoStreaming/{camId}")
    @ApiOperation("打开相机实时流")
    public Result videoStreaming(@PathVariable("camId") String camId)
    {
        return sdIntegratedVideoService.getVideoStreaming(camId);
    }

    @GetMapping("/PTZControl")
    @ApiOperation("云台控制")
    public Result PTZControl(@RequestParam Map param)
    {
        return Result.success(sdIntegratedVideoService.PTZControl(param));
    }

    @GetMapping("/nearCamList")
    @ApiOperation("查询附近相机")
    public Result nearCamList(@RequestParam Map param)
    {
        return Result.success(sdIntegratedVideoService.nearCamList(param));
    }

    @GetMapping("/presetList/{camId}")
    @ApiOperation("查询相机预置位")
    public Result presetList(@PathVariable("camId") String camId)
    {
        return Result.success(sdIntegratedVideoService.presetList(camId));
    }

    @GetMapping("/addPreset")
    @ApiOperation("新增预置位")
    public Result addPreset(@RequestParam Map param)
    {
        return Result.success(sdIntegratedVideoService.addPreset(param));
    }

//    @GetMapping("/camList")
//    @ApiOperation("部门下设备数据同步")
//    public Result camList()
//    {
//        return Result.success(sdIntegratedVideoService.getDeptCamList());
//    }

}
