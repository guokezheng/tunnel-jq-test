package com.zc.video.controller;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.Result;
import com.zc.video.service.CameraSptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @author ：xieyuwei
 * @date ：Created in 2022/7/12 8:58
 * 文件说明： </p>
 */
@RestController
@RequestMapping("/vedio/api")
@Api(tags = "视频中台")
@ApiSupport(order = 16)
public class CameraSptController {

    @Autowired
    private CameraSptService cameraSptService;

    /**
     * 相机实时流
     *
     * @param type  系统编号 默认1 后期通知更改
     * @param camId 相机编号
     * @return
     */
    @GetMapping("/liveStream")
    @ApiOperation("相机实时流")
    public Result liveStreaming(@RequestParam("type") @ApiParam(value = "系统编号 默认1 后期通知更改")String type, @RequestParam("camId") @ApiParam(value = "相机编号")String camId) {
        return Result.success(cameraSptService.liveStreaming(type, camId));
    }

    /**
     * 查询附近相机
     *
     * @param devLat   摄像机纬度
     * @param devLon   摄像机经度
     * @param distance 距离单位米
     * @return
     */
    @GetMapping("/queryNearbyCamera")
    @ApiOperation("查询附近相机")
    public Result queryNearbyCamera(@RequestParam("devLat") @ApiParam(value = "摄像机纬度") String devLat, @RequestParam("devLon") @ApiParam(value = "摄像机经度") String devLon, @RequestParam("distance")  @ApiParam(value = "距离单位米")String distance) {
        return Result.toResult((Integer) cameraSptService.queryNearbyCamera(devLat, devLon, distance));

    }

    /**
     * 云台控制
     *
     * @param msgType 传入值“3”
     * @param camID   相机编号
     * @param cmdType
     * @param speed   当cmdType小于等于28时为云台动作的速度，预置位设置及调用表示对应的预置位编号；
     * @param type
     * @return
     */
    @GetMapping("/gimbalControl")
    @ApiOperation("云台控制")
    public Result gimbalControl(@RequestParam("type") String type, @RequestParam("camID")  @ApiParam(value = "相机编号")String camID, @RequestParam("msgType")  @ApiParam(value = "传入值“3”")String msgType, @RequestParam("cmdType")String cmdType, @RequestParam("speed")  @ApiParam(value = "当cmdType小于等于28时为云台动作的速度，预置位设置及调用表示对应的预置位编号；")String speed) {
        return Result.toResult((Integer) cameraSptService.gimbalControl(type, camID, msgType, cmdType, speed));
    }
}
