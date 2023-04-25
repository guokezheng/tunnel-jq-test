package com.tunnel.deal.mca.config;

import com.tunnel.deal.mca.netty.MCASocketClient;
import com.tunnel.deal.mca.service.McaService;
import com.zc.common.core.ThreadPool.ThreadPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 启动netty客户端
 */
@Component
public class ClientApplicationRunner implements ApplicationRunner {

    @Autowired
    private McaService mcaService;



//    /**
//     * 重连次数，默认是1次
//     */
//    @Value("${mca.client.reconnect_times:1}")
//    public Integer reconnectTimes;
    @Override
    public void run(ApplicationArguments args) throws Exception {

//        Map map = new HashMap<>();
//        map.put("ip","192.168.1.131");
//        map.put("port","502");
//        DeviceManager.deviceMap.put("131",map);
//        map.put("ip","192.168.1.127");
//        map.put("port","502");
//        DeviceManager.deviceMap.put("127",map);

        //项目启动时缓存测控执行器设备信息,待优化 todo
        mcaService.getDeviceList();
        //缓存测控执行器点位信息 todo

        ThreadPool.executor.execute(() -> {
            try {
                //重连次数设置
                MCASocketClient.getInstance().init(1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
