package com.tunnel.platform.controller.digitalmodel;

import com.ruoyi.common.core.domain.AjaxResult;
import com.tunnel.platform.service.event.ISdEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author dzy
 * @date 2022/9/2 9:41
 * 万集事件传入接口
 */
@RestController
@RequestMapping("/wjData")
public class WjEventController {

    @Autowired
    private ISdEventService service;

    @PostMapping("/eventData")
    public AjaxResult eventData(@RequestBody Map<String,Object> map){
        return AjaxResult.success(service.insertWjEvent(map));
    }
}
