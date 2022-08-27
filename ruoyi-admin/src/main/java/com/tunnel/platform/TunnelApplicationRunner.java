package com.tunnel.platform;

import com.ruoyi.common.utils.spring.SpringUtils;
import com.tunnel.plc.plcutil.CmdUtil;
import com.tunnel.plc.plcutil.DataUtil;
import com.zc.common.core.redis.RedisPubSub;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * 程序启动执行
 */
@Component
public class TunnelApplicationRunner implements ApplicationRunner {

//    @Autowired
//    private UDPNettyServer udpNettyServer;
//    @Autowired
//    private static ISdDevicesService sdDevicesService;

    @Override
    public void run(ApplicationArguments arg0) throws Exception {
//        onServerStarted();
        redisSub();

    }

    private void redisSub() {
        SpringUtils.getBean(RedisPubSub.class).subscribe("PLC:CONTROL");
    }

    @Bean
    CmdUtil cmdInitCIO() {
        //初始化所有要监控的Tunnel-PLC-Device-Cmd-CIO
        return new CmdUtil();
    }

    @Bean
    CmdUtil cmdInitDM() {
        //初始化所有要监控的Tunnel-PLC-Device-Cmd-DM
        return new CmdUtil();
    }

    @Bean
    DataUtil init() {
        return new DataUtil();
    }

//    public void onServerStarted() {
//        //
//
//
//        //读取Netty配置文件
//        List<Integer> portList = new ArrayList<Integer>();
//        List<String> handlerList = new ArrayList<String>();
//        // 消防设备通信
//        portList.add(6000);
//        handlerList.add("com.tunnel.platform.othersystem.netty.FireNettyServerHandler");
//        // 光强设备等通用
//        portList.add(6001);
//        handlerList.add("com.tunnel.platform.othersystem.netty.IotNettyServerHandler");
//        nettyServer.setPortList(portList);
//        nettyServer.setHandlerList(handlerList);
//        new Thread(nettyServer).start();
//
//        /*List<String> ipList = new ArrayList<String>();
//        portList = new ArrayList<Integer>();
//        handlerList = new ArrayList<String>();
//        //微波车检设备通信
//        sdDevicesService = (ISdDevicesService) SpringContextUtils.getBean(ISdDevicesService.class);
//        SdDevices sdDevices = new SdDevices();
//        sdDevices.setEqType(108l);
//        List<SdDevices> wbcjList = sdDevicesService.selectSdDevicesList(sdDevices);
//        for(int i=0;i<wbcjList.size();i++){
//        	String ip = wbcjList.get(i).getEqFeedbackAddress1();
//        	if(ip == null || "".equals(ip)){
//        		continue;
//        	}
//        	portList.add(20001);
//            handlerList.add("com.tunnel.platform.othersystem.netty.MicrowaveNettyServerHandler");
//            ipList.add(ip);
//        }
//        nettyClient.setPortList(portList);
//        nettyClient.setIpList(ipList);
//        nettyClient.setHandlerList(handlerList);
//        if(ipList.size() > 0 ){
//        	if(wbcjList.get(0).getEqTunnelId().contains("ShanTing")){
//        		new Thread(nettyClient).start();
//        	}
//        }*/
//        //事件设备通信
//        udpNettyServer.setPort(9101);
//        new Thread(udpNettyServer).start();
//    }


}
