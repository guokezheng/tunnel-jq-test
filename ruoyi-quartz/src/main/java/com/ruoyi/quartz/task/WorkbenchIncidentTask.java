package com.ruoyi.quartz.task;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.framework.web.domain.server.Sys;
import com.tunnel.business.mapper.event.SdEventMapper;
import com.zc.common.core.websocket.WebSocketService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 工作台事件预警定时任务
 */
@Component("WorkbenchIncidentTask")
public class WorkbenchIncidentTask {

//    @Scheduled(fixedDelay=3000)
    public void incidentNumTask(){
        try{
            Integer eventUntreatedNum = SpringUtils.getBean(SdEventMapper.class).getEventUntreatedNum();
            System.out.print("事件总数"+eventUntreatedNum);
            WebSocketService.broadcast("eventUntreatedNum", eventUntreatedNum);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
