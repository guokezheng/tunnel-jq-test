package com.tunnel.platform.controller.bigScreenApi;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.tunnel.platform.service.bigScreenApi.ISdAlarmNumberService;
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
@RequestMapping("/alarmNumber")
public class SdAlarmNumberController extends BaseController {

    @Autowired
    private ISdAlarmNumberService iSdAlarmNumberService;

    /**
     * 查询今日总预警事件信息
     * */
    @PostMapping("/warnmsg")
    public AjaxResult getTodayWarningmsg(){
        Map<String, Object> todayWarningmsg = iSdAlarmNumberService.getTodayWarningmsg();
        return AjaxResult.success(todayWarningmsg);
    }

    /**
     * 查询今日事故事件信息
     * */
    @PostMapping("/accidentmsg")
    public AjaxResult getTodayAccidentmsg(){
        Map<String, Object> todayAccidentmsg = iSdAlarmNumberService.getTodayAccidentmsg();
        return AjaxResult.success(todayAccidentmsg);
    }

}
