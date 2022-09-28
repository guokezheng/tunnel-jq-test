package com.tunnel.platform;

import com.ruoyi.common.utils.spring.SpringUtils;
import com.tunnel.deal.plc.fins.CmdProcess;
import com.zc.common.core.redis.pubsub.RedisPubSub;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 程序启动执行
 */
@Component
public class TunnelApplicationRunner implements ApplicationRunner {


    @Override
    public void run(ApplicationArguments arg0) throws Exception {
//        onServerStarted();
        redisSub();
        //欧盟龙FINS协议
//        CmdUtil cmdInitCIO = cmdInitCIO();//初始化所有要监控的Tunnel-PLC-Device-Cmd-CIO
//        CmdUtil cmdInitDM = cmdInitDM();//初始化所有要监控的Tunnel-PLC-Device-Cmd-DM
//        DataUtil dataUtil = dataInit();

    }
    private void redisSub() {
        SpringUtils.getBean(RedisPubSub.class).subscribe("PLC:CONTROL");
        SpringUtils.getBean(RedisPubSub.class).subscribe("GL:CONTROL");
    }
    CmdProcess.CmdUtil cmdInitCIO() {
        return new CmdProcess.CmdUtil();
    }
    CmdProcess.CmdUtil cmdInitDM() {
        return new CmdProcess.CmdUtil();
    }
    CmdProcess.DataUtil dataInit() {
        return new CmdProcess.DataUtil();
    }

}
