package com.tunnel.deal.vehicleinspection;

import com.ruoyi.common.utils.spring.SpringUtils;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeEnum;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.mapper.dataInfo.SdDevicesMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MicrowaveNettyConfig {

    private static final Logger log = LoggerFactory.getLogger(MicrowaveNettyConfig.class);

    @Autowired
    MicrowaveNettyClientHandler nettyClientHandler;

    private static SdDevicesMapper sdDevicesMapper = SpringUtils.getBean(SdDevicesMapper.class);


    @PostConstruct
    public void init() {
        MicrowaveNettyClient.setHandler(nettyClientHandler);

        connectDevice();
    }

    //@Scheduled(cron = "30/60 * * * * ?")
    public void connectDevice() {
        log.info("剔除长时间不活动的终端");
        long curt = System.currentTimeMillis();
        for(Map.Entry<String, NettyConnectInfo> entry : MicrowaveNettyClient.channels.entrySet()) {
            String ip = entry.getKey();
            NettyConnectInfo info = entry.getValue();
            long actt = info.getActiveTime().getTime();
            //log.info("{},{}", curt, actt);
            if(curt-actt > 130000) {
                log.info("--{}-连接2分钟无活动！", ip);
                info.getChannel().close();
                MicrowaveNettyClient.channels.remove(ip);
            }
        }

        log.info("连接终端");
        new Thread() {
            @Override
            public void run() {
                try {
                    Map<String, NettyConnectInfo> mapParam = new HashMap<String, NettyConnectInfo>();
                    //加强照明调光   需要 微波车检检测车辆经过信息。从而开启调光。需要 每个隧道入口 前的 微波车检  监控过车 信息。
                    //后期  针对性查找  每个隧道入口的   微波车检 信息存入  mapParam
                    //从而进行链接接收过车数据

                    // device from db
                    SdDevices sdDevices = new SdDevices();
                    sdDevices.setEqTypes(new Long[]{DevicesTypeEnum.WEI_BO_CHE_JIAN.getCode()});
                    List<SdDevices> devicesList = sdDevicesMapper.selectSdDevicesListByParam(sdDevices);
//                    List<DeviceEntity> list = deviceRepository.getIps();
                    for(SdDevices it : devicesList) {
                        //隧道入口微波车检  可用。   仅用来测试使用。后期优化。
                        if("10.7.187.145".equals(it.getIp())||"10.7.187.147".equals(it.getIp())){
                            NettyConnectInfo info = new NettyConnectInfo();
                            info.setPort(Integer.parseInt(it.getPort()));
                            info.setChannel(null);
                            info.setConnected(false);
                            info.setSdDevices(it);
                            mapParam.put(it.getIp()+":"+(it.getPort()), info);
                        }
                    }
                    // connect
                    MicrowaveNettyClient.getChannel(mapParam);
                }
                catch (Exception e) {
                    log.error("Netty client start fail : {}", e.getMessage());
                }
            }
        }.start();
    }

}
