package com.tunnel.platform.controller.digitalmodel;

import com.ruoyi.common.core.domain.AjaxResult;
import com.tunnel.platform.service.event.ISdEventService;
import com.zc.common.core.websocket.WebSocketService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
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


    /**
     * 事件数据
     * @param map
     * @return
     */
    @PostMapping("/eventData")
    public AjaxResult eventData(@RequestBody Map<String,Object> map){
        return AjaxResult.success(service.insertWjEvent(map));
    }

    /**
     * 事件图片
     * @param map
     */
    @PostMapping("/eventImage")
    public AjaxResult eventImage(@RequestBody Map<String,Object> map){
        return AjaxResult.success(service.uploadPic(map));
    }

    /**
     * 事件视频
     */
    @PostMapping("/eventVideo")
    public AjaxResult eventVideo(@RequestBody Map<String,Object> map){
        return AjaxResult.success(service.eventVideo(map));
    }

    /**
     * 重点车辆
     */
    @PostMapping("/specialCar")
    public AjaxResult specialCar(@RequestBody Map<String,Object> map){
        return AjaxResult.success(service.specialCar(map));
    }
}
