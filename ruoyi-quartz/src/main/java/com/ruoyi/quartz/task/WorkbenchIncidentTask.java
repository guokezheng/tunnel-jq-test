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

    @Scheduled(fixedDelay=500)
    public void incidentNumTask(){
        long l = System.currentTimeMillis();
        try{
            Integer eventUntreatedNum = SpringUtils.getBean(SdEventMapper.class).getEventUntreatedNum();
            Random r = new Random();

            WebSocketService.broadcast("eventUntreatedNum", r.nextInt());
        }catch (Exception e){
            e.printStackTrace();
        }
        long l1 = System.currentTimeMillis();
        System.out.print(l1-l);

    }
}
