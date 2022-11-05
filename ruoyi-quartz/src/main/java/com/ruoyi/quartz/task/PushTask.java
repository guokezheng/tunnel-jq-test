package com.ruoyi.quartz.task;

import com.tunnel.business.service.platformAuthApi.PlatformApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zhai
 * @date 2022/11/4
 */
@Component("pushTask")
public class PushTask {

    private static final Logger log = LoggerFactory.getLogger(PushTask.class);

    @Autowired
    private PlatformApiService platformApiService;

    public void pushData(){
        platformApiService.pushTask();
    }
}
