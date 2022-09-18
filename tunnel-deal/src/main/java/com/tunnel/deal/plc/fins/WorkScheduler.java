package com.tunnel.deal.plc.fins;

import com.ruoyi.common.core.redis.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WorkScheduler {
    public static CmdProcess cmdProcess = new CmdProcess();
    public static PlcProcess plcProcess = new PlcProcess();
    @Autowired
    private RedisCache redisCache;


    //    @Scheduled(fixedRate = 1000 * 60)
    public void updateCmdCache() {
        cmdProcess.updateCmdCache();
    }


    //    @Scheduled(fixedRate = 1000)
    public void insertMlgDB() {
        //获取可用的plc
        plcProcess.insertMlgDB();
    }
}