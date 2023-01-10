package com.tunnel.platform.controller.deviceControl;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.domain.AjaxResult;
import com.tunnel.platform.service.deviceControl.PhoneSpkService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.UnknownHostException;
import java.util.Map;

@Api(tags = "电话广播")
@RestController
@RequestMapping("/phoneSpk")
public class PhoneSpkController {
    @Value("${authorize.name}")
    private String deploymentType;
    @Autowired
    private PhoneSpkService phoneSpkService;

    @PostMapping(value = "/onMessage")
    public void onMessage(@RequestBody JSONObject jsonObject) {
        phoneSpkService.onMessage(jsonObject);
    }


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
    @PostMapping(value = "/playVoice")
    public AjaxResult playVoice(@RequestBody Map<String, Object> map) throws UnknownHostException {
        return phoneSpkService.playVoice(map);
    }

    /**
     * 左洞或右洞整体播放
     * @param map
     * @return
     */
    @PostMapping(value = "/playVoiceGroup")
    public AjaxResult playVoiceGroup(@RequestBody Map<String, Object> map) throws UnknownHostException {
        return phoneSpkService.playVoiceGroup(map);
    }

    @PostMapping(value = "/hungUp")
    public AjaxResult hungUp(@RequestBody Map<String, Object> map) {
        return phoneSpkService.hungUp(map);
    }

}
