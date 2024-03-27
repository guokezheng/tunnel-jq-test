package com.tunnel.platform.controller.deviceControl;

import com.alibaba.fastjson.JSONObject;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.ruoyi.common.core.domain.AjaxResult;
import com.tunnel.platform.service.deviceControl.PhoneSpkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Api(tags = "电话广播")
@RestController
@RequestMapping("/phoneSpk")
@ApiSupport(order = 16)
public class PhoneSpkController {

    @Autowired
    private PhoneSpkService phoneSpkService;

    /**
     * 接收电话websocket消息
     * @param jsonObject
     */
    @ApiOperation("接收电话websocket消息")
    @PostMapping(value = "/onMessage")
    public void onMessage(@RequestBody JSONObject jsonObject) {
        phoneSpkService.onMessage(jsonObject);
    }

    /**
     * 获取音频文件列表
     * @param map
     * @return
     */
    @ApiOperation("获取音频文件列表")
    @PostMapping(value = "/getAudioFileList")
    public AjaxResult getAudioFileList(@RequestBody Map<String, Object> map) {
        return phoneSpkService.getAudioFileList(map);
    }

    /**
     * 播放音频
     *
     * @param map
     * @return
     */
    @ApiOperation("播放音频")
    @PostMapping(value = "/playVoice")
    public AjaxResult playVoice(@RequestBody Map<String, Object> map) {
        return phoneSpkService.playVoice(map);
    }

    /**
     * 左洞或右洞整体播放
     *
     * @param map
     * @return
     */
    @ApiOperation("左洞或右洞整体播放")
    @PostMapping(value = "/playVoiceGroup")
    public AjaxResult playVoiceGroup(@RequestBody Map<String, Object> map) {
        return phoneSpkService.playVoiceGroup(map);
    }

    /**
     *
     * @param map
     * @return
     */
    @ApiOperation("挂断")
    @PostMapping(value = "/hungUp")
    public AjaxResult hungUp(@RequestBody Map<String, Object> map) {
        return phoneSpkService.hungUp(map);
    }

}
