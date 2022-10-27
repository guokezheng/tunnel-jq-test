package com.tunnel.webthings.controller;

import com.ruoyi.common.core.page.Result;
import com.tunnel.business.domain.dataInfo.SdStateStorage;
import com.tunnel.webthings.service.SendMsgService;
import com.tunnel.webthings.vo.RadarMsgTopicVo;
import com.tunnel.webthings.vo.SendMsgVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * @author dear_dzy
 */
@RequestMapping("/sendMsg")
@RestController
@Api(tags = "物联中台")
public class SendMsgController {

    @Autowired
    private SendMsgService sendMsgService;

    @PostMapping("/direct")
    @ApiOperation("发送指令数据")
    public Result direct(@RequestParam("devNo") String devNo, @RequestParam("devType") String devType) {
        String s = sendMsgService.sendDirect(devNo, devType);
        return Result.success(s);
    }

    @PostMapping("/event")
    @ApiOperation("发送事件数据")
    public Result event(){
        return Result.success(sendMsgService.sendEvent());
    }

    @PutMapping("/storage")
    @ApiOperation("车道指示器测试")
    public Result storages(@RequestBody SdStateStorage sdStateStorage){
        return Result.success(sendMsgService.storages(sdStateStorage));
    }

    @PostMapping("/devStatus")
    @ApiOperation("发送状态数据")
    public Result devStatus(@RequestBody RadarMsgTopicVo vo){
        String ss = sendMsgService.sendDevStatus(vo);
        return Result.success(ss);
    }

}
